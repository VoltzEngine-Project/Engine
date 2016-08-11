package com.builtbroken.mc.core.asm.template;

import com.builtbroken.mc.lib.mod.compat.ic.ICStaticForwarder;
import com.builtbroken.mc.lib.mod.compat.ic.ICTemplateTile;
import com.builtbroken.mc.lib.mod.compat.rf.TemplateTETile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/11/2016.
 */
public class TemplateManager
{
    /** List of ASM injection templates to use on universal energy tiles */
    public static final HashMap<String, InjectionTemplate> templates = new HashMap();
    public static final HashMap<String, ITemplateCalls> additionalTemplateCalls = new HashMap();
    public static final HashMap<Class, List<ITemplateCalls>> classToTemplateCalls = new HashMap();

    public static void load()
    {
        try
        {
            if (Class.forName("cofh.api.energy.IEnergyHandler") != null)
            {
                templates.put("RF-IEnergyHandler", new InjectionTemplate(TemplateTETile.class.getName(), Collections.singletonList("cofh.api.energy.IEnergyHandler")));
            }
            if(Class.forName("ic2.api.energy.tile.IEnergySink") != null)
            {
                templates.put("RF-IEnergySink", new InjectionTemplate(ICTemplateTile.class.getName(), Collections.singletonList("cofh.api.energy.IEnergySink")));
                additionalTemplateCalls.put("RF-IEnergySink", new ICStaticForwarder());
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
