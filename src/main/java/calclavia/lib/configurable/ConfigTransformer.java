package calclavia.lib.configurable;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import universalelectricity.core.asm.ASMHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * @since 09/03/14
 * @author tgame14
 */
public class ConfigTransformer implements IClassTransformer
{
    public static Set<String> classes = new HashSet<String>();

    @Override
    public byte[] transform (String name, String transformedName, byte[] bytes)
    {
        if (transformedName.startsWith("net.minecraft"))
            return bytes;

        ClassNode cnode = ASMHelper.createClassNode(bytes);

        for (FieldNode fnode : cnode.fields)
        {
            for (AnnotationNode anode : fnode.visibleAnnotations)
            {
                if (anode.desc.equals("Lcalclavia/configurable/Config;"))
                {
                    if (!classes.contains(fnode.name))
                        classes.add(fnode.name);
                }
            }
        }

        return bytes;

    }


}
