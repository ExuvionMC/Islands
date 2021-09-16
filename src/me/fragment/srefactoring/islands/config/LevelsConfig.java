package me.fragment.srefactoring.islands.config;

import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import me.fragment.srefactoring.core.utils.AbstractConfig;
import me.fragment.srefactoring.core.utils.ItemConfig;
import me.fragment.srefactoring.islands.IslandsPlugin;

public class LevelsConfig extends AbstractConfig {

	public LevelsConfig() {
		super(IslandsPlugin.getInstance(), "levels");
	}

	// Levels Section
	public Map<Integer, LevelConfig> getLevels() {
		return this.getConfig().getConfigurationSection("levels").getKeys(false).stream()
				.collect(Collectors.toMap(Integer::valueOf, lvl -> new LevelConfig(this.getConfig().getConfigurationSection("levels." + lvl))));
	}

	public static class LevelConfig {

		private ConfigurationSection section;

		public LevelConfig(ConfigurationSection section) {
			this.section = section;
		}

		// Settings Section
		public int getManaMaxSettings() {
			return this.section.getInt("settings.mana-max");
		}

		public int getManaRequiredSettings() {
			return this.section.getInt("settings.required.mana");
		}

		// Items Section
		public ItemStack getCompletedItem() {
			return new ItemConfig(section.getConfigurationSection("items.completed")).getItem();
		}

		public ItemStack getInProgressItem() {
			return new ItemConfig(section.getConfigurationSection("items.in-progress")).getItem();
		}

		public ItemStack getLockedItem() {
			return new ItemConfig(section.getConfigurationSection("items.locked")).getItem();
		}

	}

}
