/**
 * 
 */
package universalelectricity.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cofh.api.energy.IEnergyHandler;

/**
 * @author Calclavia
 * 
 */
public class UniversalTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		if (classNode != null && classNode.visibleAnnotations != null)
		{
			for (AnnotationNode nodes : classNode.visibleAnnotations)
			{
				if (nodes.desc.equals("Luniversalelectricity/api/UniversalClass;"))
				{
					System.out.println("WORKING:" + nodes);
					injectCompatibilityModules(classNode);
				}

			}
		}

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

	public void injectCompatibilityModules(ClassNode classNode)
	{
		// Add Thermal Expansion support
		classNode.interfaces.add(IEnergyHandler.class.getName().replace(".", "/"));

		// receiveEnergy()
		MethodNode methodNode = new MethodNode(Opcodes.ACC_PUBLIC, "receiveEnergy", "receiveEnergy(Lnet/minecraftforge/common/ForgeDirection;IZ)I", null, null);
		InsnList il = methodNode.instructions;
		il.add(new VarInsnNode(Opcodes.ALOAD, 0));
		il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, convertToSignature(), "receiveEnergy", "receiveEnergy(Lnet/minecraftforge/common/ForgeDirection;IZ)I"));
		il.add(new InsnNode(Opcodes.IRETURN));

		classNode.methods.add(methodNode);

		// Add BuildCraft support

		// Add IndustrialCraft

	}

	public String convertToSignature()
	{
		return UniversalMethods.class.getName().replace(".", "/");
	}
}
