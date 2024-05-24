package meldexun.renderlib.mixin.debug;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.RenderLib;
import meldexun.renderlib.asm.config.EarlyConfigLoader;
import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.config.RenderLibConfig.OpenGLDebugConfiguration;
import meldexun.renderlib.opengl.debug.OpenGLDebugMode;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Unique
	private OpenGLDebugConfiguration renderlib_openglDebugConfig;

	@Inject(method = "init", at = @At("HEAD"))
	public void init(CallbackInfo info) {
		renderlib_openglDebugConfig = EarlyConfigLoader.loadConfigEarly(RenderLib.MODID, "general.opengldebugoutput", new OpenGLDebugConfiguration());
	}

	@Redirect(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create(Lorg/lwjgl/opengl/PixelFormat;)V", remap = false), require = 0, expect = 1)
	private void createDisplay(PixelFormat format) throws LWJGLException {
		if (renderlib_openglDebugConfig.setContextDebugBit) {
			Display.create(format, new ContextAttribs(1, 0, 0, ContextAttribs.CONTEXT_DEBUG_BIT_ARB));
		} else {
			Display.create(format);
		}
	}

	@Redirect(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create()V", remap = false), require = 0, expect = 1)
	private void createDisplay() throws LWJGLException {
		if (renderlib_openglDebugConfig.setContextDebugBit) {
			Display.create(new PixelFormat(), new ContextAttribs(1, 0, 0, ContextAttribs.CONTEXT_DEBUG_BIT_ARB));
		} else {
			Display.create();
		}
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;createDisplay()V", shift = Shift.AFTER))
	public void init_createDisplay(CallbackInfo info) {
		OpenGLDebugMode.setupDebugOutput(renderlib_openglDebugConfig);
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/FMLClientHandler;finishMinecraftLoading()V", remap = false, shift = Shift.AFTER))
	public void init_finishMinecraftLoading(CallbackInfo info) {
		renderlib_openglDebugConfig = null;
		OpenGLDebugMode.setupDebugOutput(RenderLibConfig.openGLDebugOutput);
	}

}
