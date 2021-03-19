package de.domi207.wam.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static String prefix = "";
	File configFile;
	File leaderboardFile;
	FileConfiguration config;
	FileConfiguration leaderboard;

	@Override
	public void onEnable() {
		leaderboardFile = new File(getDataFolder(), "leaderboard.yml");
		configFile = new File(getDataFolder(), "config.yml");
		config = new YamlConfiguration();
		leaderboard = new YamlConfiguration();

		if (leaderboardFile.exists()) {
			try {
				leaderboard.load(leaderboardFile);
				config.load(configFile);
			} catch (IOException | InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			leaderboardFile.mkdirs();
			try {
				leaderboardFile.createNewFile();
				configFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			leaderboard.createSection("best");
			config.set("Prefix", "§7[§aWrack§7-§bA§7-§cMole§7]§6: ");
		}
		prefix = config.getString("Prefix");
	}

	public boolean isConfigComplete() {
		if (config.contains("centerPosition") && config.contains("radius") && config.contains("pointsPerMule")
				&& config.contains("minusPoints") && config.contains("lossPerSecond")) {
			return true;
		}
		return false;
	}

}
