package meldexun.renderlib.mixin.debug;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.util.GLUtil;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Redirect(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;create(Lorg/lwjgl/opengl/PixelFormat;)V", remap = false))
	private void createDisplay(PixelFormat format) throws LWJGLException {
		GLUtil.createDisplay(format);
		GLUtil.setupDebugOutputFromFile();
	}

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/FMLClientHandler;finishMinecraftLoading()V", remap = false, shift = Shift.AFTER))
	public void init(CallbackInfo info) {
		GLUtil.setupDebugOutputFromMemory();
	}

}
