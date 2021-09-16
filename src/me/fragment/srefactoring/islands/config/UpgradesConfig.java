package me.fragment.srefactoring.islands.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.upgrades.Upgrade;

import me.fragment.srefactoring.core.utils.AbstractConfig;
import me.fragment.srefactoring.core.utils.ItemConfig;
import me.fragment.srefactoring.islands.IslandsPlugin;

public class UpgradesConfig extends AbstractConfig {

	public UpgradesConfig() {
		super(IslandsPlugin.getInstance(), "upgrades");
	}

	public List<UpgradeConfig> getUpgradesConfig() {
		return this.getConfig().getConfigurationSection("upgrades").getKeys(false).stream().map(key -> new UpgradeConfig(this.getConfig().getConfigurationSection("upgrades." + key)))
				.collect(Collectors.toList());
	}

	public static class UpgradeConfig {

		private ConfigurationSection section;

		public UpgradeConfig(ConfigurationSection section) {
			this.section = section;
		}

		public String getName() {
			return this.section.getName();
		}

		public Upgrade getSuperiorUpgrade() {
			return SuperiorSkyblockAPI.getUpgrades().getUpgrade(getName());
		}

		public Map<Integer, Map<String, Double>> getLevelsSettings() {
			return this.section.getConfigurationSection("settings.levels").getKeys(false).stream()
					.collect(Collectors.toMap(Integer::valueOf, level -> this.section.getConfigurationSection("settings.levels." + level).getKeys(false).stream()
							.collect(Collectors.toMap(key -> key, key -> this.section.getDouble("settings.levels." + level + "." + key)))));
		}

		public Map<String, Double> getLevelSettings(int level) {
			return getLevelsSettings().getOrDefault(level, null);
		}

		public ItemStack getUnlockedItem() {
			return new ItemConfig(this.section.getConfigurationSection("items.unlocked")).getItem();
		}

		public ItemStack getLevelableItem() {
			return new ItemConfig(this.section.getConfigurationSection("items.levelable")).getItem();
		}

		public ItemStack getLeveledItem() {
			return new ItemConfig(this.section.getConfigurationSection("items.leveled")).getItem();
		}

		public ItemStack getLockedItem() {
			return new ItemConfig(this.section.getConfigurationSection("items.locked")).getItem();
		}

	}

}
