package com.builtbroken.mc.core.asm.template;


import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.asm.ASMHelper;
import com.builtbroken.mc.lib.asm.ObfMapping;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InjectionTemplate
{
    /**
     * The Java class name.
     */
    public final String className;
    public final List<String> interfaces;

    private boolean init = false;
    private boolean failedToLoadClass = false;

    /**
     * The methods to be injected upon patch(ClassNode cnode);
     */
    public ArrayList<MethodNode> methodImplementations = new ArrayList();

    /**
     * Gets a template for the @Annotaion key value
     *
     * @param key - string of the power system
     * @return template if contained
     */
    public static InjectionTemplate getTemplate(String key)
    {
        return TemplateManager.templates.containsKey(key) ? TemplateManager.templates.get(key) : null;
    }

    public InjectionTemplate(String className, List<String> interfaces)
    {
        this.className = className;
        this.interfaces = interfaces;
        Engine.logger().info("TemplateManager: Loading template for " + className + " as long as interfaces " + interfaces + " exist");
    }

    /**
     * Patches the cnode with the methods from this template.
     *
     * @param cnode
     * @return
     */
    public boolean patch(ClassNode cnode, boolean injectConstructor)
    {
        //Init data, if return true then init failed
        if (init())
        {
            return false;
        }

        for (String interfaceName : this.interfaces)
        {
            String interfaceByteName = interfaceName.replace(".", "/");

            if (!cnode.interfaces.contains(interfaceByteName))
            {
                cnode.interfaces.add(interfaceByteName);
            }
            else
            {
                return false;
            }
        }

        boolean changed = false;

        LinkedList<String> names = new LinkedList<String>();

        for (MethodNode method : cnode.methods)
        {
            ObfMapping m = new ObfMapping(cnode.name, method.name, method.desc).toRuntime();
            names.add(m.s_name + m.s_desc);
        }

        for (MethodNode impl : this.methodImplementations)
        {
            if (!impl.name.equals("<init>") || injectConstructor)
            {
                /**
                 * If the method is ALREADY implemented, then skip it.
                 */
                if (names.contains(impl.name + impl.desc))
                {
                    continue;
                }

                ObfMapping mapping = new ObfMapping(cnode.name, impl.name, impl.desc).toRuntime();
                MethodNode copy = new MethodNode(impl.access, mapping.s_name, mapping.s_desc, impl.signature, impl.exceptions == null ? null : impl.exceptions.toArray(new String[0]));
                ASMHelper.copy(impl, copy);
                cnode.methods.add(impl);
                changed = true;
            }
        }

        return changed;
    }

    //Called late to avoid loading class data in the constructor
    private boolean init()
    {
        if (!init)
        {
            init = true;
            try
            {
                final ClassNode cnode = ASMHelper.createClassNode(((LaunchClassLoader) InjectionTemplate.class.getClassLoader()).getClassBytes(className.replace('/', '.')));
                for (MethodNode method : cnode.methods)
                {
                    this.methodImplementations.add(method);
                    method.desc = new ObfMapping(cnode.name, method.name, method.desc).toRuntime().s_desc;
                }
            }
            catch (IOException e)
            {
                //TODO error out in dev mode
                //TODO make notation to users that this injector failed
                e.printStackTrace();
                failedToLoadClass = true;
            }
        }
        return failedToLoadClass;
    }

}