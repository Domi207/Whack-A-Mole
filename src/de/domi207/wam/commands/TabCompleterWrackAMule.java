package de.domi207.wam.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompleterWrackAMule implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> completes = new ArrayList();
		switch (args.length) {
		case 1: {
			if (sender.hasPermission("wam.settings")) {
				completes.add("settings");
			}
			completes.add("start");
			completes.add("stop");
			completes.add("play");
			completes.add("leaderboard");
			break;
		}
		case 2: {
			if (args[0].equalsIgnoreCase("settings")) {
				if (sender.hasPermission("wam.settings")) {
					completes.add("center");
					completes.add("radius");
					completes.add("lossPerSecond");
					completes.add("pointsPerMule");
					completes.add("time");
					completes.add("spawnPercent");
					completes.add("minusPoints");
				}
			}
			break;
		}
		default:
		}
		return completes;
	}

}
