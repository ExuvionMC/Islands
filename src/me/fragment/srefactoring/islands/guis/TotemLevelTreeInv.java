package me.fragment.srefactoring.islands.guis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
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
import me.fragment.srefactoring.islands.config.LevelsConfig.LevelConfig;
import me.fragment.srefactoring.islands.managers.Totem;

public class TotemLevelTreeInv implements InventoryProvider {

	private Map<Long, DoublePair<SlotPos, ClickableItem>> tempItems = new HashMap<Long, DoublePair<SlotPos, ClickableItem>>();
	private Totem totem;

	public static SmartInventory openTotemMainInv(Player player, Totem totem) {
		SmartInventory inv = SmartInventory.builder().manager(CorePlugin.getInventoryManager()).id("islandTotemLevelTreeGUI").provider(new TotemLevelTreeInv(totem))
				.size(IslandsPlugin.getConf().getTotemMainInvSize() / 9, 9).title(IslandsPlugin.getConf().getTotemMainInvTitle()).build();
		player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemMainInvSound().getFirst(), IslandsPlugin.getConf().getTotemMainInvSound().getSecond(),
				IslandsPlugin.getConf().getTotemMainInvSound().getThird());
		inv.open(player);

		return inv;
	}

	public TotemLevelTreeInv(Totem totem) {
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
		totem.getLevel().thenAcceptBoth(totem.getMana(), (level, mana) -> {
			Pagination pagination = contents.pagination();
			ClickableItem[] levels = new ClickableItem[IslandsPlugin.getLevelsConfig().getLevels().size()];

			contents.fill(ClickableItem.empty(IslandsPlugin.getConf().getTotemMainInvFillerItem()));

			contents.set(new SlotPos(IslandsPlugin.getConf().getTotemMainInvTotemSlot() / 9, IslandsPlugin.getConf().getTotemMainInvTotemSlot() % 9), ClickableItem.of(
					Utils.applyPlaceholders(IslandsPlugin.getConf().getTotemMainInvTotemItem(),
							Arrays.asList(new DoublePair<String, String>("%level%", Utils.longFormatter(level)), new DoublePair<String, String>("%mana%", Utils.longFormatter(mana)))),
					click -> {
						System.out.println(click);
					}));

			for (Map.Entry<Integer, LevelConfig> entry : IslandsPlugin.getLevelsConfig().getLevels().entrySet()) {
				List<DoublePair<String, String>> placeholders = Arrays.asList(new DoublePair<String, String>("%roman-level%", Utils.IntegerToRomanNumeral(entry.getKey())),
						new DoublePair<String, String>("%current-mana%", Utils.longFormatter(mana)),
						new DoublePair<String, String>("%required-mana%", Utils.longFormatter(entry.getValue().getManaRequiredSettings())));
				ClickableItem item;

				if (level >= entry.getKey()) {
					item = ClickableItem.of(Utils.applyPlaceholders(entry.getValue().getCompletedItem(), placeholders), click -> {
						this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
								contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
						click.setCurrentItem(IslandsPlugin.getConf().getTotemLevelTreeInvLevelAlreadyCompletedTaskItem());
						player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemLevelTreeInvLevelAlreadyCompletedTaskSound().getFirst(),
								IslandsPlugin.getConf().getTotemLevelTreeInvLevelAlreadyCompletedTaskSound().getSecond(),
								IslandsPlugin.getConf().getTotemLevelTreeInvLevelAlreadyCompletedTaskSound().getThird());
					});
				} else if (level + 1 == entry.getKey()) {
					item = ClickableItem.of(Utils.applyPlaceholders(entry.getValue().getInProgressItem(), placeholders), click -> {
						if (mana >= entry.getValue().getManaRequiredSettings()) {

						} else {
							this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
									contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
							click.setCurrentItem(IslandsPlugin.getConf().getTotemLevelTreeInvLevelNotCompleteTaskItem());
							player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemLevelTreeInvLevelNotCompleteTaskSound().getFirst(),
									IslandsPlugin.getConf().getTotemLevelTreeInvLevelNotCompleteTaskSound().getSecond(),
									IslandsPlugin.getConf().getTotemLevelTreeInvLevelNotCompleteTaskSound().getThird());
						}
					});
				} else {
					item = ClickableItem.of(Utils.applyPlaceholders(entry.getValue().getLockedItem(), placeholders), click -> {
						this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
								contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
						click.setCurrentItem(IslandsPlugin.getConf().getTotemLevelTreeInvLevelLockedTaskItem());
						player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemLevelTreeInvLevelLockedTaskSound().getFirst(),
								IslandsPlugin.getConf().getTotemLevelTreeInvLevelLockedTaskSound().getSecond(),
								IslandsPlugin.getConf().getTotemLevelTreeInvLevelLockedTaskSound().getThird());
					});
				}

				levels[entry.getKey() - 1] = item;
			}

			if (IslandsPlugin.getConf().getTotemLevelTreeInvSkipFirst()) {
				levels = (ClickableItem[]) ArrayUtils.add(levels, 0, IslandsPlugin.getConf().getTotemLevelTreeInvFillerItem());
			}

			pagination.setItemsPerPage(Iterators.size(IslandsPlugin.getConf().getTotemLevelTreeInvSlotsIterator()));
			pagination.setItems(levels);
			pagination.addToIterator(contents.newIterator(SlotIterator.Type.CUSTOM, IslandsPlugin.getConf().getTotemLevelTreeInvSlotsIterator()));
		});
	}

	@Override
	public void update(Player player, InventoryContents contents) {
		this.tempItems.entrySet().stream().filter(entry -> entry.getKey() < System.currentTimeMillis()).forEach(entry -> {
			contents.set(entry.getValue().getFirst(), entry.getValue().getSecond());
			Bukkit.getScheduler().runTask(IslandsPlugin.getInstance(), () -> this.tempItems.remove(entry.getKey()));
		});
	}

}
