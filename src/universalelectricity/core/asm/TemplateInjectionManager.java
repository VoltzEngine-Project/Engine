package universalelectricity.core.asm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import net.minecraft.launchwrapper.LaunchClassLoader;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * 
 * @author Calclavia, ChickenBones
 * 
 */
public class TemplateInjectionManager
{
	private static LaunchClassLoader cl = (LaunchClassLoader) ClassHeirachyManager.class.getClassLoader();

	public static class InjectionTemplate
	{
		/**
		 * The Java class name.
		 */
		public final String className;

		/**
		 * The methods to be injected upon patch(ClassNode cnode);
		 */
		public ArrayList<MethodNode> methodImplementations = new ArrayList<MethodNode>();

		public InjectionTemplate(String className)
		{
			this.className = className;

			ClassNode cnode = getClassNode(className);

			for (MethodNode method : cnode.methods)
			{
				this.methodImplementations.add(method);
				method.desc = new ObfMapping(cnode.name, method.name, method.desc).toRuntime().s_desc;
			}
		}

		/**
		 * Patches the cnode with the methods from this template.
		 * 
		 * @param cnode
		 * @return
		 */
		public boolean patch(ClassNode cnode)
		{
			LinkedList<String> names = new LinkedList<String>();

			for (MethodNode method : cnode.methods)
			{
				ObfMapping m = new ObfMapping(cnode.name, method.name, method.desc).toRuntime();
				names.add(m.s_name + m.s_desc);
			}

			boolean changed = false;
			for (MethodNode impl : this.methodImplementations)
			{
				if (names.contains(impl.name + impl.desc))
				{
					continue;
				}

				MethodNode copy = new MethodNode(impl.access, impl.name, impl.desc, impl.signature, impl.exceptions == null ? null : impl.exceptions.toArray(new String[0]));
				ASMHelper.copy(impl, copy);
				cnode.methods.add(impl);
				changed = true;
			}
			return changed;
		}
	}

	static HashMap<Class, InjectionTemplate> injectionTemplates = new HashMap<Class, InjectionTemplate>();

	/**
	 * 
	 * @param templateClass - The abstract class holding the template.
	 * @param cname
	 */
	public static void registerDefaultImpl(Class templateClass)
	{
		injectionTemplates.put(templateClass, new InjectionTemplate(templateClass.getName()));
	}

	private static ClassNode getClassNode(String name)
	{
		try
		{
			return ASMHelper.createClassNode(cl.getClassBytes(name.replace('/', '.')));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

}