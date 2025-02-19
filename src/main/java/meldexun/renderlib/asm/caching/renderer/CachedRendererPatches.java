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
		registry.addObf("net.minecraft.client.renderer.entity.RenderManager", "renderEntity", "func_188391_a", "(Lnet/minecraft/entity/Entity;DDDFFZ)V", 0, methodNode -> {
			AbstractInsnNode targetNode = ASMUtil.first(methodNode).opcode(Opcodes.INVOKEVIRTUAL).methodInsnObf("net/minecraft/client/renderer/entity/RenderManager", "getEntityRenderObject", "func_78713_a", "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;").find();

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
