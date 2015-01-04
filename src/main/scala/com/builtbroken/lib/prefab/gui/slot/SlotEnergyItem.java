package com.builtbroken.lib.prefab.gui.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import com.builtbroken.lib.mod.compat.energy.Compatibility;

public class SlotEnergyItem extends Slot
{
	public SlotEnergyItem(IInventory inv, int par3, int par4, int par5)
	{
		super(inv, par3, par4, par5);
	}

    @Override
    public boolean isItemValid(ItemStack compareStack)
    {
        return Compatibility.isHandler(compareStack, null);
    }

}
