package de.potera.rysefoxx.bossegg.listener;

import de.potera.rysefoxx.bossegg.BossEgg;
import de.potera.rysefoxx.bossegg.BossEggSerializer;
import de.potera.rysefoxx.utils.RandomCollection;
import de.potera.teamhardcore.Main;
import de.potera.teamhardcore.utils.Util;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BossDeathListener implements Listener {

    @EventHandler
    public void on(EntityDeathEvent e) {
        LivingEntity livingEntity = e.getEntity();

        if (livingEntity.hasMetadata("BOSS")) {
            for (MetadataValue string : livingEntity.getMetadata("BOSS")) {

                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forID(string.toString());

                e.setDroppedExp(0);
                e.getDrops().clear();

                livingEntity.getPassenger().remove();

                livingEntity.getEquipment().setBootsDropChance(0);
                livingEntity.getEquipment().setChestplateDropChance(0);
                livingEntity.getEquipment().setHelmetDropChance(0);
                livingEntity.getEquipment().setLeggingsDropChance(0);
                livingEntity.getEquipment().setItemInHandDropChance(0);

                livingEntity.getLocation().getWorld().playEffect(livingEntity.getLocation(), Effect.EXPLOSION_HUGE, 0, 5);
                livingEntity.getLocation().getWorld().playSound(livingEntity.getLocation(), Sound.EXPLODE, 5, 5);

                List<String> ids = Main.getPlugin(Main.class).getBossEggManager().getLivingBosses().get(bossEgg);
                ids.remove(bossEgg.getId());
                Main.getPlugin(Main.class).getBossEggManager().getLivingBosses().put(bossEgg, ids);


                if (bossEgg.getItems().isEmpty()) {
                    Main.getInstance().getLogger().warning("Boss " + bossEgg.getDisplayName() + " has no rewards");
                    return;
                }

                RandomCollection<BossEggSerializer> reward = new RandomCollection<>();
                for (BossEggSerializer bossEggSerializer : bossEgg.getItems()) {
                    reward.add(bossEggSerializer.getChance(), bossEggSerializer);
                }

                List<BossEggSerializer> items = new ArrayList<>();
                for (int i = 0; i < reward.map.size() * 150; ++i) {
                    items.add(reward.next().entry);
                }

                Collections.shuffle(items);
                int rewardAmount = Util.randInt(bossEgg.getMinDropAmount(), bossEgg.getMaxDropAmount());

                for (int i = 0; i < rewardAmount; i++) {
                    BossEggSerializer bossEggSerializer = items.get(new Random().nextInt(items.size()));
                    livingEntity.getLocation().getWorld().dropItem(livingEntity.getLocation(), bossEggSerializer.getItemStack());
                    bossEggSerializer.setAmount(bossEggSerializer.getAmount() + 1);
                }
            }
        }

    }

}
