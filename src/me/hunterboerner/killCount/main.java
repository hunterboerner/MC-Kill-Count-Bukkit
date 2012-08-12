package me.hunterboerner.killCount;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class main extends org.bukkit.plugin.java.JavaPlugin implements Listener {
	Map<String, Integer> kills = new HashMap<String, Integer>();
	Map<String, Integer> deaths = new HashMap<String, Integer>();

	public void onEnable() {
		getLogger().info(
				"KillCount " + this.getDescription().getVersion()
						+ " has been enabled.");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getMessenger().registerOutgoingPluginChannel(this,
				"KillCount");
		getServer().getMessenger().registerOutgoingPluginChannel(this,
				"DeathCount");
		try {
			kills = SLAPI.load(this.getDataFolder() + "/kills.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			deaths = SLAPI.load(this.getDataFolder() + "/deaths.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDisable() {
		getLogger().info(
				"Kill Count " + this.getDescription().getVersion()
						+ " has been disabled");
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent playerDeath) {
		String dead = playerDeath.getEntity().getName();
		int deathCount = 0;
		if (deaths.get(dead) != null) {
			deathCount = deaths.get(dead);
		}
		deathCount++;
		deaths.put(dead, deathCount);

		if (playerDeath.getEntity().getListeningPluginChannels()
				.contains("KillCount")) {
			String deathString = deathCount + "";
			playerDeath.getEntity().sendPluginMessage(
					this,
					"DeathCount",
					deathString.getBytes(java.nio.charset.Charset
							.forName("UTF-8")));
			try {
				SLAPI.save(deaths, this.getDataFolder() + "/deaths.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@EventHandler
	public void onDeath(EntityDeathEvent death) {
		Player killer = death.getEntity().getKiller();
		if (killer != null && killer.getGameMode() != GameMode.CREATIVE) {
			int killCount = 0;
			if (kills.get(killer.getName()) != null) {
				killCount = kills.get(killer.getName());
			}
			killCount++;
			kills.put(killer.getName(), killCount);

			if (killer.getListeningPluginChannels().contains("KillCount")) {
				String killString = killCount + "";
				killer.sendPluginMessage(this, "KillCount", killString
						.getBytes(java.nio.charset.Charset.forName("UTF-8")));
				try {
					SLAPI.save(kills, this.getDataFolder() + "/kills.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent join) {
		final PlayerJoinEvent join2 = join;
		getServer().getScheduler().scheduleSyncDelayedTask(this,
				new Runnable() {

			
					public void run() {
						if (join2.getPlayer().getListeningPluginChannels()
								.contains("KillCount")) {
							int killCount = 0;
							if (kills.get(join2.getPlayer().getName()) != null) {
								killCount = kills.get(join2.getPlayer()
										.getName());
							}
							String killString = killCount + "";
							join2.getPlayer().sendPluginMessage(
									main.this,
									"KillCount",
									killString
											.getBytes(java.nio.charset.Charset
													.forName("UTF-8")));
							try {
								SLAPI.save(kills, main.this.getDataFolder()
										+ "/kills.bin");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						if (join2.getPlayer().getListeningPluginChannels()
								.contains("DeathCount")) {
							int deathCount = 0;
							if (deaths.get(join2.getPlayer().getName()) != null) {
								deathCount = deaths.get(join2.getPlayer()
										.getName());
							}
							String deathString = deathCount + "";
							join2.getPlayer().sendPluginMessage(
									main.this,
									"DeathCount",
									deathString
											.getBytes(java.nio.charset.Charset
													.forName("UTF-8")));
							try {
								SLAPI.save(deaths, main.this.getDataFolder()
										+ "/deaths.bin");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}, 1L);

	}

}
