package me.fragment.srefactoring.islands.managers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World.Environment;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;

import co.aikar.idb.DB;
import me.fragment.srefactoring.core.CustomEntity;
import me.fragment.srefactoring.core.persistentDataTypes.UUIDDataType;
import me.fragment.srefactoring.core.utils.Utils;
import me.fragment.srefactoring.islands.IslandsPlugin;
import me.fragment.srefactoring.islands.config.LevelsConfig.LevelConfig;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.world.entity.boss.enderdragon.EntityEnderCrystal;

public class Totem {

	private Island island;
	private EnderCrystal totemCrystal;

	private Long id;
	private UUID uuid;
	private Integer level;
	private Integer mana;
	private Integer upPoints;
	private List<String> unlockedUpgrades;

	public Totem(UUID islandUUID) {
		this.uuid = islandUUID;
		this.island = SuperiorSkyblockAPI.getIslandByUUID(this.uuid);
		this.totemCrystal = (EnderCrystal) this.island.getAllChunks(Environment.NORMAL, false, false).stream().map(Chunk::getEntities).flatMap(entities -> Stream.of(entities))
				.filter(entity -> TotemCrystal.entityMatch(entity)).findFirst().orElse(null);
	}

	public Island getIsland() {
		return island;
	}

	public EnderCrystal getTotemCrystal() {
		return totemCrystal;
	}

	public void setTotemCrystal(EnderCrystal totemCrystal) {
		this.totemCrystal = totemCrystal;
	}

	public CompletableFuture<LevelConfig> getLevelConfig() {
		CompletableFuture<LevelConfig> future = new CompletableFuture<LevelConfig>();

		getLevel().thenAccept(level -> future.complete(IslandsPlugin.getLevelsConfig().getLevels().get(level)));

		return future;
	}

	public CompletableFuture<Long> getId() {
		CompletableFuture<Long> future = new CompletableFuture<Long>();

		if (this.id != null) {
			future.complete(this.id);
		} else {
			DB.getFirstRowAsync("SELECT `id` FROM `totem` WHERE `uuid` = ?", this.uuid.toString()).thenAccept(result -> {
				this.id = result.getLong("id");
				future.complete(this.id);
			});
		}

		return future;
	}

	public void setId(Long id) {
		this.id = id;
		DB.executeUpdateAsync("UPDATE `totem` SET `id`=? WHERE `uuid` = ?", this.id, this.uuid.toString());
	}

	public UUID getUUIDIsland() {
		return uuid;
	}

	public CompletableFuture<Integer> getLevel() {
		CompletableFuture<Integer> future = new CompletableFuture<Integer>();

		if (this.level != null) {
			future.complete(this.level);
		} else {
			DB.getFirstRowAsync("SELECT `level` FROM `totem` WHERE `uuid` = ?", this.uuid.toString()).thenAccept(result -> {
				this.level = result.getInt("level");
				future.complete(this.level);
			});
		}

		return future;
	}

	public void setLevel(int level) {
		this.level = level;
		DB.executeUpdateAsync("UPDATE `totem` SET `level`=? WHERE `uuid` = ?", this.level, this.uuid.toString());
	}

	public CompletableFuture<Integer> getMana() {
		CompletableFuture<Integer> future = new CompletableFuture<Integer>();

		if (this.mana != null) {
			future.complete(this.mana);
		} else {
			DB.getFirstRowAsync("SELECT `mana` FROM `totem` WHERE `uuid` = ?", this.uuid.toString()).thenAccept(result -> {
				this.mana = result.getInt("mana");
				future.complete(this.mana);
			});
		}

		return future;
	}

	public void setMana(int mana) {
		this.mana = mana;
		DB.executeUpdateAsync("UPDATE `totem` SET `uppoints`=? WHERE `uuid` = ?", this.mana, this.uuid.toString());
	}

	public CompletableFuture<Integer> getUpPoints() {
		CompletableFuture<Integer> future = new CompletableFuture<Integer>();

		if (this.upPoints != null) {
			future.complete(this.upPoints);
		} else {
			DB.getFirstRowAsync("SELECT `uppoints` FROM `totem` WHERE `uuid` = ?", this.uuid.toString()).thenAccept(result -> {
				this.upPoints = result.getInt("uppoints");
				future.complete(this.upPoints);
			});
		}

		return future;
	}

	public void setUpPoints(int upPoints) {
		this.upPoints = upPoints;
		DB.executeUpdateAsync("UPDATE `totem` SET `mana`=? WHERE `uuid` = ?", this.upPoints, this.uuid.toString());
	}

	public CompletableFuture<List<String>> getUnlockedUgrades(Totem totem) {
		CompletableFuture<List<String>> future = new CompletableFuture<List<String>>();

		if (this.unlockedUpgrades != null) {
			future.complete(this.unlockedUpgrades);
		} else {
			DB.getResultsAsync(
					"SELECT upgrades.name FROM upgrades JOIN totemUpgrades ON totemUpgrades.idUpgrade = upgrades.id JOIN totem ON totem.id = totemUpgrades.idTotem WHERE totem.uuid = ?",
					this.uuid.toString()).thenAccept(rows -> {
						List<String> upgrades = rows.stream().map(row -> row.getString("name")).collect(Collectors.toList());
						this.unlockedUpgrades = upgrades;
						future.complete(upgrades);
					});
		}

		return future;
	}

	public void unlockedUpgrade(String upgrade) {
		this.unlockedUpgrades.add(upgrade);
		DB.executeUpdateAsync("SELECT `id` FROM `upgrades` WHERE name = ?", upgrade)
				.thenAccept(upgradeId -> DB.executeUpdateAsync("INSERT INTO `totemUpgrades`(`idTotem`, `idUpgrade`) VALUES (%,%)", this.id, upgradeId));
	}

	public static class TotemCrystal extends EntityEnderCrystal implements CustomEntity {

		private Location location;
		private Totem totem;

		public TotemCrystal(Location location, Totem totem) {
			super(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
			this.location = location;
			this.totem = totem;

			this.setPosition(location.getX() + 0.5D, location.getY() + 1, location.getZ() + 0.5D);
			this.setInvulnerable(true);
			this.setShowingBottom(false);
			this.setCustomNameVisible(true);
			this.setCustomName(new ChatComponentText(IslandsPlugin.getConf().getCrystalTotemName().replace("%level%", Utils.IntegerToRomanNumeral(1))));
		}

		public TotemCrystal summon() {
			((CraftWorld) this.location.getWorld()).getHandle().addEntity(this);
			this.getBukkitEntity().getPersistentDataContainer().set(new NamespacedKey(IslandsPlugin.getInstance(), "customEntity"), PersistentDataType.STRING, "TotemCrystal");
			this.getBukkitEntity().getPersistentDataContainer().set(new NamespacedKey(IslandsPlugin.getInstance(), "islandUUID"), new UUIDDataType(), this.totem.getUUIDIsland());
			return this;
		}

		public static boolean entityMatch(Entity entity) {
			return entity.getPersistentDataContainer().has(new NamespacedKey(IslandsPlugin.getInstance(), "customEntity"), PersistentDataType.STRING)
					&& entity.getPersistentDataContainer().get(new NamespacedKey(IslandsPlugin.getInstance(), "customEntity"), PersistentDataType.STRING).equals("TotemCrystal");
		}

		public static Totem getTotem(EnderCrystal entity) {
			return new Totem(entity.getPersistentDataContainer().get(new NamespacedKey(IslandsPlugin.getInstance(), "islandUUID"), new UUIDDataType()));
		}

	}

}
