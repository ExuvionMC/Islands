package me.fragment.srefactoring.islands.guis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterators;

import me.fragment.smartinv.ClickableItem;
import me.fragment.smartinv.SmartInventory;
import me.fragment.smartinv.content.InventoryContents;
import me.fragment.smartinv.content.InventoryProvider;
import me.fragment.smartinv.content.Pagination;
import me.fragment.smartinv.content.SlotIterator;
import me.fragment.smartinv.content.SlotPos;
import me.fragment.srefactoring.core.CorePlugin;
import me.fragment.srefactoring.core.utils.DoublePair;
import me.fragment.srefactoring.core.utils.Utils;
import me.fragment.srefactoring.islands.IslandsPlugin;
import me.fragment.srefactoring.islands.config.UpgradesConfig.UpgradeConfig;
import me.fragment.srefactoring.islands.managers.Totem;

public class TotemUpgradesInv implements InventoryProvider {

	private Map<Long, DoublePair<SlotPos, ClickableItem>> tempItems = new HashMap<Long, DoublePair<SlotPos, ClickableItem>>();
	private Totem totem;

	public static SmartInventory openTotemUpgradesInv(Player player, Totem totem, SmartInventory parentInv) {
		SmartInventory inv = SmartInventory.builder().manager(CorePlugin.getInventoryManager()).id("islandTotemUpgradesGUI").provider(new TotemUpgradesInv(totem))
				.size(IslandsPlugin.getConf().getTotemUpgradesInvSize() / 9, 9).title(IslandsPlugin.getConf().getTotemUpgradesInvTitle()).parent(parentInv).build();
		player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemUpgradesInvSound().getFirst(), IslandsPlugin.getConf().getTotemUpgradesInvSound().getSecond(),
				IslandsPlugin.getConf().getTotemUpgradesInvSound().getThird());
		inv.open(player);

		return inv;
	}

	public TotemUpgradesInv(Totem totem) {
		this.totem = totem;
	}

	public Totem getTotem() {
		return totem;
	}

	public Map<Long, DoublePair<SlotPos, ClickableItem>> getTempItems() {
		return tempItems;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		Pagination pagination = contents.pagination();
		contents.fill(ClickableItem.empty(IslandsPlugin.getConf().getTotemMainInvFillerItem()));

		totem.getUnlockedUgrades(totem).thenAcceptBoth(totem.getUpPoints(), (unlockedUpgrades, upPoints) -> {
			Bukkit.getScheduler().runTask(IslandsPlugin.getInstance(), () -> {
				ClickableItem[] upgrades = new ClickableItem[Math.max(Iterators.size(IslandsPlugin.getConf().getTotemUpgradesInvSlotsIterator()),
						IslandsPlugin.getUpgradesConfig().getUpgradesConfig().size())];

				Arrays.fill(upgrades, ClickableItem.empty(IslandsPlugin.getConf().getTotemUpgradesInvFillerItem()));

				for (int i = 0; i < IslandsPlugin.getUpgradesConfig().getUpgradesConfig().size(); i++) {
					UpgradeConfig upgradeConfig = IslandsPlugin.getUpgradesConfig().getUpgradesConfig().get(i);
					List<DoublePair<String, String>> placeholders = Arrays.asList(new DoublePair<String, String>("%upPoints%", Utils.longFormatter(upPoints)),
							new DoublePair<String, String>("%upgrade-progress-bar%",
									Utils.getProgressBar(totem.getIsland().getUpgradeLevel(upgradeConfig.getSuperiorUpgrade()).getLevel(), upgradeConfig.getLevelsSettings().size(),
											upgradeConfig.getLevelsSettings().size(), IslandsPlugin.getConf().getTotemUpgradesInvProgressBarFilled(),
											IslandsPlugin.getConf().getTotemUpgradesInvProgressBarUnfilled())),
							new DoublePair<String, String>("%island-size%", Utils.longFormatter(totem.getIsland().getIslandSize() * 2)),
							new DoublePair<String, String>("%island-spawner-rates%", String.valueOf(totem.getIsland().getSpawnerRatesMultiplier())));
					ClickableItem item;

					if (unlockedUpgrades.contains(upgradeConfig.getName())) {
						item = ClickableItem.of(Utils.applyPlaceholders(upgradeConfig.getUnlockedItem(), placeholders), click -> {
							TotemUpgradeInv.openTotemUpgradeInv(player, this.totem, upgradeConfig, contents.inventory());
						});
					} else {
						item = ClickableItem.of(Utils.applyPlaceholders(upgradeConfig.getLockedItem(), placeholders), click -> {

						});
					}

					upgrades[i] = item;
				}

				pagination.setItemsPerPage(Iterators.size(IslandsPlugin.getConf().getTotemUpgradesInvSlotsIterator()));
				pagination.setItems(upgrades);
				pagination.addToIterator(contents.newIterator(SlotIterator.Type.CUSTOM, IslandsPlugin.getConf().getTotemUpgradesInvSlotsIterator()));
			});
		});
		contents.set(new SlotPos(IslandsPlugin.getConf().getTotemUpgradesInvBackSlot() / 9, IslandsPlugin.getConf().getTotemUpgradesInvBackSlot() % 9),
				ClickableItem.of(IslandsPlugin.getConf().getTotemUpgradesInvBackItem(), click -> {
					contents.inventory().getParent().ifPresent(parentInv -> parentInv.open(player));
				}));

		if (!pagination.isLast()) {
			contents.set(new SlotPos(IslandsPlugin.getConf().getTotemUpgradesInvNextPageSlot() / 9, IslandsPlugin.getConf().getTotemUpgradesInvNextPageSlot() % 9),
					ClickableItem.of(IslandsPlugin.getConf().getTotemUpgradesInvNextPageItem(), click -> {
						if (pagination.isLast()) {
							this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
									contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
							click.setCurrentItem(IslandsPlugin.getConf().getTotemUpgradesInvNoNextPageTaskItem());
							player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemUpgradesInvNoNextPageTaskSound().getFirst(),
									IslandsPlugin.getConf().getTotemUpgradesInvNoNextPageTaskSound().getSecond(),
									IslandsPlugin.getConf().getTotemUpgradesInvNoNextPageTaskSound().getThird());

							return;
						}

						this.tempItems.clear();
						contents.inventory().open(player, pagination.next().getPage());
					}));
		}

		if (!pagination.isFirst()) {
			contents.set(new SlotPos(IslandsPlugin.getConf().getTotemUpgradesInvPreviousPageSlot() / 9, IslandsPlugin.getConf().getTotemUpgradesInvPreviousPageSlot() % 9),
					ClickableItem.of(IslandsPlugin.getConf().getTotemUpgradesInvPreviousPageItem(), click -> {
						if (pagination.isLast()) {
							this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
									contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
							click.setCurrentItem(IslandsPlugin.getConf().getTotemUpgradesInvNoPreviousPageTaskItem());
							player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemUpgradesInvNoPreviousPageTaskSound().getFirst(),
									IslandsPlugin.getConf().getTotemUpgradesInvNoPreviousPageTaskSound().getSecond(),
									IslandsPlugin.getConf().getTotemUpgradesInvNoPreviousPageTaskSound().getThird());

							return;
						}

						this.tempItems.clear();
						contents.inventory().open(player, pagination.next().getPage());
					}));
		}

	}

	@Override
	public void update(Player player, InventoryContents contents) {
		this.tempItems.entrySet().stream().filter(entry -> entry.getKey() < System.currentTimeMillis()).forEach(entry -> {
			contents.set(entry.getValue().getFirst(), entry.getValue().getSecond());
			Bukkit.getScheduler().runTask(IslandsPlugin.getInstance(), () -> this.tempItems.remove(entry.getKey()));
		});
	}

}
