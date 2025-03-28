package meldexun.renderlib.integration;

import java.nio.FloatBuffer;

import meldexun.matrixutil.Matrix4f;
import meldexun.reflectionutil.ReflectionField;
import meldexun.reflectionutil.ReflectionMethod;
import meldexun.renderlib.asm.RenderLibClassTransformer;
import meldexun.renderlib.util.Vec3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class Optifine {

	private static final ReflectionMethod<Boolean> IS_SHADERS = new ReflectionMethod<>("Config", "isShaders", "isShaders");
	private static final ReflectionField<Boolean> IS_SHADOW_PASS = new ReflectionField<>("net.optifine.shaders.Shaders", "isShadowPass", "isShadowPass");
	private static final ReflectionField<FloatBuffer> PROJECTION = new ReflectionField<>("net.optifine.shaders.Shaders", "projection", "projection");
	private static final ReflectionField<FloatBuffer> MODEL_VIEW = new ReflectionField<>("net.optifine.shaders.Shaders", "modelView", "modelView");
	private static final ReflectionField<float[]> SHADOW_LIGHT_POSITION_VECTOR = new ReflectionField<>("net.optifine.shaders.Shaders", "shadowLightPositionVector", "shadowLightPositionVector");
	private static final ReflectionMethod<Void> NEXT_ENTITY = new ReflectionMethod<>("net.optifine.shaders.Shaders", "nextEntity", "nextEntity", Entity.class);
	private static final ReflectionField<Entity> RENDERED_ENTITY = new ReflectionField<>(RenderGlobal.class, "renderedEntity", "renderedEntity");
	private static final ReflectionMethod<Boolean> IS_FAST_RENDER = new ReflectionMethod<>("Config", "isFastRender", "isFastRender");
	private static final ReflectionMethod<Boolean> IS_ANTIALIASING = new ReflectionMethod<>("Config", "isAntialiasing", "isAntialiasing");
	private static final ReflectionMethod<Void> BEGIN_ENTITIES_GLOWING = new ReflectionMethod<>("net.optifine.shaders.Shaders", "beginEntitiesGlowing", "beginEntitiesGlowing");
	private static final ReflectionMethod<Void> END_ENTITIES_GLOWING = new ReflectionMethod<>("net.optifine.shaders.Shaders", "endEntitiesGlowing", "endEntitiesGlowing");
	private static final ReflectionMethod<Void> NEXT_BLOCK_ENTITY = new ReflectionMethod<>("net.optifine.shaders.Shaders", "nextBlockEntity", "nextBlockEntity", TileEntity.class);

	public static boolean isOptifineDetected() {
		return RenderLibClassTransformer.OPTIFINE_DETECTED;
	}

	public static boolean isShaders() {
		return IS_SHADERS.invoke(null);
	}

	public static boolean isShadowPass() {
		return IS_SHADOW_PASS.getBoolean(null);
	}

	public static Matrix4f getProjectionMatrix() {
		Matrix4f m = new Matrix4f();
		m.load(PROJECTION.get(null));
		return m;
	}

	public static Matrix4f getModelViewMatrix() {
		Matrix4f m = new Matrix4f();
		m.load(MODEL_VIEW.get(null));
		return m;
	}

	public static Vec3 getShadowLightPositionVector() {
		return new Vec3(SHADOW_LIGHT_POSITION_VECTOR.get(null));
	}

	public static void nextEntity(Entity entity) {
		NEXT_ENTITY.invoke(null, entity);
	}

	public static void setRenderedEntity(Entity entity) {
		RENDERED_ENTITY.set(Minecraft.getMinecraft().renderGlobal, entity);
	}

	public static boolean isFastRender() {
		return IS_FAST_RENDER.invoke(null);
	}

	public static boolean isAntialiasing() {
		return IS_ANTIALIASING.invoke(null);
	}

	public static void beginEntitiesGlowing() {
		BEGIN_ENTITIES_GLOWING.invoke(null);
	}

	public static void endEntitiesGlowing() {
		END_ENTITIES_GLOWING.invoke(null);
	}

	public static void nextBlockEntity(TileEntity tileEntity) {
		NEXT_BLOCK_ENTITY.invoke(null, tileEntity);
	}

}
