package me.fragment.srefactoring.islands;

import me.fragment.srefactoring.core.plugins.SRPlugin;
import me.fragment.srefactoring.islands.listeners.SkyblockEvents;

public class IslandsPlugin extends SRPlugin {

	@Override
	public void onEnable() {
		super.onEnable();
		saveDefaultConfig();

		getServer().getPluginManager().registerEvents(new SkyblockEvents(), this);
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

}
