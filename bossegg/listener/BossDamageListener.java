package de.potera.rysefoxx.bossegg.listener;

import de.potera.rysefoxx.bossegg.BossEgg;
import de.potera.teamhardcore.Main;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;

public class BossDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && !(e.getEntity() instanceof Player)) {
            LivingEntity livingEntity = (LivingEntity) e.getEntity();

            if (livingEntity.hasMetadata("BOSS")) {
                for (MetadataValue string : livingEntity.getMetadata("BOSS")) {
                    BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forID(string.toString());
                    livingEntity.getPassenger().setCustomName(bossEgg.getHoloText().replace("%health%", String.valueOf(Math.round(livingEntity.getHealth()))).replace("%maxHealth%", String.valueOf(Math.round(livingEntity.getMaxHealth()))));
                }
            }
        } else if (!(e.getDamager() instanceof Player)) {
            e.setDamage(0);
        }


    }

}
