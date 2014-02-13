package com.google.gwt.sample.stockwatcher.client;

import java.util.List;

import com.google.gwt.sample.stockwatcher.shared.GameImage;
import com.google.gwt.sample.stockwatcher.shared.Player;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void getRandom(AsyncCallback<String> callback)
			throws IllegalArgumentException;
	
	void storeWord(String word, String pictureInfo, AsyncCallback<String> asyncCallback);
	
	void getUserLoginUrl(String backUrl, AsyncCallback<String> newUrl);
	
	void getUserNickname(AsyncCallback<String> nickname);
	
	void setUserNickname(String newNickname, AsyncCallback<Void> callback);
	
	void getPlayer(String backUrl, AsyncCallback<Player> user);

	void getPlayerRankings(AsyncCallback<List<Player>> callback);
	
	void getImageInfos(AsyncCallback<List<GameImage>> callback);

}
