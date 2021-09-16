package me.fragment.srefactoring.islands.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandSchematicPasteEvent;

import me.fragment.srefactoring.islands.IslandsPlugin;

public class SkyblockEvents implements Listener {

	@EventHandler
	public void onSchematicPaste(IslandSchematicPasteEvent event) {
		if (event.getLocation().getWorld().getEnvironment() != Environment.NORMAL)
			return;

		Bukkit.getScheduler().runTask(IslandsPlugin.getInstance(), () -> IslandsPlugin.getTotemManager().createTotem(event.getIsland()));
	}

	@EventHandler
	public void onIslandDisband(IslandDisbandEvent event) {
		IslandsPlugin.getTotemManager().deleteTotem(event.getIsland());
	}

}
