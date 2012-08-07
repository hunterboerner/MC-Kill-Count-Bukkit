package me.hunterboerner.killCount;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class main extends org.bukkit.plugin.java.JavaPlugin implements Listener {
	Map<String, Integer> players = new HashMap<String, Integer>();
	Map<String, Integer> hostile = new HashMap<String, Integer>();
	Map<String, Integer> neutral = new HashMap<String, Integer>();
	Map<String, Integer> peaceful = new HashMap<String, Integer>();
	
	public void onEnable(){
		getLogger().info("KillCount " + this.getDescription().getVersion() + " has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "KillCount");
	}
	
	public void onDisable(){
		getLogger().info("Kill Count " + this.getDescription().getVersion() + " has been enabled");
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent death){
				
		Player killer = death.getEntity().getKiller();
		if(killer != null && killer.getGameMode() != GameMode.CREATIVE) {
			int killCount = 0;
			if(players.get(killer.getName()) != null) {
				killCount = players.get(killer.getName());
			}
			killCount++;
			players.put(killer.getName(), killCount);
			
			String killString = killCount + "";
			
			if (killer.getListeningPluginChannels().contains("KillCount"))	{
				killer.sendPluginMessage(this, "KillCount", killString.getBytes(java.nio.charset.Charset.forName("UTF-8")));
			}
		}
		
	}
	
}
