package meldexun.renderlib.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.BiMap;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.HashMapClassNodeClassTransformer;
import meldexun.asmutil2.IClassTransformerRegistry;
import meldexun.asmutil2.NonLoadingClassWriter;
import meldexun.asmutil2.reader.ClassUtil;
import meldexun.renderlib.asm.animaniacatsdogs.AnimaniaCatsDogsPatches;
import meldexun.renderlib.asm.cbmultipart.CBMultipartPatches;
import meldexun.renderlib.asm.pokecube.PokecubePatches;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.asm.FMLSanityChecker;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public class RenderLibClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	private static final ClassUtil REMAPPING_CLASS_UTIL;
	static {
		try {
			Field _classLoader = FMLDeobfuscatingRemapper.class.getDeclaredField("classLoader");
			_classLoader.setAccessible(true);
			if (_classLoader.get(FMLDeobfuscatingRemapper.INSTANCE) == null) {
				Method _debfuscationDataName = FMLInjectionData.class.getDeclaredMethod("debfuscationDataName");
				_debfuscationDataName.setAccessible(true);
				Map<String, Object> callData = new HashMap<String, Object>();
				callData.put("runtimeDeobfuscationEnabled", false);
				callData.put("mcLocation", Launch.minecraftHome);
				callData.put("classLoader", Launch.classLoader);
				callData.put("deobfuscationFileName", _debfuscationDataName.invoke(null));
				new FMLSanityChecker().injectData(callData);
			}

			Field _classNameBiMap = FMLDeobfuscatingRemapper.class.getDeclaredField("classNameBiMap");
			_classNameBiMap.setAccessible(true);
			@SuppressWarnings("unchecked")
			BiMap<String, String> deobfuscationMap = (BiMap<String, String>) _classNameBiMap.get(FMLDeobfuscatingRemapper.INSTANCE);
			REMAPPING_CLASS_UTIL = ClassUtil.getInstance(new ClassUtil.Configuration(Launch.classLoader, deobfuscationMap.inverse(), deobfuscationMap));
		} catch (ReflectiveOperationException e) {
			throw new UnsupportedOperationException(e);
		}
	}

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
		registry.addObf("net.minecraft.client.renderer.RenderGlobal", "renderEntities", "func_180446_a", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V", ClassWriter.COMPUTE_FRAMES, methodNode -> {
			AbstractInsnNode targetNode1 = ASMUtil.first(methodNode).opcode(Opcodes.INVOKESTATIC).methodInsn("com/google/common/collect/Lists", "newArrayList", "()Ljava/util/ArrayList;").find();
			targetNode1 = ASMUtil.prev(methodNode, targetNode1).type(LabelNode.class).find();
			AbstractInsnNode popNode1 = ASMUtil.first(methodNode).opcode(Opcodes.INVOKEVIRTUAL).methodInsn("net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "preDrawBatch", "()V").find();
			popNode1 = ASMUtil.prev(methodNode, popNode1).opcode(Opcodes.INVOKEVIRTUAL).methodInsnObf("net/minecraft/profiler/Profiler", "endStartSection", "func_76318_c", "(Ljava/lang/String;)V").find();
			if (OPTIFINE_DETECTED) {
				popNode1 = ASMUtil.prev(methodNode, popNode1).opcode(Opcodes.INVOKESTATIC).methodInsn("net/optifine/shaders/Shaders", "endEntities", "()V").find();
				popNode1 = ASMUtil.prev(methodNode, popNode1).type(JumpInsnNode.class).find();
			}
			popNode1 = ASMUtil.prev(methodNode, popNode1).type(LabelNode.class).find();

			AbstractInsnNode targetNode2 = ASMUtil.first(methodNode).opcode(Opcodes.INVOKEVIRTUAL).methodInsn("net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "preDrawBatch", "()V").find();
			if (OPTIFINE_DETECTED) {
				targetNode2 = ASMUtil.next(methodNode, targetNode2).opcode(Opcodes.INVOKESTATIC).methodInsn("net/minecraft/client/renderer/tileentity/TileEntitySignRenderer", "updateTextRenderDistance", "()V").find();
			}
			targetNode2 = ASMUtil.next(methodNode, targetNode2).type(LabelNode.class).find();
			AbstractInsnNode popNode2 = ASMUtil.first(methodNode).opcode(Opcodes.INVOKEVIRTUAL).methodInsn("net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "drawBatch", "(I)V").find();
			if (OPTIFINE_DETECTED) {
				popNode2 = ASMUtil.prev(methodNode, popNode2).opcode(Opcodes.INVOKEVIRTUAL).methodInsn("net/optifine/reflect/ReflectorMethod", "exists", "()Z").find();
			}
			popNode2 = ASMUtil.prev(methodNode, popNode2).type(LabelNode.class).find();

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

		registry.addObf("net.minecraft.server.MinecraftServer", "setUserMessage", "func_71192_d", "(Ljava/lang/String;)V", 0, methodNode -> {
			methodNode.access &= ~Opcodes.ACC_SYNCHRONIZED;
		});
		registry.addObf("net.minecraft.server.MinecraftServer", "getUserMessage", "func_71195_b_", "()Ljava/lang/String;", 0, methodNode -> {
			methodNode.access &= ~Opcodes.ACC_SYNCHRONIZED;
		});
		// @formatter:on

		PokecubePatches.registerTransformers(registry);
		AnimaniaCatsDogsPatches.registerTransformers(registry);
		CBMultipartPatches.registerTransformers(registry);
	}

	@Override
	protected ClassWriter createClassWriter(int flags) {
		return new NonLoadingClassWriter(flags, REMAPPING_CLASS_UTIL);
	}

}
