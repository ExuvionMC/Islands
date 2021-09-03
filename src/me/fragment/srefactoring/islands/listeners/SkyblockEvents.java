package me.fragment.srefactoring.islands.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bgsoftware.superiorskyblock.api.events.IslandCreateEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandDisbandEvent;

import co.aikar.idb.DB;

public class SkyblockEvents implements Listener {

	@EventHandler
	public void onIslandCreate(IslandCreateEvent event) {
		DB.executeUpdateAsync("INSERT INTO `islands`(`uuid`) VALUES (?)", event.getIsland().getUniqueId().toString());
	}

	@EventHandler
	public void onIslandDisband(IslandDisbandEvent event) {

	}

}
