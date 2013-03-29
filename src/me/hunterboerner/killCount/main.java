package me.hunterboerner.killCount;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import yt.codebukkit.scoreboardapi.Scoreboard;
import yt.codebukkit.scoreboardapi.ScoreboardAPI;

public class main extends org.bukkit.plugin.java.JavaPlugin implements Listener {
	ScoreboardAPI api = ScoreboardAPI.getInstance();
	Scoreboard killCountBoard = api.createScoreboard("kc", 2);

	Map<String, Integer> kills = new HashMap<String, Integer>();
	Map<String, Integer> deaths = new HashMap<String, Integer>();

	public void onEnable() {
		killCountBoard.setType(Scoreboard.Type.SIDEBAR);
		killCountBoard.setScoreboardName("KillCounter");
		killCountBoard.setScoreboardName("Scores");

		getDataFolder().mkdir();
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

	int deathCount = 0;

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent playerDeath) {
		String dead = playerDeath.getEntity().getName();

		if (deaths.get(dead) != null) {
			deathCount = deaths.get(dead);
		}
		deathCount++;
		deaths.put(dead, deathCount);

		playerDeath.getEntityType();
		killCountBoard.setItem(dead, killCount / deathCount);
		playerDeath.getEntityType();
		if (playerDeath.getEntity().getKiller() instanceof Player) {
			killCountBoard.setItem(playerDeath.getEntity().getKiller()
					.getName(), killCount / deathCount);
		}
		killCountBoard.showToPlayer(playerDeath.getEntity(), true);

		try {
			SLAPI.save(deaths, this.getDataFolder() + "/deaths.bin");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	int killCount = 0;

	@EventHandler
	public void onDeath(EntityDeathEvent death) {
		Player killer = death.getEntity().getKiller();
		if (killer != null && killer.getGameMode() != GameMode.CREATIVE
				&& killer instanceof Player) {

			if (kills.get(killer.getName()) != null) {
				killCount = kills.get(killer.getName());
			}
			killCount++;
			kills.put(killer.getName(), killCount);

			death.getEntityType();
			killCountBoard.setItem(killer.getName(), killCount / deathCount);

			death.getEntityType();
			death.getEntityType();
			killCountBoard.setItem(EntityType.PLAYER.getName(), killCount
					/ deathCount);

			killCountBoard.showToPlayer(killer, true);
			try {
				SLAPI.save(kills, main.this.getDataFolder() + "/kills.bin");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent join) {
		if (kills.get(join.getPlayer().getName()) != null) {
			killCount = kills.get(join.getPlayer().getName());
		}
		if (deaths.get(join.getPlayer().getName()) != null) {
			deathCount = deaths.get(join.getPlayer().getName());
		}

		killCountBoard.setItem(join.getPlayer().getName(), killCount
				/ deathCount);
		killCountBoard.showToPlayer(join.getPlayer(), true);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("kdclear")) {
			if (!(sender instanceof Player)) {
				getLogger().info("Only a player may use the kdclear command.");
			} else {
				Player player = (Player) sender;
				killCountBoard.showToPlayer(player, true);
				int killCount = 0;
				if (kills.get(player.getName()) != null) {
					kills.put(player.getName(), killCount);
				}

				int deathCount = 0;
				if (deaths.get(player.getName()) != null) {
					deaths.put(player.getName(), deathCount);
				}

				killCountBoard.setItem(player.getName(), 0);
				try {
					SLAPI.save(deaths, this.getDataFolder() + "/deaths.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					SLAPI.save(kills, main.this.getDataFolder() + "/kills.bin");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		return true;
	}

}
