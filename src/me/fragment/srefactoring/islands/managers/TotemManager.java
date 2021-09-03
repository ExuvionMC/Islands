package me.fragment.srefactoring.islands.managers;

import org.bukkit.World.Environment;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.bgsoftware.superiorskyblock.api.island.Island;

public class TotemManager {

	public TotemManager() {
		// TODO Auto-generated constructor stub
	}

	public Totem createTotem(Island island) {
		EnderCrystal enderCrystal = (EnderCrystal) island.getTeleportLocations().get(Environment.NORMAL).getWorld()
				.spawnEntity(island.getTeleportLocations().get(Environment.NORMAL), EntityType.ENDER_CRYSTAL,
						SpawnReason.CUSTOM);

		return new Totem(island, enderCrystal);
	}

}
