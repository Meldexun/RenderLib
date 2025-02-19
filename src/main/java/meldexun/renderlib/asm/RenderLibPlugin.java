package meldexun.renderlib.asm;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions({ "meldexun.renderlib.asm", "meldexun.asmutil2" })
public class RenderLibPlugin implements IFMLLoadingPlugin {

	@SuppressWarnings("unchecked")
	public RenderLibPlugin() {
		try {
			Field _deobfuscatedEnvironment = CoreModManager.class.getDeclaredField("deobfuscatedEnvironment");
			_deobfuscatedEnvironment.setAccessible(true);
			if (_deobfuscatedEnvironment.getBoolean(null)) {
				((List<String>) Launch.blackboard.get("TweakClasses")).add(RenderLibTweaker.class.getName());
			} else {
				((List<ITweaker>) Launch.blackboard.get("Tweaks")).add(new RenderLibTweaker());
			}
			Field _tweakSorting = CoreModManager.class.getDeclaredField("tweakSorting");
			_tweakSorting.setAccessible(true);
			((Map<String, Integer>) _tweakSorting.get(null)).put(RenderLibTweaker.class.getName(), 1001);
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	@Override
	public String[] getASMTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		if (Boolean.FALSE.equals(data.get("runtimeDeobfuscationEnabled"))) {
			MixinBootstrap.init();
			MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
		}
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
