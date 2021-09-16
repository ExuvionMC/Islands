package me.fragment.srefactoring.islands;

import me.fragment.srefactoring.core.plugins.SRPlugin;
import me.fragment.srefactoring.islands.config.Config;
import me.fragment.srefactoring.islands.config.LevelsConfig;
import me.fragment.srefactoring.islands.config.UpgradesConfig;
import me.fragment.srefactoring.islands.listeners.SkyblockEvents;
import me.fragment.srefactoring.islands.listeners.VanillaEvents;
import me.fragment.srefactoring.islands.managers.TotemManager;

public class IslandsPlugin extends SRPlugin {

	private static Config config;
	private static LevelsConfig levelsConfig;
	private static UpgradesConfig upgradesConfig;

	private static TotemManager totemManager;

	@Override
	public void onEnable() {
		super.onEnable();
		saveDefaultConfig();
		saveResource("levels.yml", false);
		saveResource("upgrades.yml", false);
		config = new Config();
		levelsConfig = new LevelsConfig();
		upgradesConfig = new UpgradesConfig();

		totemManager = new TotemManager();

		getServer().getPluginManager().registerEvents(new VanillaEvents(), this);
		getServer().getPluginManager().registerEvents(new SkyblockEvents(), this);
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	public static Config getConf() {
		return config;
	}

	public static LevelsConfig getLevelsConfig() {
		return levelsConfig;
	}

	public static UpgradesConfig getUpgradesConfig() {
		return upgradesConfig;
	}

	public static TotemManager getTotemManager() {
		return totemManager;
	}

}
