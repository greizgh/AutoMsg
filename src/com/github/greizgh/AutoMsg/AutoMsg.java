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
		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, randomMessage, 1200L, period);
	}
	Runnable randomMessage = new Runnable() {
		public void run() {
			List<String> messages = getConfig().getStringList("messages");
			Iterator<String> iter = messages.iterator();
			ArrayList<String> txt= new ArrayList<String>();
			while (iter.hasNext()) {
				txt.add(iter.next());
			}
			String show = txt.get((int)(Math.random()*(txt.size())));
			getServer().broadcastMessage(show);
		}
	};
}