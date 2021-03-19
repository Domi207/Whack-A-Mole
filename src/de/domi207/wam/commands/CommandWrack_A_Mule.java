package de.domi207.wam.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.domi207.wam.main.Main;

public class CommandWrack_A_Mule implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("wam.settings")) {
			if (args.length == 0) {

			} else {
				if (args[0].equalsIgnoreCase("settings")) {
					if (sender.hasPermission("wam.settings")) {

					} else {
						sender.sendMessage(
								Main.prefix + "§cDu hast keine Berechtigungen um diesen Command auszuführen.");
					}
				} else if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("play")) {

				} else if (args[0].equalsIgnoreCase("stop")) {

				}
			}
		} else {
			sender.sendMessage(Main.prefix + "§cDu hast keine Berechtigungen um diesen Command auszuführen.");
		}
		return false;
	}

}
