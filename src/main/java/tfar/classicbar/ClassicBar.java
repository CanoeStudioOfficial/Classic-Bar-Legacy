package tfar.classicbar;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import tfar.classicbar.classicbar.Tags;
import tfar.classicbar.compat.FoodHelper;
import tfar.classicbar.config.ModConfig;
import tfar.classicbar.network.SyncHandler;
import tfar.classicbar.overlays.modoverlays.*;
import tfar.classicbar.overlays.vanillaoverlays.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class ClassicBar {

    public static final String DEPENDENCIES = "after:randomtweaks@[1.12.2-2.7.1.0,);";

    public static final String[] problemMods = new String[]{"mantle", "toughasnails", "simpledifficulty"};

    public static final boolean TOUGHASNAILS = Loader.isModLoaded("toughasnails");
    public static final boolean SIMPLEDIFFICULTY = Loader.isModLoaded("simpledifficulty");
    public static final boolean IBLIS = Loader.isModLoaded("iblis");
    public static final boolean BAUBLES = Loader.isModLoaded("baubles");
    public static final boolean RANDOMTWEAKS = Loader.isModLoaded("randomtweaks");
    public static final boolean BETWEENLANDS = Loader.isModLoaded("thebetweenlands");
    public static final boolean ENDURANCE = Loader.isModLoaded("ercore");

    public static final boolean VAMPIRISM = Loader.isModLoaded("vampirism");

    public static final boolean HUNGERCHANGED = IBLIS || RANDOMTWEAKS;


    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        FoodHelper.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SyncHandler.init();
        if (event.getSide() == Side.CLIENT) {
            TooltipRender.init();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new ModConfig.ConfigEventHandler());
        //Register renderers for events
        ClassicBar.logger.info("Registering Vanilla Overlay");
        MinecraftForge.EVENT_BUS.register(new EventHandler());

        EventHandler.registerAll(new AbsorptionRenderer(),new AirRenderer(), new ArmorRenderer(), new ArmorToughnessRenderer(),
                new HealthRenderer(), new HungerRenderer(), new MountHealthRenderer());

        //mod renderers
        ClassicBar.logger.info("Registering Mod Overlays");
        if (Loader.isModLoaded("randomthings")) EventHandler.register(new LavaCharmRenderer());
        if (Loader.isModLoaded("lavawaderbauble")) EventHandler.register(new LavaWaderBaubleRenderer());
        if (BETWEENLANDS) EventHandler.register(new DecayRenderer());
        //if (Loader.isModLoaded("superiorshields"))
        //  MinecraftForge.EVENT_BUS.register(new SuperiorShieldRenderer());
        if (TOUGHASNAILS) EventHandler.register(new ThirstBarRenderer());
        if (SIMPLEDIFFICULTY) EventHandler.register(new SDThirstBarRenderer());
        if (ENDURANCE) EventHandler.register(new EnduranceBarRenderer());
        if (Loader.isModLoaded("botania")) EventHandler.register(new TiaraBarRenderer());
        if (VAMPIRISM) EventHandler.register(new VampireRenderer());
        EventHandler.setup();

        boolean areProblemModsPresent = Arrays.stream(problemMods).anyMatch(Loader::isModLoaded);
        if (areProblemModsPresent) {
            logger.info("Unregistering problematic overlays.");
            ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners;
            try {
                Field f = EventBus.class.getDeclaredField("listeners");
                f.setAccessible(true);
                listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>) f.get(MinecraftForge.EVENT_BUS);
                listeners.keySet().forEach(key -> {
                    String s = key.getClass().getCanonicalName();
                    if ("slimeknights.mantle.client.ExtraHeartRenderHandler".equals(s)) {
                        logger.info("Unregistered Mantle bar");
                        MinecraftForge.EVENT_BUS.unregister(key);
                    }
                    else if ("toughasnails.handler.thirst.ThirstOverlayHandler".equals(s)) {
                        logger.info("Unregistered Thirst bar");
                        MinecraftForge.EVENT_BUS.unregister(key);
                    }
                    else if ("com.charles445.simpledifficulty.client.gui.ThirstGui".equals(s)) {
                        logger.info("Unregistered SDThirst bar");
                        MinecraftForge.EVENT_BUS.unregister(key);
                    }
                });
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}