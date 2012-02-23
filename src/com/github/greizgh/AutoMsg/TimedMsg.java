package com.github.greizgh.AutoMsg;

import org.bukkit.plugin.java.JavaPlugin;

public class TimedMsg extends Thread {
	private String message;
	private JavaPlugin plg;
	public TimedMsg(JavaPlugin plug, String str)
	{
		message=str;
		plg=plug;
	}
	public void run()
	{
		if (plg.getServer().getOnlinePlayers().length!=0)
		{
			plg.getServer().broadcastMessage(message);
		}
	}

}
