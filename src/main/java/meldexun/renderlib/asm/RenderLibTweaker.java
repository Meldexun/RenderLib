package meldexun.renderlib.asm;

import java.io.File;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class RenderLibTweaker implements ITweaker {

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {

	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
		try {
			Class.forName("com.cleanroommc.common.CleanroomVersion");
		} catch (ClassNotFoundException e) {
			classLoader.registerTransformer(AsyncKeyboardTransformer.class.getName());
		}
		classLoader.registerTransformer(RenderLibClassTransformer.class.getName());
	}

	@Override
	public String getLaunchTarget() {
		throw new RuntimeException("Invalid for use as a primary tweaker");
	}

	@Override
	public String[] getLaunchArguments() {
		return new String[0];
	}

}
