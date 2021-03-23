package de.domi207.wam.main;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MuleRunnable implements Runnable, Listener {

	Player player;
	Long startTime = System.currentTimeMillis();
	Integer points = 0;

	Integer maxTime;
	Integer minusPoints;
	Integer pointsPerMule;
	Integer radius;
	Integer lossPerSecond;
	Location center;
	Integer spawnPercent;
	Main main;
	double x1;
	double x2;
	double z1;
	double z2;

	FileConfiguration leaderboard;

	HashMap<ArmorStand, Mule> mules = new HashMap<>();

	public MuleRunnable(Main main, FileConfiguration config, FileConfiguration leaderboard, Player p) {
		maxTime = config.getInt("time") * 1000;
		pointsPerMule = config.getInt("pointsPerMule");
		radius = config.getInt("radius");
		lossPerSecond = config.getInt("lossPerSecond");
		center = config.getLocation("center");
		spawnPercent = config.getInt("spawnPercent");
		minusPoints = config.getInt("minusPoints");
		this.main = main;
		player = p;

		p.teleport(center);

		x1 = center.getX() - radius - 1;
		x2 = center.getX() + radius + 1;
		z1 = center.getZ() - radius - 1;
		z2 = center.getZ() + radius + 1;

		this.leaderboard = leaderboard;

	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location to = e.getTo();
		if (p == player) {
			if (to.getX() < x1) {
				p.setVelocity(p.getVelocity().setX(1).setY(0.5));
			}
			if (to.getX() > x2) {
				p.setVelocity(p.getVelocity().setX(-0.5).setY(0.5));
			}
			if (to.getZ() < z1) {
				p.setVelocity(p.getVelocity().setZ(0.5).setY(0.5));
			}
			if (to.getZ() > z2) {
				p.setVelocity(p.getVelocity().setZ(-0.5).setY(0.5));
			}
		} else {
			if (to.getX() >= x1 && to.getX() <= center.getX()) {
				p.setVelocity(p.getVelocity().setX(-0.5).setY(0.5));
			}
			if (to.getX() <= x2 && to.getX() >= center.getX()) {
				p.setVelocity(p.getVelocity().setX(0.5).setY(0.5));
			}
			if (to.getX() >= x1 && to.getX() <= center.getX()) {
				p.setVelocity(p.getVelocity().setZ(-0.5).setY(0.5));
				p.setVelocity(p.getVelocity().add(new Vector(0, 0.5, -1)).setY(0.5));
			}
			if (to.getX() <= x2 && to.getX() >= center.getX()) {
				p.setVelocity(p.getVelocity().setZ(0.5).setY(0.5));
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerAnimationEvent e) {
		points -= minusPoints;
	}

	@EventHandler
	public void onInteractEntity(EntityDamageByEntityEvent e) {
		if (mules.containsKey(e.getEntity())) {
			Mule mule = mules.get(e.getEntity());
			points += pointsPerMule - mule.calcHit(lossPerSecond) + minusPoints;
		}
	}

	@Override
	public void run() {
		if ((System.currentTimeMillis() - startTime) > maxTime) {

			if (leaderboard.contains(player.getUniqueId().toString())) {
				ConfigurationSection playerSection = leaderboard
						.getConfigurationSection(player.getUniqueId().toString());
				Integer bestPoints = playerSection.getInt("bestPoints");
				if (points > bestPoints) {
					playerSection.set("bestPoints", points);
					playerSection.set("date", System.currentTimeMillis());
					main.save();
					player.sendTitle("§b§lNeuer Highscore!", "§a" + points + " Punkte", 10, 60, 10);

					player.sendTitle(main.getMessages().getString("gameEnd.highScore.title"),
							String.format(main.getMessages().getString("gameEnd.highScore.subtitle"), points), 10, 60,
							10);
				} else {
					player.sendTitle(main.getMessages().getString("gameEnd.normal.title"),
							String.format(main.getMessages().getString("gameEnd.normal.subtitle"), points), 10, 60, 10);
				}
			} else {
				ConfigurationSection playerSection = leaderboard.createSection(player.getUniqueId().toString());
				playerSection.set("bestPoints", points);
				playerSection.set("date", System.currentTimeMillis());
				main.save();

				player.sendTitle(main.getMessages().getString("gameEnd.normal.title"),
						String.format(main.getMessages().getString("gameEnd.normal.subtitle"), points), 10, 60, 10);
			}

			main.stop();
		} else {

			if (player != null) {
				Long millis = System.currentTimeMillis() - startTime;

				String formatted = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
						TimeUnit.MILLISECONDS.toMinutes(millis)
								- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
						TimeUnit.MILLISECONDS.toSeconds(millis)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
						String.format(main.getMessages().getString("game.actionbar"), formatted, points)));

			}
			if (new Random().nextInt(100) < spawnPercent) {
				Location spawn = new Location(center.getWorld(), center.getX() - radius, center.getY() - 2.5,
						center.getZ() - radius);
				spawn.setX(spawn.getX() + ((double) new Random().nextInt((radius * 2 + 1) * 100)) / 100);
				spawn.setZ(spawn.getZ() + ((double) new Random().nextInt((radius * 2 + 1) * 100)) / 100);
				while (isNearToOthers(spawn)) {
					spawn = new Location(center.getWorld(), center.getX() - radius, center.getY() - 2.5,
							center.getZ() - radius);
					spawn.setX(spawn.getX() + ((double) new Random().nextInt((radius * 2 + 1) * 100)) / 100);
					spawn.setZ(spawn.getZ() + ((double) new Random().nextInt((radius * 2 + 1) * 100)) / 100);
				}

				new Mule(spawn, this);
			}
		}
	}

	public boolean isNearToOthers(Location loc) {
		for (ArmorStand armorStand : mules.keySet()) {
			Location loc2 = new Location(loc.getWorld(), armorStand.getLocation().getX(), loc.getY(),
					armorStand.getLocation().getZ());
			if (loc.distance(loc2) < 1.5) {
				return true;
			}
		}
		return false;
	}

}
