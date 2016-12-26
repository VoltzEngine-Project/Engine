package com.builtbroken.mc.prefab.module;

import com.builtbroken.mc.api.IHasMass;
import com.builtbroken.mc.api.items.IItemHasMass;
import com.builtbroken.mc.api.modules.IModule;
import com.builtbroken.mc.api.modules.IModuleHasMass;
import com.builtbroken.mc.api.modules.IModuleItem;
import com.builtbroken.mc.prefab.items.ItemAbstract;
import net.minecraft.item.ItemStack;

/**
 * Prefab for implementing missile modules
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 10/26/2015.
 */
public abstract class ItemAbstractModule<I extends IModule> extends ItemAbstract implements IModuleItem, IItemHasMass
{
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        IModule module = getModule(stack);
        if (module != null)
        {
            return module.getUnlocalizedName();
        }
        return super.getUnlocalizedName(stack);
    }

    @Override
    public I getModule(ItemStack stack)
    {
        if (stack != null)
        {
            ItemStack insert = stack.copy();
            insert.stackSize = 1;
            return newModule(stack);
        }
        return null;
    }

    protected abstract I newModule(ItemStack stack);

    @Override
    public double getMass(ItemStack stack)
    {
        IModule module = getModule(stack);
        if (module instanceof IModuleHasMass)
        {
            return ((IModuleHasMass) module).getMass() + ((IModuleHasMass) module).getSubPartMass();
        }
        else if (module instanceof IHasMass)
        {
            return ((IHasMass) module).getMass();
        }
        return -1;
    }
}
