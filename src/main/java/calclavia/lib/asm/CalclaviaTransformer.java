package calclavia.lib.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import universalelectricity.core.asm.ASMHelper;
import universalelectricity.core.asm.ObfMapping;

/**
 * @author Calclavia
 * 
 */
public class CalclaviaTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (transformedName.equals("net.minecraft.world.chunk.Chunk"))
		{
			System.out.println("[Calclavia Core] Transforming Chunk class for chunkModified event.");

			ClassNode cnode = ASMHelper.createClassNode(bytes);

			for (MethodNode method : cnode.methods)
			{
				ObfMapping m = new ObfMapping(cnode.name, method.name, method.desc).toRuntime();

				if (m.s_name.equals("setBlockIDWithMetadata"))
				{
					System.out.println("[Calclavia Core] Found method " + m.s_name);
					InsnList list = new InsnList();
					list.add(new VarInsnNode(ALOAD, 0));
					list.add(new VarInsnNode(ILOAD, 1));
					list.add(new VarInsnNode(ILOAD, 2));
					list.add(new VarInsnNode(ILOAD, 3));
					list.add(new VarInsnNode(ILOAD, 4));
					list.add(new VarInsnNode(ILOAD, 5));
					list.add(new MethodInsnNode(INVOKESTATIC, "calclavia/lib/asm/StaticForwarder", "chunkSetBlockEvent", "(Lnet/minecraft/world/chunk/Chunk;IIIII)V"));
					method.instructions.insert(list);
					System.out.println("[Calclavia Core] Injected instruction to method: " + m.s_name);
				}
			}

			return ASMHelper.createBytes(cnode, 0);
		}

		return bytes;
	}

}
