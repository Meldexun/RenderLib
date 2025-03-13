package meldexun.renderlib.mixin;

import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import meldexun.renderlib.config.RenderLibConfig;
import meldexun.renderlib.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.Timer;
import net.minecraft.util.math.MathHelper;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Shadow
	private boolean isGamePaused;
	@Shadow
	private float renderPartialTicksPaused;
	@Shadow
	private Timer timer;
	@Shadow
	public WorldClient world;
	@Shadow
	private GameSettings gameSettings;

	@ModifyArg(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickStart(F)V", remap = false))
	public float onRenderTickStart(float partialTick) {
		return isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks;
	}

	@Inject(method = "runGameLoop", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=gameRenderer"))
	public void runGameLoop(CallbackInfo info) {
		RenderUtil.update(isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks);
	}

	@ModifyArg(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickEnd(F)V", remap = false))
	public float onRenderTickEnd(float partialTick) {
		return isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks;
	}

	@Inject(method = "updateDisplay", at = @At("HEAD"))
	public void renderlibFPSLimiter(CallbackInfo info) {
		if (world == null) {
			int fps;
			if (RenderLibConfig.mainMenuFPSSynced) {
				fps = MathHelper.clamp(gameSettings.limitFramerate, 30, 240);
			} else {
				fps = RenderLibConfig.mainMenuFPS;
			}
			Display.sync(fps);
		} else if (gameSettings.limitFramerate < GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) {
			Display.sync(gameSettings.limitFramerate);
		}
	}

	@Redirect(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isFramerateLimitBelowMax()Z"))
	public boolean vanillaFPSLimiterEnabled(Minecraft mc) {
		return false;
	}

	@Redirect(method = "launchIntegratedServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V"))
	public void launchIntegratedServer_sleep(long millis) {

	}

}
