/**
 * 
 */
package universalelectricity.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import universalelectricity.api.Compatibility.CompatibilityType;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.compatibility.CompatibilityModule;
import universalelectricity.core.asm.TemplateInjectionManager.InjectionTemplate;

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
					/*
					 * The 2nd value in UniversalClass is the annotation we're looking for to filter
					 * out which mod to deal with.
					 */
					String flags = null;

					if (nodes.values != null && nodes.values.size() >= 2)
					{
						flags = (String) nodes.values.get(1);
					}

					/**
					 * The type of transformation to make.
					 * 
					 * -1: Don't transform.
					 * 0: Transform IEnergyInterface
					 * 1: Transform IConductor
					 * 2: Transform IElectricItem
					 */
					int transformationType = -1;

					if (cnode.interfaces.contains(IEnergyInterface.class.getName().replace(".", "/")))
					{
						transformationType = 0;
					}
					else if (cnode.interfaces.contains(IConductor.class.getName().replace(".", "/")))
					{
						transformationType = 1;
					}

					if (transformationType == 0)
					{
						if (flags == null || flags.equals(""))
						{
							for (InjectionTemplate template : TemplateInjectionManager.injectionTemplates.values())
							{
								if (template != null)
								{
									changed |= template.patch(cnode);
									System.out.println("[Universal Electricity] Injected " + template.className + " API into: " + cnode.name);
								}
							}
						}
						else
						{
							String[] separatedFlags = flags.split(";");

							for (String separated : separatedFlags)
							{
								if (CompatibilityType.get(separated) != null)
								{
									InjectionTemplate template = TemplateInjectionManager.injectionTemplates.get(separated);

									if (template != null)
									{
										changed |= template.patch(cnode);
										System.out.println("[Universal Electricity] Injected " + template.className + " API into: " + cnode.name);
									}
								}
							}
						}
					}
					else
					{
						System.out.println("[Universal Electricity] Failed to inject class " + cnode.name + " due to missing required interfaces.");
					}

					break;
				}

			}
		}

		return changed ? ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_FRAMES) : bytes;
	}
}
