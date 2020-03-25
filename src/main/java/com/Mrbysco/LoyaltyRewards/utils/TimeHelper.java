package com.mrbysco.loyaltyrewards.utils;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

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

	public static ITextComponent secondsToTextComponent(int totalSeconds) {
		//Calculate the seconds to display:
		int seconds = totalSeconds % 60;
		totalSeconds -= seconds;
		//Calculate the minutes:
		long minutesCount = totalSeconds / 60;
		long minutes = minutesCount % 60;
		minutesCount -= minutes;
		//Calculate the hours:
		long hours = minutesCount / 60;
		ITextComponent hourComponent = new TextComponentString(String.format("\n %02d", hours));
		hourComponent.getStyle().setColor(TextFormatting.YELLOW);
		ITextComponent hourExtra = new TextComponentString("H");
		hourExtra.getStyle().setColor(TextFormatting.GOLD);
		ITextComponent minuteComponent = new TextComponentString(String.format(":%02d", minutes));
		minuteComponent.getStyle().setColor(TextFormatting.YELLOW);
		ITextComponent minuteExtra = new TextComponentString("M");
		minuteExtra.getStyle().setColor(TextFormatting.GOLD);
		ITextComponent secondComponent = new TextComponentString(String.format(":%02d", seconds));
		secondComponent.getStyle().setColor(TextFormatting.YELLOW);
		ITextComponent secondExtra = new TextComponentString("S");
		secondExtra.getStyle().setColor(TextFormatting.GOLD);
		return hourComponent.appendSibling(hourExtra).appendSibling(minuteComponent).appendSibling(minuteExtra).appendSibling(secondComponent).appendSibling(secondExtra);
	}

	public static String secondsToString(int totalSeconds) {
		//Calculate the seconds to display:
		int seconds = totalSeconds % 60;
		totalSeconds -= seconds;
		//Calculate the minutes:
		long minutesCount = totalSeconds / 60;
		long minutes = minutesCount % 60;
		minutesCount -= minutes;
		//Calculate the hours:
		long hours = minutesCount / 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
