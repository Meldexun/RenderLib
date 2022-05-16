package meldexun.renderlib.mixin.caching.renderer;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import meldexun.renderlib.util.IEntityRendererCache;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class MixinEntity implements IEntityRendererCache {

	@Unique
	private Render<Entity> renderer;
	@Unique
	private boolean rendererInitialized;

	@SuppressWarnings("unchecked")
	@Unique
	@Override
	@Nullable
	public <T extends Entity> Render<T> getRenderer() {
		if (!rendererInitialized) {
			if (!((Entity) (Object) this instanceof AbstractClientPlayer)
					|| (((AbstractClientPlayer) (Entity) (Object) this).playerInfo != null
					&& ((AbstractClientPlayer) (Entity) (Object) this).playerInfo.skinType != null)) {
				rendererInitialized = true;
			}
			renderer = loadRenderer((Entity) (Object) this);
		}
		return (Render<T>) renderer;
	}

}
