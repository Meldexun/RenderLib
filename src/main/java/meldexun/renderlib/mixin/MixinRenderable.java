package meldexun.renderlib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.renderlib.util.IRenderable;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

@Mixin(value = { Entity.class, TileEntity.class })
public class MixinRenderable implements IRenderable {

	@Unique
	private int lastTimeRendered;

	@Override
	public boolean setLastTimeRendered(int frame) {
		if (lastTimeRendered != frame) {
			lastTimeRendered = frame;
			return true;
		}
		return false;
	}

}
