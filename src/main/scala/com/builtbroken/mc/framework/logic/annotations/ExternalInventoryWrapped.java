package com.builtbroken.mc.framework.logic.annotations;

import net.minecraft.inventory.IInventory;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
public @interface ExternalInventoryWrapped
{
    Class<? extends IInventory> type();
}
