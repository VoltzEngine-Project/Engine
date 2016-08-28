package com.builtbroken.mc.core.asm.template;

import com.builtbroken.mc.core.EngineCoreMod;
import com.builtbroken.mc.lib.asm.ASMHelper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Applies templates to objects at runtime using byte code manipulation
 *
 * @author Calclavia, DarkGuardsman
 */
public class ClassTransformer implements IClassTransformer
{
    //TODO http://stackoverflow.com/questions/5346908/generating-a-hello-world-class-with-the-java-asm-library

    /** List of class paths that are acceptable for this transformer to process */
    public static final List<String> permittedClassPaths = new ArrayList();
    /** Prefixes of the classes that should be processed */
    public static final List<String> permittedPrefixClass = new ArrayList();

    static
    {
        permittedClassPaths.add("com.builtbroken");
        permittedPrefixClass.add("Tile");
        permittedPrefixClass.add("TileEntity");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        try
        {
            //Only functions on our classes
            if (!shouldProcess(transformedName) || TemplateManager.templates.isEmpty())
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
                        File file = new File(".", "asmTestFolder/" + name + ".class");
                        if (!file.getParentFile().exists())
                        {
                            file.mkdirs();
                        }
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
        }
        catch (Exception e)
        {
            throw new RuntimeException("TemplateClassTransformer: Failed to process class " + transformedName, e);
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
                    EngineCoreMod.logger.info("TemplateClassTransformer: Injected " + template.className + " API into: " + cnode.name);
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
                        EngineCoreMod.logger.info("TemplateClassTransformer: Injected " + template.className + " API into: " + cnode.name);
                    }
                }
            }
        }
        return changed;
    }

    //Checks if the class(name) starts with an acceptable class path
    private boolean shouldProcess(String name)
    {
        if (name != null && !name.isEmpty())
        {
            for (String path : permittedClassPaths)
            {
                if (permittedClassPaths.equals(name) || name.startsWith(path))
                {
                    final String className = name.replace(".class", "").substring(name.lastIndexOf(".") + 1, name.length());
                    for (String prefix : permittedPrefixClass)
                    {
                        if (className.startsWith(prefix))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}