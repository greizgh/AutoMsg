package com.github.greizgh.AutoMsg;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class AutoMsg extends JavaPlugin {
	private static Logger logger = Logger.getLogger("Minecraft");
	PluginDescriptionFile plugdesc;
	private FileConfiguration config;
	public static void log(Level level, String message) {
		logger.log(level, "[AutoMsg] " + message);
	}
	@Override
	public void onDisable() {
		log(Level.INFO, plugdesc.getName() + " disabled.");
	}

	@Override
	public void onEnable() {
		plugdesc = this.getDescription();
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		Long period= config.getLong("general.period");
		log(Level.INFO, plugdesc.getName() + " v"+plugdesc.getVersion()+" enabled.");
		if (config.getBoolean("general.random"))
		{
			this.getServer().getScheduler().runTaskTimerAsynchronously(this, randomMessage, period, period);
		}
		if (config.getBoolean("general.timed"))
		{
			List<String> messages = config.getStringList("messages.timed");
			for (int i=0;i<messages.size();i++)
			{
				String time = new String(messages.get(i).split(";", 2)[0]);
				String text = new String(messages.get(i).split(";", 2)[1].trim());
				Calendar now = Calendar.getInstance();
				Calendar schedule = Calendar.getInstance();
				try {
					schedule.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
					schedule.set(Calendar.MINUTE, Integer.parseInt(time.substring(2, 4)));
					schedule.set(Calendar.SECOND, 0);
				} catch (Exception e) {
					e.printStackTrace();
					log(Level.SEVERE, "Error parsing time in config file.");
				}
				Long delay = 0L;
				if (schedule.after(now))
				{
					delay = schedule.getTimeInMillis() - now.getTimeInMillis();
				}
				else
				{
					schedule.add(Calendar.DATE, 1);
					delay = schedule.getTimeInMillis() - now.getTimeInMillis();
				}
				delay = delay*20/1000;//convert delay in ticks
				TimedMsg msg = new TimedMsg(this, text);
				this.getServer().getScheduler().runTaskTimerAsynchronously(this, msg, delay, 1728000L);
				//Task reoccurs every 24H -> 20ticks * 60 * 60 * 24 = 1728000
			}
		}
	}
	Runnable randomMessage = new Runnable() {
		public void run() {
			List<String> messages = getConfig().getStringList("messages.random");
			Iterator<String> iter = messages.iterator();
			ArrayList<String> txt= new ArrayList<String>();
			while (iter.hasNext()) {
				txt.add(iter.next());
			}
			if (getServer().getOnlinePlayers().length!=0)
			{
				String show = txt.get((int)(Math.random()*(txt.size())));
				getServer().broadcastMessage(show);
			}
		}
	};
}