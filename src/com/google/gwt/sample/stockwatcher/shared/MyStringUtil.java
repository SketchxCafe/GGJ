package com.google.gwt.sample.stockwatcher.shared;

public class MyStringUtil {

	private static String[] stringList = {"Horse", "Unicorn", "Pokemon", "Crayon", "Dog", "Blah", "Nothingness", "Void", "Depression", "City"};
	
	public static String getRandomString()
	{
		int random = (int) (Math.random()*stringList.length);
		return stringList[random];
	}
	
	public static boolean checkForRightString(String target, String[] guesses)
	{
		boolean ans = false;
		for (String current : guesses)
		{
			if (current.toLowerCase().contains(target.toLowerCase()))
				ans = true;
		}
		return ans;
	}
	
	public static boolean checkForRightString(String target, String guesses)
	{
		return guesses.toLowerCase().contains(target.toLowerCase());
	}
}
