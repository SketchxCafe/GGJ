package com.google.gwt.sample.stockwatcher.server;

import javax.jdo.PersistenceManager;

import com.google.gwt.sample.stockwatcher.shared.GameImage;
import com.google.gwt.sample.stockwatcher.shared.MyStringUtil;
import com.google.gwt.sample.stockwatcher.shared.Player;

public class GameOperations {
	//public static PersistenceManagerFactory pmf =  JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public static void incrementUserScore(String userID, int howMuch)
	{
		if (howMuch == 0)
			return;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		try{
			Player current = pm.getObjectById(Player.class, userID);
			current.setScore(current.score + howMuch);
		}
		finally {
			pm.close();
		}
	}
	

	public static String addGuess(String gameImageKey, String playerID, String guess){
		//Checking whether the answer is right
		//imageDAO myImageDAO = new imageDAO();
		//ImageBlob temp = myImageDAO.get(gameImageKey);
		
		//if (temp == null)
		//	return "Blob not found. Notify admin.";
		
		boolean guessResult = false;// = MyStringUtil.checkForRightString(temp.getWord(), guess);
		
		//Updating the image information.
		int count = -1;
		//Collection<String> tempo = null;
		String [] tempo = null;
		boolean [] tempu = null;
		String host = null;
		String correctAnswer = "";
		
		boolean objectChanged = false;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			//GameImage currentImage = pm.getObjectById(GameImage.class, gameImageKey);
			
			GameImage result;
			
			//String request = "key == '" + gameImageKey + "'";
				//"key.equals('"+ gameImageKey +"')";
			//Query myQ = pm.newQuery(GameImage.class, request);
			//Query myQ = pm.newQuery(GameImage.class);
//			myQ.setFilter("key == param");
//			myQ.declareParameters("String param");
//			try{
//				result = (List<GameImage>) myQ.execute(gameImageKey);
//				nElements = result.size();
//			}
//			finally{
//				myQ.closeAll();
//			}
			
			result = pm.getObjectById(GameImage.class, Long.decode(gameImageKey));
			
			//TODO: allows multiple guesses!
			if (!(result==null))
			{
				GameImage currentImage = result;
				count = currentImage.getGuessed();
				correctAnswer = currentImage.getWord();
				guessResult = MyStringUtil.checkForRightString(correctAnswer, guess);
				if (count < 5)
				{
					count++;
					
					pm.currentTransaction().begin();
					currentImage.setGuessed(count);
					currentImage.setNextGuesser(playerID);
					tempu = currentImage.getGuessedRight();
					
					tempu[count-1] = guessResult;
					currentImage.setGuessedRight(tempu);				
					tempo = currentImage.getGuessers();
					//TODO: potential optimization
					host = currentImage.getPosterID();
					pm.currentTransaction().commit();
					
					objectChanged = true;
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
		
		if (!objectChanged && (tempo==null || count >=5))
			//return "No more votes required.";
			return "No more votes required.\n" + ((guessResult) ? 
					"Right! It is "+ correctAnswer + "." 
					: "Wrong!");
			
		//Check for giving out the points
		if (objectChanged && count == 5 && tempo!=null && tempu!=null)
		{
			int rightCount = 0;
			for (int i = 0; i<5; i++)
				if (tempu[i] == true)
					rightCount++;
			
			if (rightCount == 0 || rightCount == 5)
			{
				//Nobody got it right or everybody got it right - give 0 to host
				
				//Give 1 to each player
				for (int i = 0; i<5; i++)
				{
					incrementUserScore(tempo[i], 1);
				}
			}
			else 
			{
				//Give some to the host
				int [] score = {0, 2, 3, 2, 1, 0};
				
				incrementUserScore(host, score[rightCount]);
				
				for (int i = 0; i<5; i++)
				{
					//Give some to right-guessers
					if (tempu[i] == true)
						incrementUserScore(tempo[i], 1);
				}
			}
			return "It was the last guess: points assigned!";
		}
		return "The guess #" + count + " added.";
	}
}
