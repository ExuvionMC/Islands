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
import me.fragment.srefactoring.islands.config.LevelsConfig.LevelConfig;
import me.fragment.srefactoring.islands.managers.Totem;

public class TotemLevelTreeInv implements InventoryProvider {

	private Map<Long, DoublePair<SlotPos, ClickableItem>> tempItems = new HashMap<Long, DoublePair<SlotPos, ClickableItem>>();
	private Totem totem;

	public static SmartInventory openTotemLevelTreeInv(Player player, Totem totem, SmartInventory parentInv) {
		SmartInventory inv = SmartInventory.builder().manager(CorePlugin.getInventoryManager()).id("islandTotemLevelTreeGUI").provider(new TotemLevelTreeInv(totem))
				.size(IslandsPlugin.getConf().getTotemLevelTreeInvSize() / 9, 9).title(IslandsPlugin.getConf().getTotemLevelTreeInvTitle()).parent(parentInv).build();
		player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemLevelTreeInvSound().getFirst(), IslandsPlugin.getConf().getTotemLevelTreeInvSound().getSecond(),
				IslandsPlugin.getConf().getTotemLevelTreeInvSound().getThird());
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
		Pagination pagination = contents.pagination();
		contents.fill(ClickableItem.empty(IslandsPlugin.getConf().getTotemMainInvFillerItem()));

		totem.getLevel().thenAcceptBoth(totem.getMana(), (level, mana) -> {
			ClickableItem[] levels = new ClickableItem[Math.max(Iterators.size(IslandsPlugin.getConf().getTotemLevelTreeInvSlotsIterator()),
					IslandsPlugin.getLevelsConfig().getLevels().size()) + (IslandsPlugin.getConf().getTotemLevelTreeInvSkipFirst() ? 1 : 0)];

			Arrays.fill(levels, ClickableItem.empty(IslandsPlugin.getConf().getTotemLevelTreeInvFillerItem()));
			if (IslandsPlugin.getConf().getTotemLevelTreeInvSkipFirst())
				levels[0] = ClickableItem.empty(IslandsPlugin.getConf().getTotemLevelTreeInvFillerItem());

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

				levels[IslandsPlugin.getConf().getTotemLevelTreeInvSkipFirst() ? entry.getKey() : entry.getKey() - 1] = item;
			}

			pagination.setItemsPerPage(Iterators.size(IslandsPlugin.getConf().getTotemLevelTreeInvSlotsIterator()));
			pagination.setItems(levels);
			pagination.addToIterator(contents.newIterator(SlotIterator.Type.CUSTOM, IslandsPlugin.getConf().getTotemLevelTreeInvSlotsIterator()));

			contents.set(new SlotPos(IslandsPlugin.getConf().getTotemLevelTreeInvBackSlot() / 9, IslandsPlugin.getConf().getTotemLevelTreeInvBackSlot() % 9),
					ClickableItem.of(IslandsPlugin.getConf().getTotemLevelTreeInvBackItem(), click -> {
						contents.inventory().getParent().ifPresent(parentInv -> parentInv.open(player));
					}));
			if (!pagination.isLast()) {
				contents.set(new SlotPos(IslandsPlugin.getConf().getTotemLevelTreeInvNextPageSlot() / 9, IslandsPlugin.getConf().getTotemLevelTreeInvNextPageSlot() % 9),
						ClickableItem.of(IslandsPlugin.getConf().getTotemLevelTreeInvNextPageItem(), click -> {
							if (pagination.isLast()) {
								this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
										contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
								click.setCurrentItem(IslandsPlugin.getConf().getTotemLevelTreeInvNoNextPageTaskItem());
								player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemLevelTreeInvNoNextPageTaskSound().getFirst(),
										IslandsPlugin.getConf().getTotemLevelTreeInvNoNextPageTaskSound().getSecond(),
										IslandsPlugin.getConf().getTotemLevelTreeInvNoNextPageTaskSound().getThird());

								return;
							}

							this.tempItems.clear();
							contents.inventory().open(player, pagination.next().getPage());
						}));
			}
			if (!pagination.isFirst()) {
				contents.set(new SlotPos(IslandsPlugin.getConf().getTotemLevelTreeInvPreviousPageSlot() / 9, IslandsPlugin.getConf().getTotemLevelTreeInvPreviousPageSlot() % 9),
						ClickableItem.of(IslandsPlugin.getConf().getTotemLevelTreeInvPreviousPageItem(), click -> {
							if (pagination.isFirst()) {
								this.tempItems.put(System.currentTimeMillis() + 3000, new DoublePair<SlotPos, ClickableItem>(new SlotPos(click.getSlot() / 9, click.getSlot() % 9),
										contents.get(new SlotPos(click.getSlot() / 9, click.getSlot() % 9)).get()));
								click.setCurrentItem(IslandsPlugin.getConf().getTotemLevelTreeInvNoPreviousPageTaskItem());
								player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemLevelTreeInvNoPreviousPageTaskSound().getFirst(),
										IslandsPlugin.getConf().getTotemLevelTreeInvNoPreviousPageTaskSound().getSecond(),
										IslandsPlugin.getConf().getTotemLevelTreeInvNoPreviousPageTaskSound().getThird());

								return;
							}

							this.tempItems.clear();
							contents.inventory().open(player, pagination.previous().getPage());
						}));
			}
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
