package meldexun.renderlib.mixin;

import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Inject(method = "getLimitFramerate", cancellable = true, at = @At("HEAD"))
	public void getLimitFramerate(CallbackInfoReturnable<Integer> info) {
		if (world == null) {
			if (RenderLibConfig.mainMenuFPSSynced) {
				info.setReturnValue(MathHelper.clamp(gameSettings.limitFramerate, 30, 240));
			} else {
				info.setReturnValue(RenderLibConfig.mainMenuFPS);
			}
		} else {
			info.setReturnValue(gameSettings.limitFramerate);
		}
	}

	@Inject(method = "isFramerateLimitBelowMax", cancellable = true, at = @At("HEAD"))
	public void isFramerateLimitBelowMax(CallbackInfoReturnable<Boolean> info) {
		if (world == null) {
			info.setReturnValue(true);
		}
	}

	@Redirect(method = "launchIntegratedServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V"))
	public void launchIntegratedServer_sleep(long millis) {
		Display.sync(20);
	}

}
