package com.builtbroken.mc.core.asm.template;

import com.builtbroken.mc.lib.asm.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Applies templates to objects at runtime using byte code manipulation
 *
 * @author Calclavia, DarkGuardsman
 */
public class ClassTransformer implements IClassTransformer
{
    //TODO http://stackoverflow.com/questions/5346908/generating-a-hello-world-class-with-the-java-asm-library
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        //Only functions on our classes
        if (transformedName.startsWith("net.minecraft") || TemplateManager.templates.isEmpty())
        {
            return bytes;
        }

        boolean changed = false;

        ClassNode cnode = ASMHelper.createClassNode(bytes);

        if (cnode != null && cnode.visibleAnnotations != null)
        {
            for (AnnotationNode nodes : cnode.visibleAnnotations)
            {
                if (nodes.desc.equals("Lcom/builtbroken/mc/api/InjectTemplate;"))
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

                    changed |= injectTemplate(cnode, flags);
                    break;
                }
            }
        }

        if (changed)
        {
            byte[] data = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_FRAMES);

            if (System.getProperty("development") != null && System.getProperty("development").equalsIgnoreCase("true"))
            {
                try
                {
                    File file = new File(".", name + ".class");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return data;
        }
        return bytes;
    }

    private boolean injectTemplate(ClassNode cnode, String flags)
    {
        boolean changed = false;

        if (flags == null || flags.equals(""))
        {
            for (InjectionTemplate template : TemplateManager.templates.values())
            {
                if (template != null)
                {
                    changed |= template.patch(cnode, false);
                    System.out.println("[Universal Electricity] Injected " + template.className + " API into: " + cnode.name);
                }
            }
        }
        else
        {
            String[] separatedFlags = flags.split(";");

            for (String templateKey : separatedFlags)
            {
                if (InjectionTemplate.getTemplate(templateKey) != null)
                {
                    InjectionTemplate template = InjectionTemplate.getTemplate(templateKey);

                    if (template != null)
                    {
                        changed |= template.patch(cnode, false);
                        System.out.println("[Universal Electricity] Injected " + template.className + " API into: " + cnode.name);
                    }
                }
            }
        }
        return changed;
    }
}