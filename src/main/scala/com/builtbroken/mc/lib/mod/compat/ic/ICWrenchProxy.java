package com.builtbroken.mc.lib.mod.compat.ic;

import com.builtbroken.mc.lib.helper.WrenchUtility;
import ic2.core.item.tool.ItemToolWrench;
import ic2.core.item.tool.ItemToolWrenchElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Proxy for support IC2 wrench interface
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/26/2016.
 */
public class ICWrenchProxy extends WrenchUtility.WrenchType
{
    @Override
    public boolean isWrench(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemToolWrench || itemStack.getItem() instanceof ItemToolWrenchElectric)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
    {
        if (itemStack != null)
        {
            if (itemStack.getItem() instanceof ItemToolWrench || itemStack.getItem() instanceof ItemToolWrenchElectric)
            {
                return itemStack.getItemDamage() < itemStack.getMaxDamage();
            }
        }
        return false;
    }
}
