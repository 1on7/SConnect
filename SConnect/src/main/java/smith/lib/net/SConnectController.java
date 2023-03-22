package smith.lib.net;

import com.google.gson.Gson;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.*;
import okhttp3.*;

@SuppressWarnings({"Unused"})
class SConnectController {
    
    private static final int SOCKET_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 25000;
    protected OkHttpClient client;
    private static SConnectController mInstance;

    public static synchronized SConnectController getInstance() {
        if (mInstance == null) mInstance = new SConnectController();
        return mInstance;
    }

    private OkHttpClient getClient() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            try {
                final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[] {};
                        }
                    }
                };

                final SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustAllCerts, new SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                builder.connectTimeout(SOCKET_TIMEOUT, TimeUnit.MILLISECONDS);
                builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
                builder.writeTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            } catch (Exception e) {}

            client = builder.build();
        }

        return client;
    }

    public void connect(final SConnect sconnect, String method, String url, final String tag, final SConnectCallBack callback) {
        Request.Builder reqBuilder = new Request.Builder();
        Headers.Builder headerBuilder = new Headers.Builder();

        if (sconnect.getHeaders().size() > 0) {
            HashMap<String, Object> headers = sconnect.getHeaders();
            for (HashMap.Entry<String, Object> header : headers.entrySet()) {
                headerBuilder.add(header.getKey(), String.valueOf(header.getValue()));
            }
        }

        try {
            if (sconnect.getConnectType() == SConnect.PARAM) {
                if (method.equals(SConnect.GET)) {
                    HttpUrl.Builder httpBuilder;
                    try {
                        httpBuilder = HttpUrl.parse(url).newBuilder();
                    } catch (NullPointerException ne) {
                        throw new NullPointerException("unexpected url: " + url);
                    }

                    if (sconnect.getParams().size() > 0) {
                        HashMap<String, Object> params = sconnect.getParams();
                        for (HashMap.Entry<String, Object> param : params.entrySet()) {
                            httpBuilder.addQueryParameter(param.getKey(), String.valueOf(param.getValue()));
                        }
                    }

                    reqBuilder.url(httpBuilder.build()).headers(headerBuilder.build()).get();
                } else {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    if (sconnect.getParams().size() > 0) {
                        HashMap<String, Object> params = sconnect.getParams();
                        for (HashMap.Entry<String, Object> param : params.entrySet()) {
                            formBuilder.add(param.getKey(), String.valueOf(param.getValue()));
                        }
                    }
                    RequestBody reqBody = formBuilder.build();
                    reqBuilder.url(url).headers(headerBuilder.build()).method(method, reqBody);
                }
            } else {
                RequestBody reqBody = RequestBody.create("application/json; charset=utf-8;", MediaType.parse(new Gson().toJson(sconnect.getParams())));
                if (method.equals(SConnect.GET)) {
                    reqBuilder.url(url)
                        .headers(headerBuilder.build())
                        .get();
                } else {
                    reqBuilder.url(url)
                        .headers(headerBuilder.build())
                        .method(method, reqBody);
                }
            }

            Request req = reqBuilder.build();

            getClient().newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    sconnect.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFialure(new SResponse(e.getMessage()), tag);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String responseBody = response.body().string().trim();
                    sconnect.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Headers b = response.headers();
                            HashMap<String, Object> map = new HashMap<>();
                            for (String s : b.names()) {
                                map.put(s, b.get(s) != null ? b.get(s) : "null");
                            }
                            callback.onSuccess(new SResponse(responseBody), tag, map);
                        }
                    });
                }
            });
        } catch (Exception e) {
            callback.onFailure(new SResponse(e.getMessage()), tag);
        }
    }
}