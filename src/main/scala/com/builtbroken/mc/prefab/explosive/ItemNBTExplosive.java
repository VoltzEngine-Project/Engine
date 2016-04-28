package com.builtbroken.mc.prefab.explosive;

import com.builtbroken.mc.api.explosive.IExplosiveHandler;
import com.builtbroken.mc.api.items.explosives.IExplosiveItem;
import com.builtbroken.mc.lib.world.explosive.ExplosiveItemUtility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/28/2016.
 */
public class ItemNBTExplosive extends Item implements IExplosiveItem
{
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        IExplosiveHandler handler = getExplosive(stack);
        if (handler != null)
        {
            return handler.getTranslationKey();
        }
        return super.getUnlocalizedName(stack);
    }


    public boolean setExplosive(ItemStack stack, IExplosiveHandler ex, double size, NBTTagCompound nbt)
    {
        if (stack != null && size > 0)
        {
            if (stack.getTagCompound() == null)
            {
                stack.setTagCompound(new NBTTagCompound());
            }

            ExplosiveItemUtility.setExplosive(stack, ex);
            ExplosiveItemUtility.setSize(stack, size);
            if (nbt != null)
            {
                stack.getTagCompound().setTag("exData", nbt);
            }
            else if (stack.getTagCompound().hasKey("exData"))
            {
                stack.getTagCompound().removeTag("exData");
            }
            return true;
        }
        return false;
    }

    @Override
    public NBTTagCompound getAdditionalExplosiveData(ItemStack stack)
    {
        if(stack.getTagCompound() == null)
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.getTagCompound().hasKey("exData"))
        {
            stack.getTagCompound().setTag("exData", new NBTTagCompound());
        }
        return stack.getTagCompound().getCompoundTag("exData");
    }

    @Override
    public double getExplosiveSize(ItemStack stack)
    {
        return ExplosiveItemUtility.getSize(stack);
    }

    @Override
    public IExplosiveHandler getExplosive(ItemStack stack)
    {
        return ExplosiveItemUtility.getExplosive(stack);
    }
}
