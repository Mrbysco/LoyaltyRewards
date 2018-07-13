package com.Mrbysco.LoyaltyRewards.utils;

public class TimeHelper {
	public static int getProperTime(int time, String amount) {
		int actualTime = 0;
		int timeTick = time * 20;
		if (amount.isEmpty())
		{
			return 0;
		}
		else if (amount.contains("sec"))
		{
			actualTime = timeTick;
			return actualTime;
		}
		else if(amount.contains("min"))
		{
			actualTime = timeTick * 60;
			return actualTime;
		}
		else if (amount.contains("hour"))
		{
			actualTime = timeTick * 3600;
			return actualTime;
		}
		
		return actualTime;
	}
}
