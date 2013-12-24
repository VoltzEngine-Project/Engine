/**
 * 
 */
package universalelectricity.core.asm;

import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ISpecialElectricItem;

import java.util.HashMap;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import universalelectricity.api.CompatibilityType;
import universalelectricity.api.energy.IConductor;
import universalelectricity.api.energy.IEnergyInterface;
import universalelectricity.api.item.IEnergyItem;
import universalelectricity.core.asm.TemplateInjectionManager.InjectionTemplate;
import universalelectricity.core.asm.template.item.TemplateICItem;
import universalelectricity.core.asm.template.item.TemplateTEItem;
import universalelectricity.core.asm.template.tile.TemplateICTile;
import universalelectricity.core.asm.template.tile.TemplateTETile;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;

/**
 * @author Calclavia
 * 
 */
public class UniversalTransformer implements IClassTransformer
{
	static LaunchClassLoader cl = (LaunchClassLoader) UniversalTransformer.class.getClassLoader();

	static
	{
		TemplateInjectionManager.registerTileTemplate(CompatibilityType.THERMAL_EXPANSION.moduleName, TemplateTETile.class, IEnergyHandler.class);
		TemplateInjectionManager.registerTileTemplate(CompatibilityType.INDUSTRIALCRAFT.moduleName, TemplateICTile.class, IEnergySink.class, IEnergySource.class);

		TemplateInjectionManager.registerItemTemplate(CompatibilityType.THERMAL_EXPANSION.moduleName, TemplateTEItem.class, IEnergyContainerItem.class);
		TemplateInjectionManager.registerItemTemplate(CompatibilityType.INDUSTRIALCRAFT.moduleName, TemplateICItem.class, ISpecialElectricItem.class);
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		if (transformedName.startsWith("net.minecraft") || TemplateInjectionManager.tileTemplates.isEmpty())
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
					else if (cnode.interfaces.contains(IEnergyItem.class.getName().replace(".", "/")))
					{
						transformationType = 2;
					}

					if (transformationType == 0 || transformationType == 1)
					{
						changed |= injectTemplate(cnode, flags, TemplateInjectionManager.tileTemplates);
					}
					else if (transformationType == 2)
					{
						changed |= injectTemplate(cnode, flags, TemplateInjectionManager.itemTemplates);
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

	private boolean injectTemplate(ClassNode cnode, String flags, HashMap<String, InjectionTemplate> templates)
	{
		boolean changed = false;

		if (flags == null || flags.equals(""))
		{
			for (InjectionTemplate template : templates.values())
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
					InjectionTemplate template = templates.get(separated);

					if (template != null)
					{
						changed |= template.patch(cnode);
						System.out.println("[Universal Electricity] Injected " + template.className + " API into: " + cnode.name);
					}
				}
			}
		}

		return changed;
	}
}
