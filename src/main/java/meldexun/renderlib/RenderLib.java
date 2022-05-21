package meldexun.renderlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = RenderLib.MODID)
public class RenderLib {

	public static final String MODID = "renderlib";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static boolean isFairyLightsInstalled;
	public static boolean isValkyrienSkiesInstalled;

	@EventHandler
	public void onFMLConstructionEvent(FMLConstructionEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		ConfigManager.sync(MODID, Config.Type.INSTANCE);
	}

	@EventHandler
	public void onFMLPostInitializationEvent(FMLPostInitializationEvent event) {
		isFairyLightsInstalled = Loader.isModLoaded("fairylights");
		isValkyrienSkiesInstalled = Loader.isModLoaded("valkyrienskies");
	}

	@SubscribeEvent
	public void onConfigChangedEvent(OnConfigChangedEvent event) {
		if (event.getModID().equals(MODID)) {
			ConfigManager.sync(MODID, Config.Type.INSTANCE);
		}
	}

}
