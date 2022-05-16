package meldexun.renderlib.util;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public interface ITileEntityRendererCache {

	default boolean hasRenderer() {
		return getRenderer() != null;
	}

	@Nullable
	<T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer();

	@Nullable
	default <T extends TileEntity> TileEntitySpecialRenderer<T> loadRenderer(TileEntity tileEntity) {
		return TileEntityRendererDispatcher.instance.getRenderer(tileEntity);
	}

}
