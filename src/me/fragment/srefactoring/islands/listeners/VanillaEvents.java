package me.fragment.srefactoring.islands.listeners;

import org.bukkit.entity.EnderCrystal;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.fragment.srefactoring.islands.guis.TotemMainInv;
import me.fragment.srefactoring.islands.managers.Totem.TotemCrystal;

public class VanillaEvents implements Listener {

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getHand() == EquipmentSlot.HAND && TotemCrystal.entityMatch(event.getRightClicked())) {
			TotemMainInv.openTotemMainInv(event.getPlayer(), TotemCrystal.getTotem((EnderCrystal) event.getRightClicked()));
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (TotemCrystal.entityMatch(event.getEntity()))
			event.setCancelled(true);
	}

}
