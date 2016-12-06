package com.builtbroken.mc.core.asm.template;

import com.builtbroken.mc.api.InjectTemplate;
import com.builtbroken.mc.core.EngineCoreMod;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Handles everything to do with ASM injection templates
 * <p>
 * Yes this is a lazy solution to a complex problem, but meh its by far easier to deal with than manually coding 10+ class per tile.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/11/2016.
 */
public class TemplateManager
{

    /** List of ASM injection templates to use on universal energy tiles */
    public static final HashMap<String, InjectionTemplate> templates = new HashMap();
    /** Map of objects that handle additional method calls needed for templates to work, such as {@link net.minecraft.tileentity.TileEntity#validate()} */
    public static final HashMap<String, ITemplateCalls> additionalTemplateCalls = new HashMap();
    /** Cache of classes to template calls to make, provides a slight performance gain */
    public static final HashMap<Class, List<ITemplateCalls>> classToTemplateCalls = new HashMap();

    //TODO create an ASM system that can inject method calls for ITemplateCalls automatically to save time
    public static void load()
    {
        EngineCoreMod.logger.info("TemplateManager: loading ASM templates...");
        try
        {
            //Load RF support
            if (classExists("cofh.api.energy.IEnergyHandler"))
            {
                templates.put("RF-IEnergyHandler", new InjectionTemplate("com.builtbroken.mc.lib.mod.compat.rf.TemplateTETile", Collections.singletonList("cofh.api.energy.IEnergyHandler")));
            }
            else
            {
                EngineCoreMod.logger.error("TemplateManager: Skipping RF support - class not found");
            }

            //Load IC2 support
            if (classExists("ic2.api.energy.tile.IEnergySink"))
            {
                templates.put("IC-IEnergySink", new InjectionTemplate("com.builtbroken.mc.lib.mod.compat.ic.ICTemplateTile", Collections.singletonList("ic2.api.energy.tile.IEnergySink")));
            }
            else
            {
                EngineCoreMod.logger.error("TemplateManager: Skipping IC2 support - class not found");
            }
        }
        catch (Exception e)
        {
            EngineCoreMod.logger.error("TemplateManager: Failed to load templates, ASM injection may fail or even crash", e);
        }
        EngineCoreMod.logger.info("TemplateManager: Finished loading...");
    }

    //Checks if a class exists
    private static boolean classExists(String clazz)
    {
        try
        {
            if (Class.forName(clazz, false, TemplateManager.class.getClassLoader()) != null)
            {
                return true;
            }
        }
        catch (ClassNotFoundException e2)
        {
            EngineCoreMod.logger.error("Failed to find class '" + clazz + "'");
        }
        return false;
    }

    /**
     * Checks to ensure that information about the class is already loaded
     *
     * @param object - some object that is being used that has
     *               ASM injected code inside of it
     */
    public static void checkLoad(Object object)
    {
        Class c = object.getClass();
        if (!classToTemplateCalls.containsKey(c) || classToTemplateCalls.get(c) == null)
        {
            List<ITemplateCalls> list = new ArrayList();
            InjectTemplate annotation = getAnnotation(c);
            if (annotation != null)
            {
                String string = annotation.integration();
                if (string != null)
                {
                    String[] split = string.contains(";") ? string.split(";") : new String[]{string};
                    for (String template : split)
                    {
                        if (additionalTemplateCalls.containsKey(template) && additionalTemplateCalls.get(template) != null)
                        {
                            list.add(additionalTemplateCalls.get(template));
                        }
                    }
                }
            }
            classToTemplateCalls.put(c, list);
        }
    }

    private static InjectTemplate getAnnotation(Class c)
    {
        Annotation[] annotations = c.getAnnotations();
        for (Annotation annotation : annotations)
        {
            if (annotation instanceof InjectTemplate)
            {
                return (InjectTemplate) annotation;
            }
        }
        return null;
    }
}
