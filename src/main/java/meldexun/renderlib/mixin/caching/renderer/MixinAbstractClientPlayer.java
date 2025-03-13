package meldexun.renderlib.mixin.caching.renderer;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.renderlib.api.IEntityRendererCache;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer implements IEntityRendererCache {

	@Unique
	private Render<AbstractClientPlayer> renderer;
	@Unique
	private String prevSkinType;

	@SuppressWarnings("unchecked")
	@Unique
	@Override
	@Nullable
	public <T extends Entity> Render<T> getRenderer() {
		String skinType = ((AbstractClientPlayer) (Object) this).playerInfo != null && ((AbstractClientPlayer) (Object) this).playerInfo.skinType != null
				? ((AbstractClientPlayer) (Object) this).playerInfo.skinType
				: "default";
		if (!skinType.equals(prevSkinType)) {
			prevSkinType = skinType;
			renderer = loadRenderer((AbstractClientPlayer) (Object) this);
		}
		return (Render<T>) renderer;
	}

}
