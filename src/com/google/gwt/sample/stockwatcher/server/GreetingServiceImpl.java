package com.google.gwt.sample.stockwatcher.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.sample.stockwatcher.shared.GameImage;
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
	public void setUserNickname(String nickName) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if (user!=null)
        {
        	PersistenceManager pm = PMF.get().getPersistenceManager();
        	try{
        		Player current = pm.getObjectById(Player.class, user.getUserId());
        		current.setUsername(nickName);
        	}
        	finally{
        		pm.close();
        	}
        }
	}

	@SuppressWarnings("unchecked")
	@Override
	public Player getPlayer(String backUrl) {
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
		if (user==null)
			return null;

		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean userExists = false;
		List<Player> result = null;
		
		//Checking whether it is a new user, if not - creating one.
		//Player currentPlayer = pm.getObjectById(Player.class, user.getUserId());
		Query myQ = pm.newQuery(Player.class, "id == '" + user.getUserId() + "'");
		try{
			result = (List<Player>) myQ.execute();
			if (!(result.isEmpty()))
				userExists = true;
		}
		finally{
			myQ.closeAll();
		}
		
		Player toStore = userExists ? result.get(0) : null;
		
		if (!userExists)
		{
			toStore = new Player();
			toStore.setId(user.getUserId());
			toStore.setUsername(user.getNickname());
			
			try {
				pm.makePersistent(toStore);
			} finally{
				pm.close();
			}
		}
		
		//Removing the ID due to security reasons
		Player toSend = new Player();
		toSend.id = "";
		toSend.setUsername(toStore.getUsername());
		toSend.score = toStore.score;
		toSend.logoutURL = userService.createLoginURL(backUrl);
		
		return toSend;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Player> getPlayerRankings() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query myQ = pm.newQuery(Player.class);
		myQ.setFilter("score > 0");
		myQ.setOrdering("score desc");
		
		List<Player> qResult = null;
		try{
			qResult = (List<Player>) myQ.execute();
		}
		finally{
			myQ.closeAll();
		}
		
		if (qResult != null)
		{
			List<Player> trimmedList = new LinkedList<Player>();
			for (Player current : qResult)
			{
				Player newPlayer = new Player();
				newPlayer.setUsername(current.getUsername());
				newPlayer.score = current.score;
				trimmedList.add(newPlayer);
			}
			qResult = trimmedList;
		}
		return qResult;
	}
	
	@SuppressWarnings("unchecked")
	public List<GameImage> getImageInfos(){
		List<GameImage> toReturn = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query myQ = pm.newQuery(GameImage.class);
		myQ.setOrdering("time desc");
		try{
			toReturn = (List<GameImage>) myQ.execute();
		}
		finally{
			myQ.closeAll();
		}
		//TODO: Trim the right answers!
		//Due to the problems with serialization, need to create another List 
		toReturn = new LinkedList<GameImage>(toReturn);
		return toReturn;
	}
}
