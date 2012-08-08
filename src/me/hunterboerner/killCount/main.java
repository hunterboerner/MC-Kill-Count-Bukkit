package me.hunterboerner.killCount;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class main extends org.bukkit.plugin.java.JavaPlugin implements Listener {
	Map<String, Integer> kills = new HashMap<String, Integer>();
	Map<String, Integer> deaths = new HashMap<String, Integer>();
	
	
	public void onEnable(){
		getLogger().info("KillCount " + this.getDescription().getVersion() + " has been enabled.");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "KillCount");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "DeathCount");
	}
	
	public void onDisable(){
		getLogger().info("Kill Count " + this.getDescription().getVersion() + " has been enabled");
	}
	

	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent playerDeath){
		String dead = playerDeath.getEntity().getName();
		int deathCount = 0;
		if(deaths.get(dead) != null){
			deathCount = deaths.get(dead);
		}
		deathCount++;
		deaths.put(dead, deathCount);
		
		
		if (playerDeath.getEntity().getListeningPluginChannels().contains("KillCount")){
			 String deathString = deathCount + "";
			 playerDeath.getEntity().sendPluginMessage(this, "DeathCount", deathString.getBytes(java.nio.charset.Charset.forName("UTF-8")));
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent death){
				
		Player killer = death.getEntity().getKiller();
		if(killer != null && killer.getGameMode() != GameMode.CREATIVE) {
			int killCount = 0;
			if(kills.get(killer.getName()) != null) {
				killCount = kills.get(killer.getName());
			}
			killCount++;
			kills.put(killer.getName(), killCount);
			
			if (killer.getListeningPluginChannels().contains("KillCount"))	{
				String killString = killCount + "";
				killer.sendPluginMessage(this, "KillCount", killString.getBytes(java.nio.charset.Charset.forName("UTF-8")));
			}
		}
		
	}
	
	
	
}
