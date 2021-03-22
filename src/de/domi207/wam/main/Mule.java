package de.domi207.wam.main;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Mule implements Listener {

	ArmorStand armorStand;
	Integer animation1;
	Location start;

	MuleRunnable muleRunnable;
	Long startTime = System.currentTimeMillis();

	public Mule(Location loc, MuleRunnable muleRunnable) {
		this.muleRunnable = muleRunnable;

		start = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
		armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		armorStand.setGravity(false);
		armorStand.setInvisible(true);
		armorStand.setHelmet(getHead(Main.getInstance().getConfig().getString("headValue")));

		muleRunnable.mules.put(armorStand, this);

		animation1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				if (round(armorStand.getLocation().getY(), 1) == (loc.getY() + 1)) {
					Bukkit.getScheduler().cancelTask(animation1);
					animation1 = null;
				} else {
					armorStand.teleport(armorStand.getLocation().add(0, 0.1, 0));
				}
			}
		}, 1, 1);

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				leave();
			}
		}, 40);

	}

	public Integer calcHit(Integer reducePerSecond) {
		leave();
		return (int) (((System.currentTimeMillis() - startTime) / 1000f) * (float) reducePerSecond);
	}

	public void leave() {
		if (animation1 != null) {
			Bukkit.getScheduler().cancelTask(animation1);
			animation1 = null;
		}
		animation1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {

			@Override
			public void run() {
				if (armorStand.getLocation().getY() == (start.getY())) {
					muleRunnable.mules.remove(armorStand);
					Bukkit.getScheduler().cancelTask(animation1);
					animation1 = null;
					armorStand.remove();
				} else {
					armorStand.teleport(armorStand.getLocation().add(0, -0.1, 0));
				}
			}
		}, 1, 1);

	}

	private static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	private static ItemStack getHead(String value) {
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
		UUID hashAsId = new UUID(value.hashCode(), value.hashCode());
		return Bukkit.getUnsafe().modifyItemStack(skull,
				"{display:{Name:\"{\\\"text\\\":\\\"Monty Mole\\\"}\"},SkullOwner:{Id:[I;-1396838960,715932738,-1334828223,1662194184],Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGI1MTUwMTU5MmQyZWFiMjZkMDcyNjA2MzQ2YjBkNGJkNjM1ZDM2YjM1NTUwYzFmNjlhNzdmZTNlNTZkM2QxNSJ9fX0=\"}]}}}");
	}

}
