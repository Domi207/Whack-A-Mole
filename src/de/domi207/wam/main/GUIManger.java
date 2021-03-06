package de.domi207.wam.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.chat.ComponentBuilder;

public class GUIManger implements Listener {

	Main main = Main.getInstance();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (p.hasPermission("wam")) {
			if (e.getView().getTitle().equalsIgnoreCase(main.getMessages().getString("highscoreinventory.title"))) {
				if (e.getSlot() == e.getRawSlot()) {
					if (e.getSlot() == 0) {
						Inventory inventory = Bukkit.createInventory(null, 27,
								main.getMessages().getString("inventory.title"));

						ItemStack fillerStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
						ItemMeta fillerMeta = fillerStack.getItemMeta();
						fillerMeta.setDisplayName("?r");
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
						highScoreMeta.setTitle("Du b?ser Cheater!");
						highScoreMeta.setDisplayName(main.getMessages().getString("inventory.item.highscores.name"));
						highScoreMeta.setAuthor("");
						highScoreMeta.spigot().addPage(new ComponentBuilder(
								"?4?lDu b?ser Cheater! ?r \nWe're no strangers to love. You know the rules and so do. I A full commitment's what I'm thinking of. You wouldn't get this from any other guy. I just wanna tell you how. I'm feeling Gotta make you understand. Never gonna give you up.")
										.create());
						highScoreStack.setItemMeta(highScoreMeta);

						inventory.setItem(11, startStopItem);
						inventory.setItem(15, highScoreStack);
						p.openInventory(inventory);
					}
					e.setCancelled(true);
				}
			}
			if (e.getView().getTitle().equalsIgnoreCase(main.getMessages().getString("inventory.title"))) {
				if (e.getSlot() == e.getRawSlot()) {
					e.setCancelled(true);
					if (e.getSlot() == 11) {
						if (main.getPlayer() == null) {
							main.start(p);
							p.closeInventory();
						} else {
							if (p.hasPermission("wam.other.stop") || p == main.getPlayer()) {
								main.stop();
								p.closeInventory();
							}
						}
					}
					if (e.getSlot() == 15) {
						Inventory inventory = Bukkit.createInventory(null, 27,
								main.getMessages().getString("highscoreinventory.title"));

						ItemStack fillerStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
						ItemMeta fillerMeta = fillerStack.getItemMeta();
						fillerMeta.setDisplayName("?r");
						fillerStack.setItemMeta(fillerMeta);

						for (int i = 0; i < inventory.getSize(); i++) {
							inventory.setItem(i, fillerStack);
						}

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
									return date1 - date2;
								}
								return points2 - points1;
							}
						});
						System.out.println(keys);
						Integer amount = 7;
						if (amount > keys.size()) {
							amount = keys.size();
						}
						for (String key : keys) {
							if (amount > 0) {
								ConfigurationSection playerSection = leaderboard.getConfigurationSection(key);
								OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(key));
								Integer points = playerSection.getInt("bestPoints");
								String name = player.getName();
								Long time = playerSection.getLong("date");
								SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
								String fomattedDate = format.format(new Date(time));

								ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
								SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
								skullMeta.setOwningPlayer(player);
								skullMeta.setDisplayName(String.format(
										main.getMessages().getString("highscoreinventory.template"), name, points));
								ArrayList<String> lore = new ArrayList<>();
								lore.add(String.format(main.getMessages().getString("highscoreinventory.loretemplate"),
										fomattedDate));
								skullMeta.setLore(lore);
								itemStack.setItemMeta(skullMeta);
								inventory.setItem(keys.indexOf(key) + 10, itemStack);

							}
							amount--;
						}

						ItemStack back = new ItemStack(Material.PAPER);
						ItemMeta backMeta = back.getItemMeta();
						backMeta.setDisplayName(main.getMessages().getString("leaderboard.back"));
						back.setItemMeta(backMeta);
						inventory.setItem(0, back);

						if (leaderboard.contains(p.getUniqueId().toString())) {
							ConfigurationSection playerSection = leaderboard
									.getConfigurationSection(p.getUniqueId().toString());
							Player player = p;
							Integer points = playerSection.getInt("bestPoints");
							String name = player.getName();
							Long time = playerSection.getLong("date");
							SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY");
							String fomattedDate = format.format(new Date(time));

							ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
							SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
							skullMeta.setOwningPlayer(player);
							skullMeta.setDisplayName(String
									.format(main.getMessages().getString("highscoreinventory.template"), name, points));
							ArrayList<String> lore = new ArrayList<>();
							lore.add(String.format(main.getMessages().getString("highscoreinventory.loretemplate"),
									fomattedDate));
							skullMeta.setLore(lore);
							itemStack.setItemMeta(skullMeta);
							inventory.setItem(18, itemStack);
						}

						p.openInventory(inventory);
					}
				}
			}
		}
	}

}
