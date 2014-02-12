package com.google.gwt.sample.stockwatcher.server;

import java.util.Collection;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.sample.stockwatcher.client.PlayerInfo;
import com.google.gwt.sample.stockwatcher.shared.Player;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	//private PersistenceManagerFactory pmf;// = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public static String currWord = "ERROR!";
	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
//		if (!FieldVerifier.isValidName(input)) {
//			// If the input is not valid, throw an IllegalArgumentException back to
//			// the client.
//			throw new IllegalArgumentException(
//					"Name must be at least 4 characters long");
//		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public String getRandom() throws IllegalArgumentException {
		//currWord = MyStringUtil.getRandomString();
		currWord = wordList[(int) (Math.random()*wordList.length)];
		return currWord;
	}

	
	
	@Override
	public String storeWord(String word, String pictureInfo) {		
		// TODO Auto-generated method stub
//		return "recived " + pictureInfo;
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user == null)
        	return "Please, log in.";
		return GameOperations.addGuess(pictureInfo, user.getUserId(), word);
	}
	
	String[] wordList = {"Rainbow"};

	@Override
	public String getUserLoginUrl(String backUrl) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if (user==null)
        {
        	return userService.createLoginURL(backUrl);
        }
		return null;
	}

	@Override
	public String getUserNickname() {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if (user!=null)
        {
        	return user.getNickname();
        }
		return null;
	}

	@Override
	public PlayerInfo getPlayerInfo(String backUrl) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
		if (user==null)
			return null;

		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean userExists = false;
		
		//Checking whether it is a new user, if not - creating one.
		//Player currentPlayer = pm.getObjectById(Player.class, user.getUserId());
		Query myQ = pm.newQuery(Player.class, "id == " + user.getUserId());
		try{
			@SuppressWarnings("unchecked")
			Collection<Player> result = (Collection<Player>) myQ.execute();
			if (!result.isEmpty())
				userExists = true;
		}
		finally{
			myQ.closeAll();
		}
		
		if (!userExists)
		{
			Player toStore = new Player();
			toStore.setId(user.getUserId());
			toStore.setUsername(user.getNickname());
			
			try {
				pm.makePersistent(toStore);
			} finally{
				pm.close();
			}
		}
		
		PlayerInfo toReturn = new PlayerInfo();
		toReturn.nickName = user.getNickname();
		toReturn.logoutURL = userService.createLogoutURL(backUrl);
		
		return toReturn;
	}
}
