package smith.lib.net;

import java.util.Map;

public interface SConnectCallBack {
	public void onSuccess(SResponse response, String tag, Map<String, Object> responseHeaders);
	public void onFailure(SResponse response, String tag);
}