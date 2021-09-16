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

public class TotemUpgradeInv implements InventoryProvider {

	private Map<Long, DoublePair<SlotPos, ClickableItem>> tempItems = new HashMap<Long, DoublePair<SlotPos, ClickableItem>>();
	private Totem totem;
	private UpgradeConfig upgradeConfig;

	public static SmartInventory openTotemUpgradeInv(Player player, Totem totem, UpgradeConfig upgradeConfig, SmartInventory parentInv) {
		SmartInventory inv = SmartInventory.builder().manager(CorePlugin.getInventoryManager()).id("islandTotemUpgradeGUI").provider(new TotemUpgradeInv(totem, upgradeConfig))
				.size(IslandsPlugin.getConf().getTotemUpgradeInvSize() / 9, 9)
				.title(IslandsPlugin.getConf().getTotemUpgradeInvTitle().replace("%upgrade-name%", upgradeConfig.getSuperiorUpgrade().getName())).parent(parentInv).build();
		player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemUpgradeInvSound().getFirst(), IslandsPlugin.getConf().getTotemUpgradeInvSound().getSecond(),
				IslandsPlugin.getConf().getTotemUpgradeInvSound().getThird());
		inv.open(player);

		return inv;
	}

	public TotemUpgradeInv(Totem totem, UpgradeConfig upgradeConfig) {
		this.totem = totem;
		this.upgradeConfig = upgradeConfig;
	}

	public Totem getTotem() {
		return totem;
	}

	public UpgradeConfig getUpgradeConfig() {
		return upgradeConfig;
	}

	public Map<Long, DoublePair<SlotPos, ClickableItem>> getTempItems() {
		return tempItems;
	}

	@Override
	public void init(Player player, InventoryContents contents) {
		Pagination pagination = contents.pagination();

		totem.getMana().thenAcceptBoth(totem.getUpPoints(), (mana, upPoints) -> {
			ClickableItem[] upgrades = new ClickableItem[Math.max(Iterators.size(IslandsPlugin.getConf().getTotemUpgradeInvSlotsIterator()),
					this.totem.getIsland().getUpgradeLevel(this.upgradeConfig.getSuperiorUpgrade()).getLevel())];

			Arrays.fill(upgrades, ClickableItem.empty(IslandsPlugin.getConf().getTotemUpgradeInvFillerItem()));
			contents.fill(ClickableItem.empty(IslandsPlugin.getConf().getTotemMainInvFillerItem()));

			for (int i = 1; i <= this.upgradeConfig.getLevelsSettings().size(); i++) {
				List<DoublePair<String, String>> placeholders = Arrays.asList(new DoublePair<String, String>("%roman-level%", Utils.IntegerToRomanNumeral(i)),
						new DoublePair<String, String>("%upPoints%", Utils.longFormatter(upPoints)),
						new DoublePair<String, String>("%island-size%", Utils.longFormatter(this.upgradeConfig.getSuperiorUpgrade().getUpgradeLevel(i).getBorderSize() * 2)),
						new DoublePair<String, String>("%island-current-size%", Utils.longFormatter(totem.getIsland().getIslandSize() * 2)),
						new DoublePair<String, String>("%mana-cost%", String.valueOf(this.upgradeConfig.getLevelSettings(i).get("mana"))),
						new DoublePair<String, String>("%upPoints-cost%", String.valueOf(this.upgradeConfig.getLevelSettings(i).get("upPoints"))),
						new DoublePair<String, String>("%mana%", Utils.longFormatter(mana)));
				ClickableItem item;

				if (this.totem.getIsland().getUpgradeLevel(this.upgradeConfig.getSuperiorUpgrade()).getLevel() >= i) {
					item = ClickableItem.of(Utils.applyPlaceholders(upgradeConfig.getLeveledItem(), placeholders), click -> {

					});
				} else {
					item = ClickableItem.of(Utils.applyPlaceholders(upgradeConfig.getLevelableItem(), placeholders), click -> {

					});
				}

				upgrades[i] = item;
			}

			pagination.setItemsPerPage(Iterators.size(IslandsPlugin.getConf().getTotemUpgradeInvSlotsIterator()));
			pagination.setItems(upgrades);
			pagination.addToIterator(contents.newIterator(SlotIterator.Type.CUSTOM, IslandsPlugin.getConf().getTotemUpgradeInvSlotsIterator()));
		});
		contents.set(new SlotPos(IslandsPlugin.getConf().getTotemUpgradeInvBackSlot() / 9, IslandsPlugin.getConf().getTotemUpgradeInvBackSlot() % 9),
				ClickableItem.of(IslandsPlugin.getConf().getTotemUpgradeInvBackItem(), click -> {
					contents.inventory().getParent().ifPresent(parentInv -> parentInv.open(player));
				}));
		if (!pagination.isLast()) {
			contents.set(new SlotPos(IslandsPlugin.getConf().getTotemUpgradeInvNextPageSlot() / 9, IslandsPlugin.getConf().getTotemUpgradeInvNextPageSlot() % 9),
					ClickableItem.of(IslandsPlugin.getConf().getTotemUpgradeInvNextPageItem(), click -> {
						if (pagination.isLast()) {
							this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
									contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
							click.setCurrentItem(IslandsPlugin.getConf().getTotemUpgradeInvNoNextPageTaskItem());
							player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemUpgradeInvNoNextPageTaskSound().getFirst(),
									IslandsPlugin.getConf().getTotemUpgradeInvNoNextPageTaskSound().getSecond(),
									IslandsPlugin.getConf().getTotemUpgradeInvNoNextPageTaskSound().getThird());

							return;
						}

						this.tempItems.clear();
						contents.inventory().open(player, pagination.next().getPage());
					}));
		}

		if (!pagination.isFirst()) {
			contents.set(new SlotPos(IslandsPlugin.getConf().getTotemUpgradeInvPreviousPageSlot() / 9, IslandsPlugin.getConf().getTotemUpgradeInvPreviousPageSlot() % 9),
					ClickableItem.of(IslandsPlugin.getConf().getTotemUpgradeInvPreviousPageItem(), click -> {
						if (pagination.isLast()) {
							this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
									contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
							click.setCurrentItem(IslandsPlugin.getConf().getTotemUpgradeInvNoPreviousPageTaskItem());
							player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemUpgradeInvNoPreviousPageTaskSound().getFirst(),
									IslandsPlugin.getConf().getTotemUpgradeInvNoPreviousPageTaskSound().getSecond(),
									IslandsPlugin.getConf().getTotemUpgradeInvNoPreviousPageTaskSound().getThird());

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
