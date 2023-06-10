package meldexun.renderlib.asm.pokecube;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.IClassTransformerRegistry;

public class PokecubePatches {

	public static void registerTransformers(IClassTransformerRegistry registry) {
		registry.add("pokecube.core.entity.pokemobs.helper.EntityTameablePokemob", 0, classNode -> {
			MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "getRenderer", "()Lnet/minecraft/client/renderer/entity/Render;", null, null);
			methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
			methodNode.instructions.add(new InsnNode(Opcodes.DUP));
			methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "pokecube/core/entity/pokemobs/helper/EntityTameablePokemob", "loadRenderer", "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;", false));
			methodNode.instructions.add(new InsnNode(Opcodes.ARETURN));
			classNode.methods.add(methodNode);
		});
	}

}
