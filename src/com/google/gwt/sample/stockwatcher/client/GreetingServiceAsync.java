package com.google.gwt.sample.stockwatcher.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getRandom(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void storeWord(String word, String pictureInfo, AsyncCallback<Void> callback);
}
