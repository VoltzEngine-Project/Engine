package com.builtbroken.mc.lib.mod.compat.bc;

import buildcraft.api.tools.IToolWrench;
import com.builtbroken.mc.lib.helper.WrenchUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Proxy for support BC wrench interface
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/26/2016.
 */
public class BCWrenchProxy extends WrenchUtility.WrenchType
{
    @Override
    public boolean isWrench(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            return item instanceof IToolWrench;
        }
        return false;
    }

    @Override
    public boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            return item instanceof IToolWrench && ((IToolWrench) item).canWrench(player, x, y, z);
        }
        return false;
    }
}
