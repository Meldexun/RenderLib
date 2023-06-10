package meldexun.renderlib.asm.animaniacatsdogs;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class AnimaniaCatsDogsPatches {

	public static void registerTransformers(IClassTransformerRegistry registry) {
		registry.add("com.animania.addons.catsdogs.client.render.blocks.TileEntityPropRenderer", "render", "(Lcom/animania/addons/catsdogs/common/tileentity/TileEntityProp;DDDFIF)V", ClassWriter.COMPUTE_FRAMES, methodNode -> {
			LabelNode label = new LabelNode();

			methodNode.instructions.insert(ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1),
				new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/animania/addons/catsdogs/common/tileentity/TileEntityProp", "func_145838_q", "()Lnet/minecraft/block/Block;", false), // getBlockType
				new JumpInsnNode(Opcodes.IFNONNULL, label),
				new InsnNode(Opcodes.RETURN),
				label
			));
		});
	}

}
