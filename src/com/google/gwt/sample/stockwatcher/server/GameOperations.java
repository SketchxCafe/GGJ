package com.google.gwt.sample.stockwatcher.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.sample.stockwatcher.client.ImageBlob;
import com.google.gwt.sample.stockwatcher.shared.GameImage;
import com.google.gwt.sample.stockwatcher.shared.MyStringUtil;
import com.google.gwt.sample.stockwatcher.shared.Player;

public class GameOperations {
	//public static PersistenceManagerFactory pmf =  JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public static void incrementUserScore(String userID, int howMuch)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			Player current = pm.getObjectById(Player.class, userID);
			current.setScore(current.score + howMuch);
		}
		finally {
			pm.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String addGuess(String gameImageKey, String playerID, String guess){
		//Checking whether the answer is right
		imageDAO myImageDAO = new imageDAO();
		ImageBlob temp = myImageDAO.get(gameImageKey);
		
		if (temp == null)
			return "Blob not found. Notify admin.";
		
		boolean guessResult = MyStringUtil.checkForRightString(temp.getWord(), guess);
		
		//Updating the image information.
		int count = -1;
		//Collection<String> tempo = null;
		String [] tempo = null;
		boolean [] tempu = null;
		String host = null;
		int nElements = 0;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			//GameImage currentImage = pm.getObjectById(GameImage.class, gameImageKey);
			
			List<GameImage> result;
			
			//String request = "key == '" + gameImageKey + "'";
				//"key.equals('"+ gameImageKey +"')";
			//Query myQ = pm.newQuery(GameImage.class, request);
			Query myQ = pm.newQuery(GameImage.class);
			myQ.setFilter("key == param");
			myQ.declareParameters("String param");
			try{
				result = (List<GameImage>) myQ.execute(gameImageKey);
				nElements = result.size();
			}
			finally{
				myQ.closeAll();
			}
			
			//TODO: allows multiple guesses!
			if (!result.isEmpty())
			{
				GameImage currentImage = result.get(0);
				count = currentImage.getGuessed();
				if (count < 5)
				{
					count++;
					
					pm.currentTransaction().begin();
					currentImage.setGuessed(count);
					tempo = currentImage.getGuessers();
					currentImage.setNextGuesser(playerID);
					tempu = currentImage.getGuessedRight();
					tempu[count-1] = guessResult;
					currentImage.setGuessedRight(tempu);
					//TODO: potential optimization
					host = currentImage.getPosterID();
					
					pm.currentTransaction().commit();
				}
			}
			else
				count = -1;
		}
		finally {
			pm.close();
		}
		
		if (count == -1)
			return "Element not found.";
		
		//This check is turned off for debug purposes.
//		if (host.equals(playerID))
//			return "Cannot guess on your art.";
		
		if (tempo==null || count >=5)
			//return "No more votes required.";
			return "No more votes required; " + nElements + " found.";
			
		//Check for giving out the points
		if (count == 5 && tempo!=null && tempu!=null)
		{
			int rightCount = 0;
			for (int i = 0; i<5; i++)
				if (tempu[i] == true)
					rightCount++;
			
			if (rightCount == 0 || rightCount == 5)
			{
				//Nobody got it right or everybody got it right
				//Give 0 to the host
				//incrementUserScore(host, 0);
				
				//Give 1 to each player
				for (int i = 0; i<5; i++)
				{
					incrementUserScore(tempo[i], 1);
				}
			}
			else 
			{
				//Give some to the host
				incrementUserScore(host, 3 - Math.abs(rightCount - 3));
				
				for (int i = 0; i<5; i++)
				{
					//Give some to right-guessers
					if (tempu[i] == true)
						incrementUserScore(tempo[i], 1);
				}
			}
			return "The last user: point assigned.";
		}
		return "The " + count + " guess added.";
	}
}
