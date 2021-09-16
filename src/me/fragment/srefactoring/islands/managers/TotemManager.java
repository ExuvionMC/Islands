package me.fragment.srefactoring.islands.managers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.util.Vector;

import com.bgsoftware.superiorskyblock.api.island.Island;

import co.aikar.idb.DB;
import me.fragment.srefactoring.islands.IslandsPlugin;
import me.fragment.srefactoring.islands.managers.Totem.TotemCrystal;

public class TotemManager {

	private Map<String, Vector> totemVectors = new HashMap<String, Vector>();

	public TotemManager() {
		DB.getResultsAsync("SELECT * FROM `upgrades`").thenAccept(results -> {
			IslandsPlugin.getUpgradesConfig().getUpgradesConfig().stream()
					.filter(upgrade -> results.stream().map(row -> row.getString("name")).noneMatch(name -> name.equals(upgrade.getName()))).forEach(upgrade -> {
						DB.executeUpdateAsync("INSERT INTO `upgrades`(`name`) VALUES (?)", upgrade.getName());
					});
		});
	}

	public Totem createTotem(Island island) {
		Totem totem = new Totem(island.getUniqueId());
		TotemCrystal totemCrystal = new Totem.TotemCrystal(island.getCenter(Environment.NORMAL).subtract(getTotemVector(island, island.getSchematicName())).add(0, 0.5, 0), totem)
				.summon();

		totem.setTotemCrystal((EnderCrystal) totemCrystal.getBukkitEntity());
		new Thread(() -> {
			try {
				totem.setId(DB.executeInsert("INSERT INTO `totem`(`uuid`) VALUES (?)", totem.getUUIDIsland().toString()));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}).start();

		return totem;
	}

	public void deleteTotem(Island island) {
		new Totem(island.getUniqueId()).getTotemCrystal().remove();
		DB.executeUpdateAsync("DELETE FROM `totem` WHERE `uuid` = ?", island.getUniqueId().toString());
	}

	public Vector getTotemVector(Island island, String schematic) {
		if (!this.totemVectors.containsKey(schematic)) {
			island.getAllChunks(Environment.NORMAL, true, true).stream()
					.map(chunk -> IntStream.range(0, 15).boxed()
							.map(x -> IntStream.range(0, 15).boxed().map(z -> IntStream.range(0, 255).boxed().map(y -> chunk.getBlock(x, y, z))).flatMap(Function.identity()))
							.flatMap(Function.identity()))
					.flatMap(Function.identity()).filter(block -> block.getType() == Material.COAL_BLOCK).findFirst()
					.ifPresentOrElse(block -> this.totemVectors.put(schematic, island.getCenter(Environment.NORMAL).toVector().subtract(block.getLocation().toVector())),
							() -> this.totemVectors.put(schematic, null));
		}

		return this.totemVectors.get(schematic);
	}

}
