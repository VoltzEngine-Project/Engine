package calclavia.lib.asm;

import calclavia.lib.config.ConfigSet;
import calclavia.lib.utility.ASMUtility;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import universalelectricity.core.asm.ASMHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * @since 09/03/14
 * @author tgame14
 */
public class ConfigTransformer implements IClassTransformer
{
	public static final Set<String> classes = new HashSet<String>();

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes)
	{
		// no need to iterate over Forge, FML, and MC source
		if (transformedName.startsWith("net.minecraft") || transformedName.startsWith("cpw.mods.fml") || transformedName.startsWith("universalelectricity."))
			return bytes;

		ClassNode cnode = ASMHelper.createClassNode(bytes);

		for (FieldNode fnode : cnode.fields)
		{
			if (fnode != null && fnode.visibleAnnotations != null)
			{
				for (AnnotationNode anode : fnode.visibleAnnotations)
				{
					if (anode.desc.equals("Lcalclavia/lib/config/Config;"))
					{
						if (Loader.instance().hasReachedState(LoaderState.AVAILABLE))
						{
							// ASM Code here is to add a <clinit> codebase to fire an event after class load
							MethodNode clinit = ASMUtility.findOrCreateClinit(cnode);
							InsnList hook = new InsnList();
							hook.add(new LdcInsnNode(Type.getObjectType(cnode.name)));
							hook.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "calclavia/lib/asm/StaticForwarder", "onConfigClassLoad", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Class.class))));
							clinit.instructions.insert(hook);
							return ASMHelper.createBytes(cnode, 0);

						}
						classes.add(transformedName);
						return bytes;

					}
				}
			}
		}

		return bytes;

	}

}
