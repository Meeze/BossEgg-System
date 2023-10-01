package de.potera.rysefoxx.bossegg;

import de.potera.teamhardcore.Main;
import de.potera.teamhardcore.utils.ItemBuilder;
import de.potera.teamhardcore.utils.StringDefaults;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter

public class BossEgg {

    private String displayName;
    private List<BossEggSerializer> items;
    private ItemStack itemStack;
    private int maxHealth;
    private boolean broadcast;
    private EntityType entityType;
    private ItemStack helmet;
    private ItemStack chestPlate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack itemInHand;
    private String holoText;
    private LivingEntity entity;
    private int minDropAmount;
    private int maxDropAmount;
    private String id;
    private int collection;
    private boolean broadcastOnSpawn;


    public BossEgg(String displayName) {
        this.displayName = displayName;
        this.items = new ArrayList<>();
        this.itemStack = new ItemBuilder(Material.BARRIER).setDisplayName("§c§oKein Egg(Design) gefunden").setLore(Collections.singletonList(
                "§7Benutze /BossEgg setEgg <EggName>")).build();
        this.maxHealth = 100;
        this.broadcast = false;
        this.entityType = EntityType.ZOMBIE;
        this.helmet = new ItemBuilder(Material.AIR).build();
        this.chestPlate = new ItemBuilder(Material.AIR).build();
        this.leggings = new ItemBuilder(Material.AIR).build();
        this.boots = new ItemBuilder(Material.AIR).build();
        this.itemInHand = new ItemBuilder(Material.AIR).build();
        this.holoText = "";
        this.entity = null;
        this.minDropAmount = 1;
        this.maxDropAmount = 4;
        this.id = "";
        this.collection = 1;
        this.broadcastOnSpawn = false;
    }

    public void addPotionEffect(PotionEffectType type, int time, int power) {
        if (this.entity == null) return;
        this.entity.addPotionEffect(new PotionEffect(type, time, power));
    }

    public void spawn(Player player, int x, int y, int z) {
        if (this.entityType == null) {
            Main.getInstance().getLogger().warning("Could not spawn Boss! EntityType is null.");
            player.sendMessage(StringDefaults.PREFIX + "§7Es ist ein Fehler aufgetreten.");
            return;
        }
        Location location = new Location(player.getWorld(), x, y, z);
        boolean wait = false;
        if (!location.getChunk().isLoaded()) {
            location.getChunk().load();
            wait = true;
        }
        long waitingTime = System.currentTimeMillis() + 5000L;
        long systemTime = System.currentTimeMillis();

        if (wait) {
            while (waitingTime > systemTime) {
                waitingTime -= 1000;
            }
            if (waitingTime < systemTime) {
                createEntity(player, location);
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Boss wurde erfolgreich gespawnt.");
            }
        } else {
            createEntity(player, location);
            player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Boss wurde erfolgreich gespawnt.");

        }


    }

    public void createEntity(Player player, Location location) {
        LivingEntity livingEntity = (LivingEntity) player.getWorld().spawnEntity(location, this.entityType);
        Entity entity = player.getWorld().spawnEntity(livingEntity.getLocation(), EntityType.ARMOR_STAND);

        ArmorStand as = (ArmorStand) entity;

        as.setGravity(false);
        as.setVisible(false);
        as.setSmall(true);


        this.entity = livingEntity;
        this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        List<String> allIds;
        if (Main.getPlugin(Main.class).getBossEggManager().getLivingBosses().containsKey(this)) {
            allIds = Main.getPlugin(Main.class).getBossEggManager().getLivingBosses().get(this);
        } else {
            allIds = new ArrayList<>();
        }
        allIds.add(this.id);
        Main.getPlugin(Main.class).getBossEggManager().getLivingBosses().put(this, allIds);

        livingEntity.setMetadata("BOSS", new FixedMetadataValue(Main.getPlugin(Main.class), this.id));
        livingEntity.setMaxHealth(this.maxHealth);
        livingEntity.setHealth(this.maxHealth);
        livingEntity.getEquipment().setHelmet(this.helmet);
        livingEntity.getEquipment().setChestplate(this.chestPlate);
        livingEntity.getEquipment().setLeggings(this.leggings);
        livingEntity.getEquipment().setBoots(this.boots);
        livingEntity.getEquipment().setItemInHand(this.itemInHand);

        as.setCustomName(holoText.replace("%health%", String.valueOf(livingEntity.getHealth())).replace("%maxHealth%", String.valueOf(livingEntity.getMaxHealth())));
        as.setCustomNameVisible(true);


        livingEntity.setPassenger(as);


    }

    public void save() {
        for (BossEgg bossEgg : Main.getPlugin(Main.class).getBossEggManager().getBossEggList()) {
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".items", bossEgg.getItems());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".itemStack", bossEgg.getItemStack());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".maxHealth", bossEgg.getMaxHealth());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".broadcast", bossEgg.isBroadcast());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".entityType", bossEgg.getEntityType().getName());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".helmet", bossEgg.getHelmet());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".chestPlate", bossEgg.getChestPlate());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".leggings", bossEgg.getLeggings());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".boots", bossEgg.getBoots());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".itemInHand", bossEgg.getItemInHand());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".holoText", bossEgg.getHoloText());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".minDropAmount", bossEgg.getMinDropAmount());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".maxDropAmount", bossEgg.getMaxDropAmount());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".collection", bossEgg.getCollection());
            Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(bossEgg.getDisplayName() + ".broadcastOnSpawn", bossEgg.isBroadcastOnSpawn());
        }
        Main.getPlugin(Main.class).getBossEggManager().getConfig().saveConfig();
    }

    public void delete() {
        Main.getPlugin(Main.class).getBossEggManager().getBossEggList().remove(this);
        Main.getPlugin(Main.class).getBossEggManager().getConfig().getConfig().set(this.displayName, null);
        Main.getPlugin(Main.class).getBossEggManager().getConfig().saveConfig();
    }


}
