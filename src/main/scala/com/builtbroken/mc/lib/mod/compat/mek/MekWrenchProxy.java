package com.builtbroken.mc.lib.mod.compat.mek;

import com.builtbroken.mc.lib.helper.WrenchUtility;
import mekanism.api.IMekWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Proxy for support Mekanism wrench interface
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 7/13/2016.
 */
public class MekWrenchProxy extends WrenchUtility.WrenchType
{
    @Override
    public boolean isWrench(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            return item instanceof IMekWrench;
        }
        return false;
    }

    @Override
    public boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            return item instanceof IMekWrench && ((IMekWrench) item).canUseWrench(player, x, y, z);
        }
        return false;
    }
}
