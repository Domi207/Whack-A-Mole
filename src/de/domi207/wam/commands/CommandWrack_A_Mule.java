package de.domi207.wam.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import de.domi207.wam.main.Main;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class CommandWrack_A_Mule implements CommandExecutor {

	Main main;

	public CommandWrack_A_Mule(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (sender.hasPermission("wam")) {
				if (args.length == 0) {
					Inventory inventory = Bukkit.createInventory(null, 27,
							main.getMessages().getString("inventory.title"));

					ItemStack fillerStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
					ItemMeta fillerMeta = fillerStack.getItemMeta();
					fillerMeta.setDisplayName("§r");
					fillerStack.setItemMeta(fillerMeta);

					for (int i = 0; i < inventory.getSize(); i++) {
						inventory.setItem(i, fillerStack);
					}
					ItemStack startStopItem = new ItemStack(Material.LIME_CONCRETE);
					if (main.getPlayer() != null) {
						if (p.hasPermission("wam.other.stop") || main.getPlayer() == p) {
							startStopItem.setType(Material.RED_CONCRETE);
							ItemMeta itemMeta = startStopItem.getItemMeta();
							itemMeta.setDisplayName(main.getMessages().getString("inventory.item.stop.name"));
							startStopItem.setItemMeta(itemMeta);
						} else {
							startStopItem.setType(Material.YELLOW_CONCRETE);
							ItemMeta itemMeta = startStopItem.getItemMeta();
							itemMeta.setDisplayName(main.getMessages().getString("inventory.item.cantStart.name"));
							startStopItem.setItemMeta(itemMeta);
						}
					} else {
						ItemMeta itemMeta = startStopItem.getItemMeta();
						itemMeta.setDisplayName(main.getMessages().getString("inventory.item.start.name"));
						startStopItem.setItemMeta(itemMeta);
					}

					ItemStack highScoreStack = new ItemStack(Material.WRITTEN_BOOK);
					BookMeta highScoreMeta = (BookMeta) highScoreStack.getItemMeta();
					highScoreMeta.setTitle("Du böser Cheater!");
					highScoreMeta.setDisplayName(main.getMessages().getString("inventory.item.highscores.name"));
					highScoreMeta.setAuthor("");
					highScoreMeta.spigot().addPage(new ComponentBuilder(
							"§4§lDu böser Cheater! §r \nWe're no strangers to love. You know the rules and so do. I A full commitment's what I'm thinking of. You wouldn't get this from any other guy. I just wanna tell you how. I'm feeling Gotta make you understand. Never gonna give you up.")
									.create());
					highScoreStack.setItemMeta(highScoreMeta);

					inventory.setItem(11, startStopItem);
					inventory.setItem(15, highScoreStack);
					p.openInventory(inventory);
				} else {
					if (args[0].equalsIgnoreCase("leaderboard")) {
						FileConfiguration leaderboard = main.getLeaderboard();

						List<String> keys = new ArrayList<String>(leaderboard.getKeys(false));
						keys.sort(new Comparator<String>() {
							@Override
							public int compare(String o1, String o2) {
								ConfigurationSection section1 = leaderboard.getConfigurationSection(o1);
								Integer points1 = section1.getInt("bestPoints");
								Integer date1 = section1.getInt("date");

								ConfigurationSection section2 = leaderboard.getConfigurationSection(o2);
								Integer points2 = section2.getInt("bestPoints");
								Integer date2 = section2.getInt("date");

								if (points1 == points2) {
									return date2 - date1;
								}
								return points1 - points2;
							}
						});
						System.out.println(keys);
						Integer amount = 10;
						if (args.length == 2) {
							try {
								amount = Integer.parseInt(args[1]);
							} catch (Exception e) {
								sender.sendMessage(
										Main.prefix + main.getMessages().getString("leaderboard.invalidvalue"));
								if (amount > 0) {
									sender.sendMessage(
											Main.prefix + main.getMessages().getString("leaderboard.invalidvalue"));
								}
								return true;
							}
							if (amount > 100) {
								sender.sendMessage(Main.prefix + main.getMessages().getString("leaderboard.maxvalue"));
								amount = 100;
							}
						}
						if (amount > keys.size()) {
							amount = keys.size();
						}
						sender.sendMessage(Main.prefix + main.getMessages().getString("leaderboard.header"));
						for (String key : keys) {
							if (amount > 0) {
								ConfigurationSection playerSection = leaderboard.getConfigurationSection(key);
								OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(key));
								Integer points = playerSection.getInt("bestPoints");
								String name = player.getName();
								Long time = playerSection.getLong("date");
								SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
								String fomattedDate = format.format(new Date(time));

								sender.sendMessage(String.format(main.getMessages().getString("leaderboard.template"),
										amount, name, points, fomattedDate));
							}
							amount--;
						}
					} else if (args[0].equalsIgnoreCase("settings")) {
						if (sender.hasPermission("wam.settings")) {
							if (args.length > 1) {
								if (args[1].equalsIgnoreCase("center")) {
									Location loc = new Location(p.getLocation().getWorld(), p.getLocation().getBlockX(),
											p.getLocation().getBlockY(), p.getLocation().getBlockZ());
									main.getOwnConfig().set("center", loc);
									sender.sendMessage(
											Main.prefix + main.getMessages().getString("settings.center.sucess"));
									main.save();
								} else if (args[1].equalsIgnoreCase("radius")) {
									if (args.length == 3) {
										try {
											Integer value = Integer.parseInt(args[2]);
											if (value != null && value > 1) {
												main.getOwnConfig().set("radius", value);
												main.save();
												sender.sendMessage(Main.prefix
														+ main.getMessages().getString("settings.radius.sucess"));
											} else {
												sender.sendMessage(Main.prefix
														+ main.getMessages().getString("settings.radius.invalidValue"));
											}
										} catch (Exception e) {
											sender.sendMessage(Main.prefix
													+ main.getMessages().getString("settings.radius.invalidValue"));
										}
									} else {
										sender.sendMessage(
												Main.prefix + main.getMessages().getString("settings.radius.syntax"));
									}
								} else if (args[1].equalsIgnoreCase("pointsPerMule")) {
									if (args.length == 3) {
										try {
											Integer value = Integer.parseInt(args[2]);
											if (value != null && value > 1) {
												main.getOwnConfig().set("pointsPerMule", value);
												main.save();
												sender.sendMessage(Main.prefix + main.getMessages()
														.getString("settings.pointsPerMule.sucess"));
											} else {
												sender.sendMessage(Main.prefix + main.getMessages()
														.getString("settings.pointsPerMule.invalidValue"));
											}
										} catch (Exception e) {
											sender.sendMessage(Main.prefix + main.getMessages()
													.getString("settings.pointsPerMule.invalidValue"));
										}
									} else {
										sender.sendMessage(Main.prefix
												+ main.getMessages().getString("settings.pointsPerMule.syntax"));
									}
								} else if (args[1].equalsIgnoreCase("lossPerSecond")) {
									if (args.length == 3) {
										try {
											Integer value = Integer.parseInt(args[2]);
											if (value != null && value > 0) {
												main.getOwnConfig().set("lossPerSecond", value);
												main.save();
												sender.sendMessage(Main.prefix + main.getMessages()
														.getString("settings.lossPerSecond.sucess"));
											} else {
												sender.sendMessage(Main.prefix + main.getMessages()
														.getString("settings.lossPerSecond.invalidValue"));
											}
										} catch (Exception e) {
											sender.sendMessage(Main.prefix + main.getMessages()
													.getString("settings.lossPerSecond.invalidValue"));
										}
									} else {
										sender.sendMessage(Main.prefix
												+ main.getMessages().getString("settings.lossPerSecond.syntax"));
									}
								} else if (args[1].equalsIgnoreCase("time")) {
									if (args.length == 3) {
										try {
											Integer value = Integer.parseInt(args[2]);
											if (value != null && value > 0) {
												main.getOwnConfig().set("time", value);
												main.save();
												sender.sendMessage(Main.prefix
														+ main.getMessages().getString("settings.time.sucess"));
											} else {
												sender.sendMessage(Main.prefix
														+ main.getMessages().getString("settings.time.invalidValue"));
											}
										} catch (Exception e) {
											sender.sendMessage(Main.prefix
													+ main.getMessages().getString("settings.time.invalidValue"));
										}
									} else {
										sender.sendMessage(
												Main.prefix + main.getMessages().getString("settings.time.syntax"));
									}
								} else if (args[1].equalsIgnoreCase("spawnPercent")) {
									if (args.length == 3) {
										try {
											Integer value = Integer.parseInt(args[2]);
											if (value != null && value > 0) {
												main.getOwnConfig().set("spawnPercent", value);
												main.save();
												sender.sendMessage(Main.prefix
														+ main.getMessages().getString("settings.spawnPercent.sucess"));
											} else {
												sender.sendMessage(Main.prefix + main.getMessages()
														.getString("settings.spawnPercent.invalidValue"));
											}
										} catch (Exception e) {
											sender.sendMessage(Main.prefix + main.getMessages()
													.getString("settings.spawnPercent.invalidValue"));
										}
									} else {
										sender.sendMessage(Main.prefix
												+ main.getMessages().getString("settings.spawnPercent.syntax"));
									}
								} else if (args[1].equalsIgnoreCase("minusPoints")) {
									if (args.length == 3) {
										try {
											Integer value = Integer.parseInt(args[2]);
											if (value != null && value > 0) {
												main.getOwnConfig().set("minusPoints", value);
												main.save();
												sender.sendMessage(Main.prefix
														+ main.getMessages().getString("settings.minusPoints.sucess"));
											} else {
												sender.sendMessage(Main.prefix + main.getMessages()
														.getString("settings.minusPoints.invalidValue"));
											}
										} catch (Exception e) {
											sender.sendMessage(Main.prefix + main.getMessages()
													.getString("settings.minusPoints.invalidValue"));
										}
									} else {
										sender.sendMessage(Main.prefix
												+ main.getMessages().getString("settings.minusPoints.syntax"));
									}
								} else {
									sender.sendMessage(Main.prefix + main.getMessages().getString("settings.syntax"));
								}
							} else {
								sender.sendMessage(Main.prefix + main.getMessages().getString("settings.syntax"));
							}
						} else {
							sender.sendMessage(Main.prefix + main.getMessages().getString("settings.noPermissions"));
						}
					} else if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("play")) {
						if (main.isConfigComplete()) {
							if (main.getWamTaskId() == null) {
								main.start(p);
							} else {
								sender.sendMessage(Main.prefix + main.getMessages().getString("start.alreadyRunning"));
							}
						} else {
							sender.sendMessage(Main.prefix + main.getMessages().getString("start.pluginNotSetup"));
						}
					} else if (args[0].equalsIgnoreCase("stop")) {
						if (main.getWamTaskId() != null) {
							if (main.getPlayer() == p || p.hasPermission("wam.other.stop")) {
								main.stop();
								sender.sendMessage(Main.prefix + main.getMessages().getString("stop.sucess"));
							} else {
								sender.sendMessage(Main.prefix + main.getMessages().getString("stop.noPermissions"));
							}
						} else {
							sender.sendMessage(Main.prefix + main.getMessages().getString("stop.notRunning"));
						}
					}
				}
			} else {
				sender.sendMessage(Main.prefix + main.getMessages().getString("noPermissions"));
			}
		} else {
			sender.sendMessage(Main.prefix + main.getMessages().getString("noPlayer"));
		}
		return false;
	}

}
