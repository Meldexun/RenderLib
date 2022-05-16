package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
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

	@ModifyArg(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickStart(F)V"))
	public float onRenderTickStart(float partialTick) {
		return isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks;
	}

	@ModifyArg(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickEnd(F)V"))
	public float onRenderTickEnd(float partialTick) {
		return isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks;
	}

	@Inject(method = "getLimitFramerate", cancellable = true, at = @At("HEAD"))
	public void getLimitFramerate(CallbackInfoReturnable<Integer> info) {
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.world == null) {
			info.setReturnValue(MathHelper.clamp(mc.gameSettings.limitFramerate, 30, 240));
			return;
		}

		info.setReturnValue(mc.gameSettings.limitFramerate);
	}

}
