package com.builtbroken.mc.lib.mod.compat.pe;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.content.tool.ItemSheetMetalTools;
import com.builtbroken.mc.core.content.tool.ItemSimpleCraftingTool;
import com.builtbroken.mc.lib.mod.ModProxy;
import com.builtbroken.mc.lib.mod.compat.Mods;
import moze_intel.projecte.utils.NBTWhitelist;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/31/2016.
 */
public class ProjectEProxy extends ModProxy
{
    public ProjectEProxy()
    {
        super(Mods.PROJECT_E);
    }

    @Override
    public void init()
    {
        super.init();
        if(Engine.itemSheetMetalTools != null)
        {
            NBTWhitelist.register(ItemSheetMetalTools.getHammer());
            NBTWhitelist.register(ItemSheetMetalTools.getShears());
        }
        if(Engine.itemSimpleCraftingTools != null)
        {
            for(ItemStack stack : ItemSimpleCraftingTool.getTools())
            {
                NBTWhitelist.register(stack);
            }
        }
    }
}
