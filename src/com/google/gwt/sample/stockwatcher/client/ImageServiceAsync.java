package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ImageServiceAsync {

	public void getBlobstoreUploadUrl(AsyncCallback<String> callback);

	void get(String key, AsyncCallback<ImageBlob> callback);
	
	
	void deleteImage(String key, AsyncCallback<Void> callback);

	public void getRecentlyUploaded(AsyncCallback<java.util.LinkedList<ImageBlob>> callback);
}
