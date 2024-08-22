package meldexun.renderlib.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.renderlib.asm.animaniacatsdogs.AnimaniaCatsDogsPatches;
import meldexun.renderlib.asm.caching.renderer.CachedRendererPatches;
import meldexun.renderlib.asm.cbmultipart.CBMultipartPatches;
import meldexun.renderlib.asm.pokecube.PokecubePatches;
import net.minecraft.launchwrapper.IClassTransformer;

public class RenderLibClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	public static final boolean OPTIFINE_DETECTED;
	static {
		boolean flag = false;
		try {
			Class.forName("optifine.OptiFineClassTransformer", false, RenderLibPlugin.class.getClassLoader());
			flag = true;
		} catch (ClassNotFoundException e) {
			// ignore
		}
		OPTIFINE_DETECTED = flag;
	}

	@Override
	protected void registerTransformers(IClassTransformerRegistry registry) {
		// @formatter:off
		registry.add("net.minecraft.client.renderer.RenderGlobal", "renderEntities", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V", "a", "(Lvg;Lbxy;F)V", ClassWriter.COMPUTE_FRAMES, methodNode -> {
			AbstractInsnNode targetNode1 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKESTATIC, "com/google/common/collect/Lists", "newArrayList", "()Ljava/util/ArrayList;").find();
			targetNode1 = ASMUtil.prev(targetNode1).type(LabelNode.class).find();
			AbstractInsnNode popNode1 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "preDrawBatch", "()V", "bwx", "preDrawBatch", "()V").find();
			popNode1 = ASMUtil.prev(popNode1).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/profiler/Profiler", "endStartSection", "(Ljava/lang/String;)V", "rl", "c", "(Ljava/lang/String;)V").find();
			if (OPTIFINE_DETECTED) {
				popNode1 = ASMUtil.prev(popNode1).methodInsn(Opcodes.INVOKESTATIC, "net/optifine/shaders/Shaders", "endEntities", "()V").find();
				popNode1 = ASMUtil.prev(popNode1).type(JumpInsnNode.class).find();
			}
			popNode1 = ASMUtil.prev(popNode1).type(LabelNode.class).find();

			AbstractInsnNode targetNode2 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "preDrawBatch", "()V", "bwx", "preDrawBatch", "()V").find();
			if (OPTIFINE_DETECTED) {
				targetNode2 = ASMUtil.next(targetNode2).methodInsn(Opcodes.INVOKESTATIC, "net/minecraft/client/renderer/tileentity/TileEntitySignRenderer", "updateTextRenderDistance", "()V", "bxf", "updateTextRenderDistance", "()V").find();
			}
			targetNode2 = ASMUtil.next(targetNode2).type(LabelNode.class).find();
			AbstractInsnNode popNode2 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "drawBatch", "(I)V", "bwx", "drawBatch", "(I)V").find();
			if (OPTIFINE_DETECTED) {
				popNode2 = ASMUtil.prev(popNode2).methodInsn(Opcodes.INVOKEVIRTUAL, "net/optifine/reflect/ReflectorMethod", "exists", "()Z").find();
			}
			popNode2 = ASMUtil.prev(popNode2).type(LabelNode.class).find();

			methodNode.instructions.insert(targetNode1, ASMUtil.listOf(
				new VarInsnNode(Opcodes.FLOAD, 3),
				new MethodInsnNode(Opcodes.INVOKESTATIC, "meldexun/renderlib/renderer/EntityRenderManager", "renderEntities", "(F)V", false),
				new InsnNode(Opcodes.ICONST_0),
				new JumpInsnNode(Opcodes.IFEQ, (LabelNode) popNode1)
			));

			methodNode.instructions.insert(targetNode2, ASMUtil.listOf(
				new VarInsnNode(Opcodes.FLOAD, 3),
				new MethodInsnNode(Opcodes.INVOKESTATIC, "meldexun/renderlib/renderer/TileEntityRenderManager", "renderTileEntities", "(F)V", false),
				new InsnNode(Opcodes.ICONST_0),
				new JumpInsnNode(Opcodes.IFEQ, (LabelNode) popNode2)
			));
		});
		// @formatter:on

		CachedRendererPatches.registerTransformers(registry);
		PokecubePatches.registerTransformers(registry);
		AnimaniaCatsDogsPatches.registerTransformers(registry);
		CBMultipartPatches.registerTransformers(registry);
	}

}
