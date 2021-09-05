package me.fragment.srefactoring.islands.guis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.fragment.smartinv.ClickableItem;
import me.fragment.smartinv.SmartInventory;
import me.fragment.smartinv.content.InventoryContents;
import me.fragment.smartinv.content.InventoryProvider;
import me.fragment.smartinv.content.SlotPos;
import me.fragment.srefactoring.core.CorePlugin;
import me.fragment.srefactoring.core.utils.DoublePair;
import me.fragment.srefactoring.core.utils.Utils;
import me.fragment.srefactoring.islands.IslandsPlugin;
import me.fragment.srefactoring.islands.managers.Totem;

public class TotemMainInv implements InventoryProvider {

	private Map<Long, DoublePair<SlotPos, ClickableItem>> tempItems = new HashMap<Long, DoublePair<SlotPos, ClickableItem>>();
	private Totem totem;

	public static SmartInventory openTotemMainInv(Player player, Totem totem) {
		SmartInventory inv = SmartInventory.builder().manager(CorePlugin.getInventoryManager()).id("islandTotemMainGUI").provider(new TotemMainInv(totem))
				.size(IslandsPlugin.getConf().getTotemMainInvSize() / 9, 9).title(IslandsPlugin.getConf().getTotemMainInvTitle()).build();
		player.playSound(player.getLocation(), IslandsPlugin.getConf().getTotemMainInvSound().getFirst(), IslandsPlugin.getConf().getTotemMainInvSound().getSecond(),
				IslandsPlugin.getConf().getTotemMainInvSound().getThird());
		inv.open(player);

		return inv;
	}

	public TotemMainInv(Totem totem) {
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
		contents.fill(ClickableItem.empty(IslandsPlugin.getConf().getTotemMainInvFillerItem()));

		totem.getLevel().thenAcceptBoth(totem.getMana(), (level, mana) -> {
			contents.set(new SlotPos(IslandsPlugin.getConf().getTotemMainInvTotemSlot() / 9, IslandsPlugin.getConf().getTotemMainInvTotemSlot() % 9), ClickableItem.of(
					Utils.applyPlaceholders(IslandsPlugin.getConf().getTotemMainInvTotemItem(),
							Arrays.asList(new DoublePair<String, String>("%level%", Utils.longFormatter(level)), new DoublePair<String, String>("%mana%", Utils.longFormatter(mana)))),
					click -> {
						TotemLevelTreeInv.openTotemMainInv(player, totem);
					}));
		});
	}

	@Override
	public void update(Player player, InventoryContents contents) {

	}

}
