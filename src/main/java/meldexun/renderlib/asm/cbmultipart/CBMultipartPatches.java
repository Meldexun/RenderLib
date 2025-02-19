package meldexun.renderlib.asm.cbmultipart;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.IClassTransformerRegistry;

public class CBMultipartPatches {

	public static void registerTransformers(IClassTransformerRegistry registry) {
		registry.add("codechicken.multipart.MultipartGenerator$", "silentAddTile", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;)V", ClassWriter.COMPUTE_FRAMES, methodNode -> {
			MethodInsnNode target = ASMUtil.first(methodNode).opcode(Opcodes.INVOKEVIRTUAL).methodInsnObf("net/minecraft/world/chunk/Chunk", "addTileEntity", "func_177426_a", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;)V").find();
			LabelNode label = new LabelNode();

			methodNode.instructions.insert(target, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 1),
				new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "field_72995_K", "Z"), // isRemote
				new JumpInsnNode(Opcodes.IFEQ, label),
				new VarInsnNode(Opcodes.ALOAD, 3),
				new TypeInsnNode(Opcodes.CHECKCAST, "meldexun/renderlib/api/ITileEntityRendererCache"),
				new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/renderlib/api/ITileEntityRendererCache", "hasRenderer", "()Z", true),
				new JumpInsnNode(Opcodes.IFEQ, label),
				new VarInsnNode(Opcodes.ALOAD, 1),
				new TypeInsnNode(Opcodes.CHECKCAST, "meldexun/renderlib/util/ITileEntityHolder"),
				new MethodInsnNode(Opcodes.INVOKEINTERFACE, "meldexun/renderlib/util/ITileEntityHolder", "getTileEntities", "()Ljava/util/List;", true),
//				new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", "renderableTileEntityList", "Ljava/util/List;"),
				new VarInsnNode(Opcodes.ALOAD, 3),
				new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true),
				new InsnNode(Opcodes.POP),
				label
			));
		});
	}

}
