package meldexun.renderlib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = RenderLib.MODID)
public class RenderLib {

	public static final String MODID = "renderlib";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	public static boolean isFairyLightsInstalled;
	public static boolean isValkyrienSkiesInstalled;

	@EventHandler
	public void onFMLPostInitializationEvent(FMLPostInitializationEvent event) {
		isFairyLightsInstalled = Loader.isModLoaded("fairylights");
		isValkyrienSkiesInstalled = Loader.isModLoaded("valkyrienskies");
	}

}
