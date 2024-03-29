package meldexun.renderlib.mixin.debug;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.asm.config.EarlyConfigLoader;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.config.RenderLibConfig.OpenGLDebugConfiguration;
import meldexun.renderlib.util.GLUtil;
import meldexun.renderlib.util.OpenGLDebugMode;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Redirect(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create(Lorg/lwjgl/opengl/PixelFormat;)V", remap = false))
	private void createDisplay(PixelFormat format) throws LWJGLException {
		GLUtil.createDisplay(format);
		OpenGLDebugMode.setupDebugOutput(EarlyConfigLoader.loadConfigEarly(RenderLib.MODID, "general.opengldebugoutput", new OpenGLDebugConfiguration()));
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/FMLClientHandler;finishMinecraftLoading()V", remap = false, shift = Shift.AFTER))
	public void init(CallbackInfo info) {
		OpenGLDebugMode.setupDebugOutput(RenderLibConfig.openGLDebugOutput);
	}

}
