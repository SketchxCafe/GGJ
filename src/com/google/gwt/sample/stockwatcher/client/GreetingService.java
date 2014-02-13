package com.google.gwt.sample.stockwatcher.client;

import java.util.List;

import com.google.gwt.sample.stockwatcher.shared.GameImage;
import com.google.gwt.sample.stockwatcher.shared.Player;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	
	String getRandom()
			throws IllegalArgumentException;
	
	String storeWord(String word, String pictureInfo);

	String getUserLoginUrl(String backUrl);

	String getUserNickname();
	
	void setUserNickname(String newNickname);

	Player getPlayer(String backUrl);
	
	List<Player> getPlayerRankings();
	
	List<GameImage> getImageInfos();
}
