package com.builtbroken.mc.prefab.items;

import com.builtbroken.mc.lib.helper.LanguageUtility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/14/2017.
 */
public class ItemBlockBase extends ItemBlockAbstract
{
    public boolean hasDetailedInfo = true;
    public boolean hasShiftInfo = true;

    public ItemBlockBase(Block p_i45328_1_)
    {
        super(p_i45328_1_);
    }

    @Override
    protected boolean hasDetailedInfo(ItemStack stack, EntityPlayer player)
    {
        return hasDetailedInfo && super.hasDetailedInfo(stack, player);
    }

    @Override
    protected boolean hasShiftInfo(ItemStack stack, EntityPlayer player)
    {
        if (hasShiftInfo)
        {
            final String translationKey = getUnlocalizedName(stack) + ".info.detailed";
            final String translation = LanguageUtility.getLocal(translationKey);
            return !translation.trim().isEmpty() && !translation.equals(translationKey);
        }
        return false;
    }
}
