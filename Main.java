package de.potera.teamhardcore;

import de.potera.rysefoxx.worldguard.WorldGuardListener;
import de.potera.rysefoxx.bossegg.BossEggCommand;
import de.potera.rysefoxx.bossegg.BossEggManager;
import de.potera.rysefoxx.bossegg.BossEggSerializer;
import de.potera.rysefoxx.bossegg.listener.BossDamageListener;
import de.potera.rysefoxx.bossegg.listener.BossDeathListener;
import de.potera.rysefoxx.bossegg.listener.BossInteractListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;


public class Main extends JavaPlugin {


    private BossEggManager bossEggManager;

    @Override
    public void onDisable() {
        if (shutdownDirectly) return;

        this.databaseManager.terminate();
        this.amsManager.onDisable();
        this.fakeEntityManager.onDisable();
        this.scoreboardManager.onDisable();
        this.clanManager.onDisable();
        //this.userManager.onDisable();
        this.rankingManager.onDisable();
        this.combatManager.onDisable();
        this.dailyPotManager.onDisable();


        this.bossEggManager.forceEnd();
    }


    private void registerAll() {
        this.fileManager = new FileManager();
        this.databaseManager = new DatabaseManager();

        if (!this.databaseManager.init()) {
            LogManager.getLogger(Main.class).warn("Datenbankverbindung konnte nicht aufgebaut werden!");
            shutdownDirectly();
            return;
        }

        ConfigurationSerialization.registerClass(BossEggSerializer.class);

        this.userManager = new UserManager();
        this.generalManager = new GeneralManager();
        this.warpManager = new WarpManager();
        this.antilagManager = new AntilagManager();
        this.shopManager = new ShopManager();
        this.scoreboardManager = new ScoreboardManager();
        this.fakeEntityManager = new FakeEntityManager();
        this.amsManager = new AmsManager();
        this.minesManager = new MineManager();
        this.supportManager = new SupportManager();
        this.reportManager = new ReportManager();
        this.crateManager = new CrateManager();
        this.rouletteManager = new RouletteManager();
        this.clanManager = new ClanManager();
        this.kitManager = new KitManager();
        this.coinflipManager = new CoinflipManager();
        this.rankingManager = new RankingManager();
        this.jackpotManager = new JackpotManager();
        this.combatManager = new CombatManager();
        this.punishmentController = new PunishmentController();
        this.punishmentController.loadFromDatabase();
        this.dailyPotManager = new DailyPotManager();
        this.tradeManager = new TradeManager();
        this.bossEggManager = new BossEggManager();

        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BossInteractListener(), this);
        pm.registerEvents(new BossDamageListener(), this);
        pm.registerEvents(new BossDeathListener(), this);
        pm.registerEvents(new WorldGuardListener(), this);

        getCommand("clear").setExecutor(new CommandClear());
        getCommand("broadcast").setExecutor(new CommandBroadcast());
        getCommand("chatclear").setExecutor(new CommandChatclear());
        getCommand("rename").setExecutor(new CommandRename());
        getCommand("relore").setExecutor(new CommandRelore());
        getCommand("bodysee").setExecutor(new CommandBodysee());
        getCommand("fakemessage").setExecutor(new CommandFakemessage());
        getCommand("feed").setExecutor(new CommandFeed());
        getCommand("fly").setExecutor(new CommandFly());
        getCommand("gamemode").setExecutor(new CommandGamemode());
        getCommand("stack").setExecutor(new CommandStack());
        getCommand("list").setExecutor(new CommandList());
        getCommand("more").setExecutor(new CommandMore());
        getCommand("teamchat").setExecutor(new CommandTeamchat());
        getCommand("tpa").setExecutor(new CommandTpa());
        getCommand("tpall").setExecutor(new CommandTpa());
        getCommand("tphere").setExecutor(new CommandTpa());
        getCommand("tptop").setExecutor(new CommandTpa());
        getCommand("warp").setExecutor(new CommandWarp());
        getCommand("spawn").setExecutor(new CommandSpawn());
        getCommand("fix").setExecutor(new CommandFix());
        getCommand("clearlag").setExecutor(new CommandClearlag());
        getCommand("god").setExecutor(new CommandGod());
        getCommand("gc").setExecutor(new CommandGc());
        getCommand("workbench").setExecutor(new CommandWorkbench());
        getCommand("sudo").setExecutor(new CommandSudo());
        getCommand("giveall").setExecutor(new CommandGiveall());
        getCommand("messagespy").setExecutor(new CommandMessagespy());
        getCommand("kill").setExecutor(new CommandKill());
        getCommand("random").setExecutor(new CommandRandom());
        getCommand("time").setExecutor(new CommandTime());
        getCommand("msg").setExecutor(new CommandMsg());
        getCommand("back").setExecutor(new CommandBack());
        getCommand("commandspy").setExecutor(new CommandCommandspy());
        getCommand("countdown").setExecutor(new CommandCountdown());
        getCommand("globalmute").setExecutor(new CommandGlobalmute());
        getCommand("head").setExecutor(new CommandHead());
        getCommand("invsee").setExecutor(new CommandInvsee());
        getCommand("stats").setExecutor(new CommandStats());
        getCommand("shop").setExecutor(new CommandShop());
        getCommand("settings").setExecutor(new CommandSettings());
        getCommand("regeln").setExecutor(new CommandRegeln());
        getCommand("vanish").setExecutor(new CommandVanish());
        getCommand("vote").setExecutor(new CommandVote());
        getCommand("ping").setExecutor(new CommandPing());
        getCommand("money").setExecutor(new CommandMoney());
        getCommand("build").setExecutor(new CommandBuild());
        getCommand("fakeentity").setExecutor(new CommandFakeentity());
        getCommand("home").setExecutor(new CommandHome());
        getCommand("ams").setExecutor(new CommandAms());
        getCommand("perks").setExecutor(new CommandPerks());
        getCommand("mines").setExecutor(new CommandMines());
        getCommand("support").setExecutor(new CommandSupport());
        getCommand("report").setExecutor(new CommandReport());
        getCommand("roulette").setExecutor(new CommandRoulette());
        getCommand("kit").setExecutor(new CommandKit());
        getCommand("clan").setExecutor(new CommandClan());
        getCommand("coinflip").setExecutor(new CommandCoinflip());
        getCommand("ranking").setExecutor(new CommandRanking());
        getCommand("jackpot").setExecutor(new CommandJackpot());
        getCommand("freeze").setExecutor(new CommandFreeze());
        getCommand("combatwall").setExecutor(new CommandCombatwall());
        getCommand("ignore").setExecutor(new CommandIgnore());
        getCommand("pvpshop").setExecutor(new CommandPvPShop());
        getCommand("reward").setExecutor(new CommandReward());
        getCommand("kopfgeld").setExecutor(new CommandKopfgeld());
        getCommand("ban").setExecutor(new BanCommand(getPunishmentController()));
        getCommand("pvp").setExecutor(new PvPCommand());
        getCommand("dailypot").setExecutor(new DailyPotCommand());
        getCommand("trade").setExecutor(new TradeCommand());
        getCommand("bossegg").setExecutor(new BossEggCommand());
    }

    public BossEggManager getBossEggManager() {
        return bossEggManager;
    }


}