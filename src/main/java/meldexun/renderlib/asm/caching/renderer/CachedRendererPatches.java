package meldexun.renderlib.asm.caching.renderer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class CachedRendererPatches {

	public static void registerTransformers(IClassTransformerRegistry registry) {
		registry.add("net.minecraft.client.renderer.entity.RenderManager", "renderEntity", "(Lnet/minecraft/entity/Entity;DDDFFZ)V", "a", "(Lvg;DDDFFZ)V", 0, methodNode -> {
			ASMUtil.LOGGER.info("Transforming method: RenderManager#renderEntity(Entity, double, double, double, float, float, boolean)");

			AbstractInsnNode targetNode = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/entity/RenderManager", "getEntityRenderObject", "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;", "bzf", "a", "(Lvg;)Lbzg;").find();

			methodNode.instructions.insert(targetNode, ASMUtil.listOf(
				new InsnNode(Opcodes.SWAP),
				new InsnNode(Opcodes.POP),
				new TypeInsnNode(Opcodes.CHECKCAST, "meldexun/renderlib/api/IEntityRendererCache"),
				new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/renderlib/api/IEntityRendererCache", "getRenderer", "()Lnet/minecraft/client/renderer/entity/Render;", true)
			));
			methodNode.instructions.remove(targetNode);
		});
	}

}
