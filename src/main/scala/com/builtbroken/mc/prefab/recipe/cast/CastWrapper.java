package com.builtbroken.mc.prefab.recipe.cast;

import com.builtbroken.jlib.type.Pair;
import com.builtbroken.mc.prefab.items.ItemStackWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/** Simple wrapper for Casting Item to FluidStack
 * Created by Dark on 6/23/2015.
 */
public class CastWrapper extends Pair<FluidStack, ItemStackWrapper>
{
    public CastWrapper(FluidStack left, ItemStackWrapper right)
    {
        super(left, right);
    }

    public FluidStack fluidStack()
    {
        return left();
    }

    public Fluid fluid()
    {
        return left() != null ? left().getFluid() : null;
    }

    public ItemStack itemStack()
    {
        return right() != null ? right().itemStack : null;
    }
}
