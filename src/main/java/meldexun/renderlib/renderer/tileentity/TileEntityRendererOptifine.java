package meldexun.renderlib.renderer.tileentity;

import meldexun.reflectionutil.ReflectionField;
import meldexun.reflectionutil.ReflectionMethod;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRendererOptifine extends TileEntityRenderer {

	private static final ReflectionMethod<Boolean> IS_SHADERS = new ReflectionMethod<>("Config", "isShaders", "isShaders");
	private static final ReflectionField<Boolean> IS_SHADOW_PASS = new ReflectionField<>("net.optifine.shaders.Shaders", "isShadowPass", "isShadowPass");
	private static final ReflectionMethod<Void> NEXT_BLOCK_ENTITY = new ReflectionMethod<>("net.optifine.shaders.Shaders", "nextBlockEntity", "nextBlockEntity", TileEntity.class);
	private boolean isShaders = false;

	@Override
	public void renderTileEntities(ICamera camera, float partialTicks, double camX, double camY, double camZ) {
		this.isShaders = IS_SHADERS.invoke(null);

		int r = this.renderedTileEntities;
		int o = this.occludedTileEntities;
		int t = this.totalTileEntities;
		super.renderTileEntities(camera, partialTicks, camX, camY, camZ);
		if (IS_SHADOW_PASS.getBoolean(null)) {
			this.renderedTileEntities = r;
			this.occludedTileEntities = o;
			this.totalTileEntities = t;
		}
	}

	@Override
	protected void preRenderTileEntity(TileEntity tileEntity) {
		if (this.isShaders) {
			NEXT_BLOCK_ENTITY.invoke(null, tileEntity);
		}
		super.preRenderTileEntity(tileEntity);
	}

}
