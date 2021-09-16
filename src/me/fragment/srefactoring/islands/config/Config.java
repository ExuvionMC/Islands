package me.fragment.srefactoring.islands.config;

import java.util.ListIterator;
import java.util.stream.Collectors;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import me.fragment.smartinv.content.SlotPos;
import me.fragment.srefactoring.core.utils.AbstractConfig;
import me.fragment.srefactoring.core.utils.ItemConfig;
import me.fragment.srefactoring.core.utils.TriPair;
import me.fragment.srefactoring.core.utils.Utils;
import me.fragment.srefactoring.islands.IslandsPlugin;

public class Config extends AbstractConfig {

	public Config() {
		super(IslandsPlugin.getInstance(), "config");
	}

	// Crystal Section
	public String getCrystalTotemName() {
		return Utils.color(this.getConfig().getString("totem.crystal.name"));
	}

	// Menus Section: Totem Main
	public String getTotemMainInvTitle() {
		return Utils.color(this.getConfig().getString("menus.totem-main.settings.title"));
	}

	public int getTotemMainInvSize() {
		return this.getConfig().getInt("menus.totem-main.settings.size");
	}

	public TriPair<Sound, Float, Float> getTotemMainInvSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-main.settings.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-main.settings.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-main.settings.sound").split(";")[2]));
	}

	public ItemStack getTotemMainInvFillerItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-main.settings.filler-item")).getItem();
	}

	public ItemStack getTotemMainInvTotemItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-main.items.totem")).getItem();
	}

	public int getTotemMainInvTotemSlot() {
		return this.getConfig().getInt("menus.totem-main.items.totem.slot");
	}

	public ItemStack getTotemMainInvUpgradesItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-main.items.upgrades")).getItem();
	}

	public int getTotemMainInvUpgradesSlot() {
		return this.getConfig().getInt("menus.totem-main.items.upgrades.slot");
	}

	// Menus Section: Totem Level Tree
	public String getTotemLevelTreeInvTitle() {
		return Utils.color(this.getConfig().getString("menus.totem-level-tree.settings.title"));
	}

	public int getTotemLevelTreeInvSize() {
		return this.getConfig().getInt("menus.totem-level-tree.settings.size");
	}

	public TriPair<Sound, Float, Float> getTotemLevelTreeInvSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-level-tree.settings.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.settings.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.settings.sound").split(";")[2]));
	}

	public boolean getTotemLevelTreeInvSkipFirst() {
		return this.getConfig().getBoolean("menus.totem-level-tree.settings.skip-first");
	}

	public ListIterator<SlotPos> getTotemLevelTreeInvSlotsIterator() {
		return this.getConfig().getIntegerList("menus.totem-level-tree.settings.slots-iterator").stream().map(slot -> new SlotPos(slot / 9, slot % 9)).collect(Collectors.toList())
				.listIterator();
	}

	public ItemStack getTotemLevelTreeInvFillerItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.settings.filler-item")).getItem();
	}

	public ItemStack getTotemLevelTreeInvBackItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.items.back")).getItem();
	}

	public int getTotemLevelTreeInvBackSlot() {
		return this.getConfig().getInt("menus.totem-level-tree.items.back.slot");
	}

	public ItemStack getTotemLevelTreeInvNextPageItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.items.next-page")).getItem();
	}

	public int getTotemLevelTreeInvNextPageSlot() {
		return this.getConfig().getInt("menus.totem-level-tree.items.next-page.slot");
	}

	public ItemStack getTotemLevelTreeInvPreviousPageItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.items.previous-page")).getItem();
	}

	public int getTotemLevelTreeInvPreviousPageSlot() {
		return this.getConfig().getInt("menus.totem-level-tree.items.previous-page.slot");
	}

	// Task Items
	public ItemStack getTotemLevelTreeInvNoNextPageTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.task-items.no-next-page")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemLevelTreeInvNoNextPageTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.no-next-page.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.no-next-page.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.no-next-page.sound").split(";")[2]));
	}

	public ItemStack getTotemLevelTreeInvNoPreviousPageTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.task-items.no-previous-page")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemLevelTreeInvNoPreviousPageTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.no-previous-page.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.no-previous-page.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.no-previous-page.sound").split(";")[2]));
	}

	public ItemStack getTotemLevelTreeInvLevelNotCompleteTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.task-items.level-not-complete")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemLevelTreeInvLevelNotCompleteTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-not-complete.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-not-complete.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-not-complete.sound").split(";")[2]));
	}

	public ItemStack getTotemLevelTreeInvLevelAlreadyCompletedTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.task-items.level-already-completed")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemLevelTreeInvLevelAlreadyCompletedTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-already-completed.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-already-completed.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-already-completed.sound").split(";")[2]));
	}

	public ItemStack getTotemLevelTreeInvLevelLockedTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-level-tree.task-items.level-locked")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemLevelTreeInvLevelLockedTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-locked.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-locked.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-level-tree.task-items.level-locked.sound").split(";")[2]));
	}

	// Menus Section: Totem Upgrades

	public String getTotemUpgradesInvTitle() {
		return Utils.color(this.getConfig().getString("menus.totem-upgrades.settings.title"));
	}

	public int getTotemUpgradesInvSize() {
		return this.getConfig().getInt("menus.totem-upgrades.settings.size");
	}

	public TriPair<Sound, Float, Float> getTotemUpgradesInvSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-upgrades.settings.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrades.settings.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrades.settings.sound").split(";")[2]));
	}

	public ItemStack getTotemUpgradesInvFillerItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrades.settings.filler-item")).getItem();
	}

	public String getTotemUpgradesInvProgressBarFilled() {
		return this.getConfig().getString("menus.totem-upgrades.settings.progress-bar.filled");
	}

	public String getTotemUpgradesInvProgressBarUnfilled() {
		return this.getConfig().getString("menus.totem-upgrades.settings.progress-bar.unfilled");
	}

	public ItemStack getTotemUpgradesInvTotemItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrades.items.totem")).getItem();
	}

	public int getTotemUpgradesInvTotemSlot() {
		return this.getConfig().getInt("menus.totem-upgrades.items.totem.slot");
	}

	public ListIterator<SlotPos> getTotemUpgradesInvSlotsIterator() {
		return this.getConfig().getIntegerList("menus.totem-upgrades.settings.slots-iterator").stream().map(slot -> new SlotPos(slot / 9, slot % 9)).collect(Collectors.toList())
				.listIterator();
	}

	public ItemStack getTotemUpgradesInvBackItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrades.items.back")).getItem();
	}

	public int getTotemUpgradesInvBackSlot() {
		return this.getConfig().getInt("menus.totem-upgrades.items.back.slot");
	}

	public ItemStack getTotemUpgradesInvNextPageItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrades.items.next-page")).getItem();
	}

	public int getTotemUpgradesInvNextPageSlot() {
		return this.getConfig().getInt("menus.totem-upgrades.items.next-page.slot");
	}

	public ItemStack getTotemUpgradesInvPreviousPageItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrades.items.previous-page")).getItem();
	}

	public int getTotemUpgradesInvPreviousPageSlot() {
		return this.getConfig().getInt("menus.totem-upgrades.items.previous-page.slot");
	}

	// Task Items
	public ItemStack getTotemUpgradesInvNoNextPageTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrades.task-items.no-next-page")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemUpgradesInvNoNextPageTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-upgrades.task-items.no-next-page.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrades.task-items.no-next-page.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrades.task-items.no-next-page.sound").split(";")[2]));
	}

	public ItemStack getTotemUpgradesInvNoPreviousPageTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrades.task-items.no-previous-page")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemUpgradesInvNoPreviousPageTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-upgrades.task-items.no-previous-page.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrades.task-items.no-previous-page.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrades.task-items.no-previous-page.sound").split(";")[2]));
	}

	// Menus Section: Totem Upgrade

	public String getTotemUpgradeInvTitle() {
		return Utils.color(this.getConfig().getString("menus.totem-upgrade.settings.title"));
	}

	public int getTotemUpgradeInvSize() {
		return this.getConfig().getInt("menus.totem-upgrade.settings.size");
	}

	public TriPair<Sound, Float, Float> getTotemUpgradeInvSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-upgrade.settings.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrade.settings.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrade.settings.sound").split(";")[2]));
	}

	public ItemStack getTotemUpgradeInvFillerItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrade.settings.filler-item")).getItem();
	}

	public ListIterator<SlotPos> getTotemUpgradeInvSlotsIterator() {
		return this.getConfig().getIntegerList("menus.totem-upgrade.settings.slots-iterator").stream().map(slot -> new SlotPos(slot / 9, slot % 9)).collect(Collectors.toList())
				.listIterator();
	}

	public ItemStack getTotemUpgradeInvBackItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrade.items.back")).getItem();
	}

	public int getTotemUpgradeInvBackSlot() {
		return this.getConfig().getInt("menus.totem-upgrade.items.back.slot");
	}

	public ItemStack getTotemUpgradeInvNextPageItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrade.items.next-page")).getItem();
	}

	public int getTotemUpgradeInvNextPageSlot() {
		return this.getConfig().getInt("menus.totem-upgrade.items.next-page.slot");
	}

	public ItemStack getTotemUpgradeInvPreviousPageItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrade.items.previous-page")).getItem();
	}

	public int getTotemUpgradeInvPreviousPageSlot() {
		return this.getConfig().getInt("menus.totem-upgrade.items.previous-page.slot");
	}

	// Task Items
	public ItemStack getTotemUpgradeInvNoNextPageTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrade.task-items.no-next-page")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemUpgradeInvNoNextPageTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-upgrade.task-items.no-next-page.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrade.task-items.no-next-page.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrade.task-items.no-next-page.sound").split(";")[2]));
	}

	public ItemStack getTotemUpgradeInvNoPreviousPageTaskItem() {
		return new ItemConfig(this.getConfig().getConfigurationSection("menus.totem-upgrade.task-items.no-previous-page")).getItem();
	}

	public TriPair<Sound, Float, Float> getTotemUpgradeInvNoPreviousPageTaskSound() {
		return new TriPair<>(Sound.valueOf(this.getConfig().getString("menus.totem-upgrade.task-items.no-previous-page.sound").split(";")[0]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrade.task-items.no-previous-page.sound").split(";")[1]),
				Float.valueOf(this.getConfig().getString("menus.totem-upgrade.task-items.no-previous-page.sound").split(";")[2]));
	}

}
