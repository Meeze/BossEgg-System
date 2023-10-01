package de.potera.rysefoxx.bossegg;

import de.potera.rysefoxx.menubuilder.manager.InventoryMenuBuilder;
import de.potera.teamhardcore.Main;
import de.potera.teamhardcore.utils.ItemBuilder;
import de.potera.teamhardcore.utils.StringDefaults;
import de.potera.teamhardcore.utils.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;

public class BossEggCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("potera.bossegg")) {
            player.sendMessage(StringDefaults.NO_PERM);
            return true;
        }

        /*

        /BossEgg <Create> <EggName>
        /BossEgg <Delete> <EggName>
        /BossEgg List           // ALLE MÖGLICHEN BOSSEGGS AUFLISTEN
        /BossEgg setMaxHealth <EggName> <Health>
        /BossEgg addItem <EggName> <Prozent> <Broadcast>
        /BossEgg withBroadcast <EggName> <Boolean>
        /BossEgg items <EggName>
        /BossEgg get <BossEgg>
        /BossEgg setType <Bossegg> <EntityType>
        /BossEgg setHelmet <BossEgg>
        /BossEgg setChestPlate <BossEgg>
        /BossEgg setLeggings <BossEgg>
        /BossEgg setBoots <BossEgg>
        /BossEgg setWeapon <BossEgg>
        /BossEgg setHolo <BossEgg> <String>
        /BossEgg setMinDropAmount <Bossegg> <Amount>
        /BossEgg setMaxDropAmount <Bossegg> <Amount>
        /BossEgg setAnnouncement <BossEgg> <Boolean>
        /BossEgg setCollection <BossEgg> <Integer>

        /BossEgg spawn <Bossegg> <X> <Y> <Z>


         */

        if (!Main.getPlugin(Main.class).getBossEggManager().isAccessible()) {
            player.sendMessage(StringDefaults.PREFIX + "§cBitte warte bis alle BossEggs geladen wurden.");
            return true;
        }
        if (args.length >= 3 && args[0].equalsIgnoreCase("setHolo")) {
            if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                return true;
            }
            BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
            bossEgg.setHoloText(ChatColor.translateAlternateColorCodes('&', Util.stringBuilder(2, args)));
            bossEgg.save();
            player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das Hologram für §c" + args[1] + " §7geupdatet.");
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setMinDropAmount")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (!Util.isInt(args[2])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte gib eine gültige Zahl an.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.setMinDropAmount(Integer.parseInt(args[2]));
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "Es dropppen nun mindestens §c" + args[2] + " Items");
            } else if (args[0].equalsIgnoreCase("setMaxDropAmount")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (!Util.isInt(args[2])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte gib eine gültige Zahl an.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.setMaxDropAmount(Integer.parseInt(args[2]));
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Es dropppen nun maximal §c" + args[2] + " Items");

            } else if (args[0].equalsIgnoreCase("addItem")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte halte ein Item in der Hand.");
                    return true;
                }
                if (!Util.isDouble(args[2])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte gebe eine gültige Prozentzahl an.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                BossEggSerializer bossEggSerializer = new BossEggSerializer(Double.parseDouble(args[2]), Util.getCustomName(player.getItemInHand()), player.getItemInHand());

                bossEgg.getItems().add(bossEggSerializer);
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast erfolgreich das Item in deiner Hand zum BossEgg §c" + args[1] + " §7hinzugefügt.");
            } else if (args[0].equalsIgnoreCase("setType")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (EntityType.fromName(args[2]) == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Ungültiges Entity.");
                    return true;
                }
                EntityType entityType = EntityType.fromName(args[2]);
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.setEntityType(entityType);
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§c" + args[1] + " §7erscheint nun als §c" + entityType.getName());
            } else if (args[0].equalsIgnoreCase("setCollection")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                int collection;
                try {
                    collection = Integer.parseInt(args[2]);
                } catch (NumberFormatException exception) {
                    player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Bitte gib eine gültige Zahl an");
                    return true;
                }
                bossEgg.setCollection(collection);
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§c" + args[1] + " §7ist nun aus der §c" + args[2] + " Kollektion.");
            } else if (args[0].equalsIgnoreCase("setAnnouncement")) {

                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                boolean broadcast;
                if (args[2].equalsIgnoreCase("true")) {
                    broadcast = true;
                } else if (args[2].equalsIgnoreCase("false")) {
                    broadcast = false;
                } else {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte gebe einen richtigen Boolean an. (True,False)");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.setBroadcastOnSpawn(broadcast);
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + (broadcast ? "§7Im Chat wird nun angezeigt wo der Boss sich befindet" : "§7Im Chat wird nicht mehr angezeigt wo der Boss ist."));
            } else if (args[0].equalsIgnoreCase("withBroadcast")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                boolean broadcast;
                if (args[2].equalsIgnoreCase("true")) {
                    broadcast = true;
                } else if (args[2].equalsIgnoreCase("false")) {
                    broadcast = false;
                } else {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte gebe einen richtigen Boolean an. (True,False)");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.setBroadcast(broadcast);
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Die Items die droppen werden " + (broadcast ? "§cnun an alle Spieler gesendet" : "§cnun nicht mehr an alle Spieler gesendet") + ".");

            } else if (args[0].equalsIgnoreCase("setMaxHealth")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (!Util.isInt(args[2])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Gebe eine gültige Zahl an.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.setMaxHealth(Integer.parseInt(args[2]));
                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§c" + args[1] + " §7startet nun mit §c" + Util.formatBigNumber(bossEgg.getMaxHealth()) + " Herzen");
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("spawn")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                int x;
                int y;
                int z;
                try {
                    x = Integer.parseInt(args[2]);
                    y = Integer.parseInt(args[3]);
                    z = Integer.parseInt(args[4]);
                } catch (Exception exception) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Gib eine gültige Koordinate an. (Ganze Zahl)");
                    return true;
                }

                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.spawn(player, x, y, z);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("setHelmet")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (player.getItemInHand() == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte halte ein Item in der Hand.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                Main.getPlugin(Main.class).getBossEggManager().updateHelmet(bossEgg, player.getItemInHand());
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das Rüstungsteil §cHELM §7erfolgreich verändert.");
            } else if (args[0].equalsIgnoreCase("setChestPlate")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (player.getItemInHand() == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte halte ein Item in der Hand.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                Main.getPlugin(Main.class).getBossEggManager().updateChestPlate(bossEgg, player.getItemInHand());
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das Rüstungsteil §cBRUST §7erfolgreich verändert.");
            } else if (args[0].equalsIgnoreCase("setLeggings")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (player.getItemInHand() == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte halte ein Item in der Hand.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                Main.getPlugin(Main.class).getBossEggManager().updateLeggings(bossEgg, player.getItemInHand());
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das Rüstungsteil §cHOSE §7erfolgreich verändert.");
            } else if (args[0].equalsIgnoreCase("setBoots")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (player.getItemInHand() == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte halte ein Item in der Hand.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                Main.getPlugin(Main.class).getBossEggManager().updateBoots(bossEgg, player.getItemInHand());
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das Rüstungsteil §cSCHUHE §7erfolgreich verändert.");
            } else if (args[0].equalsIgnoreCase("setWeapon")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                if (player.getItemInHand() == null) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Bitte halte ein Item in der Hand.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                Main.getPlugin(Main.class).getBossEggManager().updateWeapon(bossEgg, player.getItemInHand());
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast die Waffe erfolgreich verändert.");
            } else if (args[0].equalsIgnoreCase("get")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                player.getInventory().addItem(bossEgg.getItemStack().clone());
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das BossEgg für den Boss §c" + args[1] + " §7erhalten");
            } else if (args[0].equalsIgnoreCase("items")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                InventoryMenuBuilder inventoryMenuBuilder = new InventoryMenuBuilder().withSize(9 * 6).withTitle("§7Alle Drops vom BossEgg");

                int index = 0;
                for (BossEggSerializer bossEggSerializer : bossEgg.getItems()) {
                    if (index >= 53) break;
                    inventoryMenuBuilder.withItem(index, new ItemBuilder(bossEggSerializer.getItemStack().clone()).setDisplayName("§c" + bossEggSerializer.getDisplayName()).setLore(Arrays.asList(
                            "§7DisplayName §8➡ §c" + bossEggSerializer.getDisplayName(),
                            "§7ID §8➡ §c" + bossEggSerializer.getId(),
                            "§7Chance §8➡ §c" + bossEggSerializer.getChance() + "%",
                            "",
                            "§7Das Item ist schon §c" + Util.formatBigNumber(bossEggSerializer.getAmount()) + "x §7gedroppt.",
                            "",
                            "§7Linksklick §8- §eItem entfernen")).build(), (player1, action, item) -> {
                        bossEgg.getItems().remove(bossEggSerializer);
                        bossEgg.save();
                        player1.chat("/bossegg items " + args[1]);
                        player1.sendMessage(StringDefaults.BOSSEGG_PREFIX + "Du hast das Item erfolgreich entfernt.");
                    }, ClickType.LEFT);
                    index++;
                }

                inventoryMenuBuilder.show(player);
            } else if (args[0].equalsIgnoreCase("create")) {
                if (Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert bereits.");
                    return true;
                }
                BossEgg bossEgg = new BossEgg(args[1]);
                Main.getPlugin(Main.class).getBossEggManager().getBossEggList().add(bossEgg);


                bossEgg.setItemStack(new ItemBuilder(Material.DRAGON_EGG).setDisplayName("§6§lSpawn-Ei §8- §c§l" + bossEgg.getDisplayName()).setLore(Arrays.asList(
                        "§7Klicke mit dem Spawn-Ei auf ein Block",
                        "§7um den Boss §c§l" + bossEgg.getDisplayName() + " §7zu spawnen.",
                        "",
                        "§7Beim Spawnen verwendest du das Spawn-Ei und beschwörst",
                        "§7den Boss. Der Boss wird je nach Schwierigkeit Items droppen.",
                        "§7Dieser Boss droppt zwischen §c" + bossEgg.getMinDropAmount() + "§8-§c" + bossEgg.getMaxDropAmount() + " Items",
                        "§7Die Items haben je nach §c§lBesonderheit eine §akleine Dropchance",
                        "§7oder eine §ahohe Dropchance.",
                        "",
                        "§7Dieses Spawn-Ei stammt aus der §c§l" + bossEgg.getCollection() + " §7Kollektion.",
                        "§cBeim Spawnen werden die Koordinaten im Chat gepostet.",
                        "",
                        "§7Linksklick §8- §eMögliche Items anzeigen",
                        "§7Rechtsklick §8- §eBoss spawnen")).build());

                bossEgg.save();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das BossEgg §c" + args[1] + " §7erfolgreich erstellt.");
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (!Main.getPlugin(Main.class).getBossEggManager().alreadyExists(args[1])) {
                    player.sendMessage(StringDefaults.PREFIX + "§7Dieses BossEgg existiert nicht.");
                    return true;
                }
                BossEgg bossEgg = Main.getPlugin(Main.class).getBossEggManager().forName(args[1]);
                bossEgg.delete();
                player.sendMessage(StringDefaults.BOSSEGG_PREFIX + "§7Du hast das BossEgg §c" + args[1] + " §7erfolgreich gelöscht,");
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {

                Main.getPlugin(Main.class).getBossEggManager().help(player);
            } else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("liste")) {

                InventoryMenuBuilder inventoryMenuBuilder = new InventoryMenuBuilder().withSize(9 * 6).withTitle("§7Alle BossEggs");

                int index = 0;
                for (BossEgg bossEgg : Main.getPlugin(Main.class).getBossEggManager().getBossEggList()) {
                    if (index >= 53) break;
                    inventoryMenuBuilder.withItem(index, new ItemBuilder(bossEgg.getItemStack().clone()).setDisplayName("§cBossEgg §8- §6" + bossEgg.getDisplayName()).setLore(Arrays.asList(
                            "§7Dieser BossEgg hat §c" + bossEgg.getItems().size() + " §7verfügbare Items",
                            "§7Diese Items können je nach ihrer Wahrscheinlichkeit, beim Tod gedroppt werden.",
                            "§7Dieser BossEgg startet mit §c" + Util.formatBigNumber(bossEgg.getMaxHealth()) + " Herzen.",
                            "§7Es werden mindestens §c" + bossEgg.getMinDropAmount() + " Items gedroppt.",
                            "§7Es werden maximal §c" + bossEgg.getMaxDropAmount() + " Items gedroppt.")).build());
                    index++;
                }
                inventoryMenuBuilder.withEventHandler(event -> event.setCancelled(true));
                inventoryMenuBuilder.show(player);
            }
        } else {
            Main.getPlugin(Main.class).getBossEggManager().help(player);
        }


        return false;
    }

}
