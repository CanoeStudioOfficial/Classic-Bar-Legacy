package tfar.classicbar.compat;

import com.fuzs.aquaacrobatics.client.handler.AirMeterHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import tfar.classicbar.ClassicBar;
import tfar.classicbar.EventHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class AquaAcrobaticsHelper {

    private static AirMeterHandler airMeterHandler;
    private static boolean airMeterRemoved;

    public static void syncAirMeter() {
        if (!Loader.isModLoaded("aquaacrobatics")) return;

        AirMeterHandler handler = findAirMeterHandler();
        if (handler == null) return;

        if (EventHandler.isBarEnabled("air")) {
            if (!airMeterRemoved) {
                MinecraftForge.EVENT_BUS.unregister(handler);
                airMeterRemoved = true;
                if (ClassicBar.logger != null) ClassicBar.logger.info("Disabled Aqua Acrobatics air meter while Classic Bar air is enabled");
            }
        } else if (airMeterRemoved) {
            MinecraftForge.EVENT_BUS.register(handler);
            airMeterRemoved = false;
            if (ClassicBar.logger != null) ClassicBar.logger.info("Restored Aqua Acrobatics air meter");
        }
    }

    private static AirMeterHandler findAirMeterHandler() {
        if (airMeterHandler != null) return airMeterHandler;

        try {
            Field f = EventBus.class.getDeclaredField("listeners");
            f.setAccessible(true);
            ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners =
                    (ConcurrentHashMap<Object, ArrayList<IEventListener>>) f.get(MinecraftForge.EVENT_BUS);

            for (Object key : listeners.keySet()) {
                if (key instanceof AirMeterHandler) {
                    airMeterHandler = (AirMeterHandler) key;
                    return airMeterHandler;
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            if (ClassicBar.logger != null) ClassicBar.logger.warn("Could not find Aqua Acrobatics air meter", e);
        }

        return null;
    }
}
