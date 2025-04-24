package meldexun.renderlib.asm;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

import meldexun.asmutil2.ASMUtil;
import meldexun.asmutil2.ClassNodeClassTransformer;
import meldexun.asmutil2.ClassNodeTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public class AsyncKeyboardTransformer extends ClassNodeClassTransformer implements IClassTransformer {

	private static final List<ClassNodeTransformer> TRANSFORMER = Collections.singletonList(new ClassNodeTransformer() {
		@Override
		public boolean transform(ClassNode classNode) {
			return ASMUtil.stream(classNode)
					.flatMap(ASMUtil::stream)
					.filter(MethodInsnNode.class::isInstance)
					.map(MethodInsnNode.class::cast)
					.filter(insn -> insn.owner.equals("org/lwjgl/input/Keyboard") && insn.name.equals("isKeyDown"))
					.peek(insn -> insn.owner = "meldexun/renderlib/asm/AsyncKeyboardTransformer$Hook")
					.count() > 0;
		}

		@Override
		public int writeFlags() {
			return 0;
		}

		@Override
		public int priority() {
			return 0;
		}
	});

	@Override
	protected List<ClassNodeTransformer> getClassNodeTransformers(String name) {
		return TRANSFORMER;
	}

	public static class Hook {

		private static final ByteBuffer keyDownBuffer;
		static {
			try {
				Field f = Keyboard.class.getDeclaredField("keyDownBuffer");
				f.setAccessible(true);
				keyDownBuffer = (ByteBuffer) f.get(null);
			} catch (ReflectiveOperationException e) {
				throw new UnsupportedOperationException("Failed to find org.lwjgl.input.Keyboard keyDownBuffer");
			}
		}

		public static boolean isKeyDown(int key) {
			return keyDownBuffer.get(key) != 0;
		}

	}

}
