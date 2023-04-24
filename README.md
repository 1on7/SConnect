# SConnect

![stability-stable](https://img.shields.io/badge/stability-stable-green.svg)
![minimumSDK](https://img.shields.io/badge/minSDK-21-f39f37)
![stable version](https://img.shields.io/badge/stable_version-v3.0-blue)
![Repository size](https://img.shields.io/github/repo-size/smith8h/SConnect)
<br/>

**(S-Connect)** A Http client based library that use *OkHttp3* for simply making requests to URLs and APIs, and get a response as Json or plain text.

<br/>

**Content**
- [**Setup**](#setup)
- [**Documentations**](#documentations)
- [**Donations :heart:**](#donations)
<br/>

# Setup
> **Step 1.** Add the JitPack repository to your build file.</br>
Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
```
> **Step 2.** Add the dependency:
```gradle
dependencies {
    implementation 'com.github.smith8h:SConnect:v3.1'
}
```

<br/>

# Documentation
To create a connect first pass a context using `with()` method:
```java
    SConnect.with(this)
```
Then pass the callback interface to deal with the response using `callback()` method:
```java
    .callback(new SConnectCallBack() {
        @Override
        public void onFailure(SResponse response, String tag) {}
            
        @Override
        public void onSuccess(SResponse response, String tag, HashMap<String, Object> responseHeaders) {
            if (response.isJSON() && response.isMap()) {
                Toast.makeText(context, response.getMap().getString("key"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    })
```
After that, if you need to add headers, params to your connection. add them using `headers()` and `params()` methods:
```java
    .headers(Map<String, String>)
    .params(Map<String, String>, SConnect.PARAM)
    // or SConnect.BODY
```
then pass the url using `url()` method:
```java
    .url("url")
```
finaly, use any of `get()`, `post()`, `put()` or `delete()` methods corrisponding to your connection:
> you can pass a tag to recognize multiple connections
```java
    .get() // post() | put() | delete()
    // or get(tag) | post(tag) | put(tag) | delete(tag)
```

**Final codes**:
• connections doesn't need params/headers:
```java
    SConnect.with(this)
            .callback(callback)
            .url(url)
            .get(); // post | put | delete
            // also pass tag if you need to recognize multiple requesrs which one is giving response
```
• connections need params/headers:
```java
    SConnect.with(this)
            .callback(callback)
            .params(params, SConnect.PARAM) // BODY
            .headers(headers)
            .url(url)
            .get(); // post | put | delete
            // also pass tag if you need to recognize multiple requesrs which one is giving response
```

<br/>

Dealing with **Json response* using `SResponse` class
> if response is plain/text or HTML (when requesting websites) simply use `response.toString()` method.
```java
    // get/check response as json (if get a api json response)
    boolean isJSON = response.isJSON();
    // check if response json is Object
    boolean isMap = response.isMap();
    // else if it's Array
    boolean isArray = response.isArray();
    
    
    // getting response if it is plain text or json in general
    String text = response.toString();
    
    // get response as object
    SResponse.Map object = response.getMap();
    // now you can access all values in that object using
    // return Object
    Object o = object.get("key");
    // return int
    int i = object.getInt("key");
    // return String
    String s = object.getString("key");
    // return float
    float f = object.getFloat("key");
    // return boolean
    boolean b = object.getBoolean("key");
    // return Map object as above ↑
    // if map object nested inside another map object
    SResponse.Map = object.getMap("key");
    // get keys
    Set<String> keys = object.keys();
    // get values
    List<Object> values = object.values();
    // has key?
    boolean hasKey = object.hasKey("key");
    // has value? (accepts anything)
    boolean hasValue = object.hasValue(Object);
    // size
    int size = object.size();
    // is empty?
    boolean isEmpty = object.isEmpty();
    // to json string
    String json = object.toString();
    // to HashMap
    HashMap<String, Object> map = object.toMap();
    
    
    // the same but for arrays
    // get response as array from response or from map object
    // this if the response body is array
    SResponse.Array array = response.getArray();
    // and this if response body is object ↑ has an array as value inside it
    SResponse.Array array = object.getArray();
    // array class has same methods like map class
    // get at index
    Object o = array.get(0);
    // get string 
    String s = array.getString(0);
    // get int
    int i = array.getInt(0);
    // get float
    float f = array.getFloat(0);
    // get boolean
    boolean b = array.getBoolean(0);
    // get Map object like above if map object nested inside list
    SResponse.Map m = array.getMap(0);
    // same if it has array inside array
    SResponse.Array a = array.getArray();
    // contains something?
    boolean contains = array.contains(Object);
    // size
    int size = array.size();
    // is empty
    boolean isEmpty = array.isEmpty();
    // to json string
    String json = array.toString();
    // to List
    List<Object> list = array.toList();
    
```

<br/>

# Donations
> If you would like to support this project's further development, the creator of this projects or the continuous maintenance of the project **feel free to donate**.
Your donation is highly appreciated. Thank you!
<br/>

You can **choose what you want to donate**, all donations are awesome!</br>
<br/>

[![PayPal](https://img.shields.io/badge/PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://www.paypal.me/husseinshakir)
[![Buy me a coffee](https://img.shields.io/badge/Buy_Me_A_Coffee-FFDD00?style=for-the-badge&logo=buy-me-a-coffee&logoColor=black)](https://www.buymeacoffee.com/HusseinShakir)
[![Ko-fi](https://img.shields.io/badge/Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/husseinsmith)
<br/>

<p align="center">
  <img src="https://raw.githubusercontent.com/smith8h/smith8h/main/20221103_150053.png" style="width: 38%;"/>
  <br><b>With :heart:</b>
</p>
