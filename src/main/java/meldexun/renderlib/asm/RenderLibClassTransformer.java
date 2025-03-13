package meldexun.renderlib.asm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import LZMA.LzmaInputStream;
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

public class RenderLibClassTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {

	private static final ClassUtil REMAPPING_CLASS_UTIL;
	static {
		@SuppressWarnings("unchecked")
		BiMap<String, String> deobfuscationMap = (BiMap<String, String>) Launch.blackboard.computeIfAbsent("ASMUtil_deobfuscationMap", k -> {
			String gradleStartProp = System.getProperty("net.minecraftforge.gradle.GradleStart.srg.srg-mcp");
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(Strings.isNullOrEmpty(gradleStartProp) ? new LzmaInputStream(Launch.classLoader.getResourceAsStream("deobfuscation_data-1.12.2.lzma")) : Files.newInputStream(Paths.get(gradleStartProp)), StandardCharsets.UTF_8))) {
				return reader.lines()
						.map(Pattern.compile(" *CL: +([^ ]*) +([^ ]*).*")::matcher)
						.filter(Matcher::matches)
						.collect(HashBiMap::create, (map, matcher) -> map.put(matcher.group(1), matcher.group(2)), Map::putAll);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
		REMAPPING_CLASS_UTIL = ClassUtil.getInstance(new ClassUtil.Configuration(Launch.classLoader, deobfuscationMap.inverse(), deobfuscationMap));
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
