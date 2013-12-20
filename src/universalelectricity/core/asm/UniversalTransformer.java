/**
 * 
 */
package universalelectricity.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

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

import universalelectricity.core.asm.TemplateInjectionManager.InjectionTemplate;
import universalelectricity.core.asm.template.ThermalExpansionTemplate;
import cofh.api.energy.IEnergyHandler;

/**
 * @author Calclavia
 * 
 */
public class UniversalTransformer implements IClassTransformer
{
	static LaunchClassLoader cl = (LaunchClassLoader) UniversalTransformer.class.getClassLoader();

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (transformedName.startsWith("net.minecraft") || TemplateInjectionManager.injectionTemplates.isEmpty())
		{
			return bytes;
		}

		boolean changed = false;

		ClassNode cnode = ASMHelper.createClassNode(bytes);

		if (cnode != null && cnode.visibleAnnotations != null)
		{
			for (AnnotationNode nodes : cnode.visibleAnnotations)
			{
				if (nodes.desc.equals("Luniversalelectricity/api/UniversalClass;"))
				{
					/**
					 * Patch Thermal Expansion
					 */
					InjectionTemplate impl = TemplateInjectionManager.injectionTemplates.get(ThermalExpansionTemplate.class);

					if (impl != null)
					{
						changed |= impl.patch(cnode);
						System.out.println("[UniversalElectricity] Injected Thermal Expansion API into: " + cnode.name);
					}
				}

			}
		}

		return changed ? ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_FRAMES) : bytes;
	}
}
