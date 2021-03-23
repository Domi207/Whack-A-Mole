package de.domi207.wam.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import de.domi207.wam.commands.CommandWrack_A_Mule;
import de.domi207.wam.commands.TabCompleterWrackAMule;

public class Main extends JavaPlugin {

	public static String prefix = "";
	File configFile;
	File leaderboardFile;
	File messageFile;
	FileConfiguration config;
	FileConfiguration messages;
	FileConfiguration leaderboard;
	static Main instance;
	Integer wamTaskId;
	Player player;
	MuleRunnable muleRunnable;
	GameMode lastGamemode;
	HashMap<Integer, ItemStack> lastItems = new HashMap<>();

	@Override
	public void onEnable() {
		instance = this;

		Bukkit.getPluginManager().registerEvents(new GUIManger(), this);

		leaderboardFile = new File(getDataFolder(), "leaderboard.yml");
		configFile = new File(getDataFolder(), "config.yml");
		messageFile = new File(getDataFolder(), "messages.yml");
		config = new YamlConfiguration();
		leaderboard = new YamlConfiguration();
		messages = new YamlConfiguration();

		if (leaderboardFile.exists()) {
			try {
				leaderboard.load(leaderboardFile);
			} catch (IOException | InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			getDataFolder().mkdirs();
			try {
				leaderboardFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// CONFIG
		// CONFIG
		// CONFIG
		if (configFile.exists()) {
			try {
				config.load(configFile);
			} catch (IOException | InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			getDataFolder().mkdirs();
			try {
				configFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			config.set("headValue",
					"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI1MTUwMTU5MmQyZWFiMjZkMDcyNjA2MzQ2YjBkNGJkNjM1ZDM2YjM1NTUwYzFmNjlhNzdmZTNlNTZkM2QxNSJ9fX0=");
		}

		// MESSAGES
		// MESSAGES
		// MESSAGES

		if (messageFile.exists()) {
			try {
				messages.load(messageFile);
			} catch (IOException | InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			getDataFolder().mkdirs();
			try {
				messageFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			messages.set("prefix", "§7[§aWhack§7-§bA§7-§cMole§7]§6: ");

			messages.set("highscoreinventory.title", "§aWhack§7-§bA§7-§cMole");
			messages.set("highscoreinventory.template", "§a%s§7: §b%s§8");
			messages.set("highscoreinventory.loretemplate", "§8%s");

			messages.set("inventory.title", "§aWhack§7-§bA§7-§cMole");
			messages.set("inventory.item.start.name", "§r§a§lStarte ein neues Spiel");
			messages.set("inventory.item.cantStart.name", "§r§c§lAktuell spielt bereits ein Spieler");
			messages.set("inventory.item.stop.name", "§r§c§lStoppe das aktuelle Spiel");
			messages.set("inventory.item.highscores.name", "§6§lHighscores");

			messages.set("leaderboard.invalidvalue", "§cDieser Wert ist ungültig");
			messages.set("leaderboard.maxvalue", "§cDer Wert wurde auf 100 reduziert");
			messages.set("leaderboard.header", "§bDie Top §a%s§b von Whack-A-Mole:\n ");
			messages.set("leaderboard.template", "§6%s. §a%s§7: §b%s§8 - §7%s");
			messages.set("leaderboard.back", "§cZurück");

			messages.set("settings.center.sucess", "§aDer Mittelpunkt wurde erfolgreich gesetzt");

			messages.set("settings.radius.sucess", "§aDer Radius wurde erfolgreich gesetzt");
			messages.set("settings.radius.invalidValue", "§cDieser Wert ist ungültig");
			messages.set("settings.radius.syntax", "§c/whackamule settings radius <value>");

			messages.set("settings.pointsPerMule.sucess", "§aDer Punkte für jeden Schlag wurde erfolgreich gesetzt");
			messages.set("settings.pointsPerMule.invalidValue", "§cDieser Wert ist ungültig");
			messages.set("settings.pointsPerMule.syntax", "§c/whackamule settings pointsPerMule <value>");

			messages.set("settings.lossPerSecond.sucess", "§aDie Verluste wurden erfolgreich gesetzt");
			messages.set("settings.lossPerSecond.invalidValue", "§cDieser Wert ist ungültig");
			messages.set("settings.lossPerSecond.syntax", "§c/whackamule settings lossPerSecond <value>");

			messages.set("settings.time.sucess", "§aDie Spielzeit wurden erfolgreich gesetzt");
			messages.set("settings.time.invalidValue", "§cDieser Wert ist ungültig");
			messages.set("settings.time.syntax", "§c/whackamule settings time <value>");

			messages.set("settings.spawnPercent.sucess", "§aDie Spawn Prozent wurden erfolgreich gesetzt");
			messages.set("settings.spawnPercent.invalidValue", "§cDieser Wert ist ungültig");
			messages.set("settings.spawnPercent.syntax", "§c/whackamule settings spawnPercent <value>");

			messages.set("settings.minusPoints.sucess", "§aDie Minus Punkte wurden erfolgreich gesetzt");
			messages.set("settings.minusPoints.invalidValue", "§cDieser Wert ist ungültig");
			messages.set("settings.minusPoints.syntax", "§c/whackamule settings spawnPercent <value>");

			messages.set("settings.syntax",
					"§c/whackamule settings <center;radius;pointsPerMule;minusPoints;lossPerSecond;time> [value]");
			messages.set("settings.noPermissions", "§cDu darfst diesen Command nicht ausführen");

			messages.set("start.alreadyRunning", "§cEin Spieler spielt bereits");
			messages.set("start.pluginNotSetup", "§cDas Plugin ist nicht fertig eingerichtet");

			messages.set("stop.noPermissions", "§cDu darfst das Spiel von anderen Spielern nicht beenden");
			messages.set("stop.notRunning", "§cEs läuft aktuell kein Spiel");
			messages.set("stop.sucess", "§aDas Spiel wurde erfolgreich beendet");

			messages.set("noPermissions", "§cDu hast keine Berechtigungen um diesen Command auszuführen");
			messages.set("noPlayer", "§cDas sind doch alles Bots");

			messages.set("countdown.3", "§c§l3");
			messages.set("countdown.2", "§e§l2");
			messages.set("countdown.1", "§b§l1");
			messages.set("countdown.0", "§a§lGo!");

			messages.set("gameEnd.normal.title", "§c§lDas Spiel ist vorbei");
			messages.set("gameEnd.normal.subtitle", "§a%s Punkte");

			messages.set("gameEnd.highScore.title", "§b§lNeuer Highscore");
			messages.set("gameEnd.highScore.subtitle", "§a%s Punkte");

			messages.set("game.actionbar", "§a%s §b§l | §c%s Punkte");

		}
		save();

		getCommand("wrackamule").setExecutor(new CommandWrack_A_Mule(this));
		getCommand("wrackamule").setTabCompleter(new TabCompleterWrackAMule());

		prefix = messages.getString("prefix");
	}

	@Override
	public void onDisable() {
		if (muleRunnable != null) {
			muleRunnable.mules.keySet().forEach((mule) -> mule.remove());
		}
	}

	public FileConfiguration getMessages() {
		return messages;
	}

	public static Main getInstance() {
		return instance;
	}

	public void start(Player p) {
		lastGamemode = p.getGameMode();
		p.setGameMode(GameMode.ADVENTURE);
		lastItems.clear();
		Inventory inv = p.getInventory();
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) != null) {
				lastItems.put(i, inv.getItem(i));
			}
		}
		p.getInventory().clear();

		player = p;

		p.sendTitle(getMessages().getString("countdown.3"), "", 2, 16, 2);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				p.sendTitle(getMessages().getString("countdown.2"), "", 2, 16, 2);
			}
		}, 20);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				p.sendTitle(getMessages().getString("countdown.1"), "", 2, 16, 2);
			}
		}, 40);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			@Override
			public void run() {
				p.sendTitle(getMessages().getString("countdown.0"), "", 2, 16, 2);
				muleRunnable = new MuleRunnable(instance, getOwnConfig(), getLeaderboard(), p);
				Bukkit.getPluginManager().registerEvents(muleRunnable, Main.getInstance());
				wamTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, muleRunnable, 5, 5);
			}
		}, 60);
	}

	public Integer getWamTaskId() {
		return wamTaskId;
	}

	public Player getPlayer() {
		return player;
	}

	public void stop() {
		if (player.isOnline()) {
			player.setGameMode(lastGamemode);
			Inventory inventory = player.getInventory();
			inventory.clear();
			for (int i : lastItems.keySet()) {
				inventory.setItem(i, lastItems.get(i));
			}
		}

		HandlerList.unregisterAll(muleRunnable);
		muleRunnable.mules.values().forEach((mule) -> mule.leave());
		player = null;
		Bukkit.getScheduler().cancelTask(wamTaskId);
		wamTaskId = null;
		muleRunnable = null;
	}

	public boolean isConfigComplete() {
		if (config.contains("center") && config.contains("radius") && config.contains("pointsPerMule")
				&& config.contains("minusPoints") && config.contains("lossPerSecond") && config.contains("time")
				&& config.contains("spawnPercent")) {
			return true;
		}
		return false;
	}

	public void save() {
		try {
			leaderboard.save(leaderboardFile);
			config.save(configFile);
			messages.save(messageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FileConfiguration getOwnConfig() {
		return config;
	}

	public FileConfiguration getLeaderboard() {
		return leaderboard;
	}

}
