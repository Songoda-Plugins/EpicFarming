package com.songoda.epicfarming.settings;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.configuration.Config;
import com.songoda.core.configuration.ConfigSetting;
import com.songoda.core.hooks.EconomyManager;
import com.songoda.epicfarming.EpicFarming;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Settings {
    static final Config CONFIG = EpicFarming.getPlugin(EpicFarming.class).getCoreConfig();

    public static final ConfigSetting UPGRADE_WITH_ECONOMY = new ConfigSetting(CONFIG, "Main.Upgrade With Economy", true,
            "Should you be able to upgrade farmses with economy?");

    public static final ConfigSetting UPGRADE_WITH_XP = new ConfigSetting(CONFIG, "Main.Upgrade With XP", true,
            "Should you be able to upgrade farms with experience?");

    public static final ConfigSetting PARTICLE_TYPE = new ConfigSetting(CONFIG, "Main.Upgrade Particle Type", "SPELL_WITCH",
            "The type of particle shown when a furnace is upgraded.");

    public static final ConfigSetting FARM_TICK_SPEED = new ConfigSetting(CONFIG, "Main.Farm Tick Speed", 20,
            "The delay in ticks between each farm growth event.");

    public static final ConfigSetting GROWTH_TICK_SPEED = new ConfigSetting(CONFIG, "Main.Growth Tick Speed", 70,
            "The delay in ticks between each farm entity event.");

    public static final ConfigSetting FARM_BLOCK_MATERIAL = new ConfigSetting(CONFIG, "Main.Farm Block Material", ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9) ? "END_ROD" : "TORCH",
            "What material should be used as a farm item?");

    public static final ConfigSetting NON_COMMAND_FARMS = new ConfigSetting(CONFIG, "Main.Allow Non Command Issued Farm Items", false,
            "Should farm item materials found in the wild work as farms?");

    public static final ConfigSetting ANIMATE = new ConfigSetting(CONFIG, "Main.Animate", true,
            "Should the processed farm item be animated above the farm item?");

    public static final ConfigSetting DISABLE_AUTO_TIL_LAND = new ConfigSetting(CONFIG, "Main.Disable Auto Til Land", false,
            "Should farms not auto til land around them?");

    public static final ConfigSetting USE_PROTECTION_PLUGINS = new ConfigSetting(CONFIG, "Main.Use Protection Plugins", true,
            "Should we use protection plugins?");

    public static final ConfigSetting BREAKABLE_BLOCKS = new ConfigSetting(CONFIG, "Main.Breakable Blocks", Arrays.asList(
            "TALL_GRASS", "GRASS", "BLUE_ORCHID", "AZURE_BLUET",
            "ALLIUM", "POPPY", "DANDELION", "SNOW",
            "CORNFLOWER", "FERN", "LARGE_FERN", "SUNFLOWER",
            "ROSE_BUSH", "OXEYE_DAISY", "LILY_OF_THE_VALLEY", "PEONY",
            "LILAC", "WITHER_ROSE", "PINK_TULIP", "RED_TULIP",
            "WHITE_TULIP", "ORANGE_TULIP", "NETHER_SPROUTS", "GLOW_LICHEN"),
            "Which blocks should farms be allowed to break when they til the land?");

    public static final ConfigSetting ECONOMY_PLUGIN = new ConfigSetting(CONFIG, "Main.Economy", EconomyManager.getEconomy() == null ? "Vault" : EconomyManager.getEconomy().getName(),
            "Which economy plugin should be used?",
            "Supported plugins you have installed: \"" + EconomyManager.getManager().getRegisteredPlugins().stream().collect(Collectors.joining("\", \"")) + "\".");

    public static final ConfigSetting ECO_ICON = new ConfigSetting(CONFIG, "Interfaces.Economy Icon", "SUNFLOWER");
    public static final ConfigSetting XP_ICON = new ConfigSetting(CONFIG, "Interfaces.XP Icon", "EXPERIENCE_BOTTLE");

    public static final ConfigSetting GLASS_TYPE_1 = new ConfigSetting(CONFIG, "Interfaces.Glass Type 1", "GRAY_STAINED_GLASS_PANE");
    public static final ConfigSetting GLASS_TYPE_2 = new ConfigSetting(CONFIG, "Interfaces.Glass Type 2", "BLUE_STAINED_GLASS_PANE");
    public static final ConfigSetting GLASS_TYPE_3 = new ConfigSetting(CONFIG, "Interfaces.Glass Type 3", "LIGHT_BLUE_STAINED_GLASS_PANE");

    public static final ConfigSetting LANGUGE_MODE = new ConfigSetting(CONFIG, "System.Language Mode", "en_US",
            "The enabled language file.",
            "More language files (if available) can be found in the plugins data folder.");

    public static final ConfigSetting MYSQL_ENABLED = new ConfigSetting(CONFIG, "MySQL.Enabled", false, "Set to 'true' to use MySQL instead of SQLite for data storage.");
    public static final ConfigSetting MYSQL_HOSTNAME = new ConfigSetting(CONFIG, "MySQL.Hostname", "localhost");
    public static final ConfigSetting MYSQL_PORT = new ConfigSetting(CONFIG, "MySQL.Port", 3306);
    public static final ConfigSetting MYSQL_DATABASE = new ConfigSetting(CONFIG, "MySQL.Database", "your-database");
    public static final ConfigSetting MYSQL_USERNAME = new ConfigSetting(CONFIG, "MySQL.Username", "user");
    public static final ConfigSetting MYSQL_PASSWORD = new ConfigSetting(CONFIG, "MySQL.Password", "pass");
    public static final ConfigSetting MYSQL_USE_SSL = new ConfigSetting(CONFIG, "MySQL.Use SSL", false);
    public static final ConfigSetting MYSQL_POOL_SIZE = new ConfigSetting(CONFIG, "MySQL.Pool Size", 3, "Determines the number of connections the pool is using. Increase this value if you are getting timeout errors when more players online.");

    /**
     * In order to set dynamic economy comment correctly, this needs to be
     * called after EconomyManager load
     */
    public static void setupConfig() {
        CONFIG.load();
        CONFIG.setAutoremove(true).setAutosave(true);

        // convert glass pane settings
        int color;
        if ((color = GLASS_TYPE_1.getInt(-1)) != -1) {
            CONFIG.set(GLASS_TYPE_1.getKey(), CompatibleMaterial.getGlassPaneForColor(color).name());
        }
        if ((color = GLASS_TYPE_2.getInt(-1)) != -1) {
            CONFIG.set(GLASS_TYPE_2.getKey(), CompatibleMaterial.getGlassPaneForColor(color).name());
        }
        if ((color = GLASS_TYPE_3.getInt(-1)) != -1) {
            CONFIG.set(GLASS_TYPE_3.getKey(), CompatibleMaterial.getGlassPaneForColor(color).name());
        }

        if (Settings.FARM_TICK_SPEED.getInt() == 70) {
            CONFIG.set(FARM_TICK_SPEED.getKey(), 20);
        }
        CONFIG.saveChanges();
    }
}
