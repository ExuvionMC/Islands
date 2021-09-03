package me.fragment.srefactoring.islands.managers;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EnderCrystal;

import com.bgsoftware.superiorskyblock.api.island.Island;

import net.minecraft.world.entity.boss.enderdragon.EntityEnderCrystal;

public class Totem {

	private Island island;
	private EnderCrystal totemCrystal;

	public Totem(Island island, EnderCrystal totemCrystal) {
		this.island = island;
		this.totemCrystal = totemCrystal;
	}

	public Island getIsland() {
		return island;
	}

	public EnderCrystal getTotemCrystal() {
		return totemCrystal;
	}

	public static class TotemCrystal extends EntityEnderCrystal {

		public TotemCrystal(Location location) {
			super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());

			this.setPosition(location.getX() + 0.5D, location.getY() + 1, location.getZ() + 0.5D);
			this.setInvulnerable(true);
			this.setShowingBottom(false);
			this.setCustomNameVisible(true);
		}

	}

}
