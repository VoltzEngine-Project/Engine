package universalelectricity.basiccomponents;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import universalelectricity.extend.IItemElectric;

public class ContainerBatteryBox extends Container
{
    private TileEntityBatteryBox batteryBox;

    public ContainerBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityBatteryBox batteryBox)
    {
        this.batteryBox = batteryBox;
        this.addSlotToContainer(new SlotElectricItem(batteryBox, 0, 33, 24)); //Top slot
        this.addSlotToContainer(new SlotElectricItem(batteryBox, 1, 33, 48)); //Bottom slot
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.batteryBox.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    @Override
    public ItemStack transferStackInSlot(int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 != 0 && par1 != 1)
            {
                if (var4.getItem() instanceof IItemElectric)
                {
                    if (((IItemElectric)var4.getItem()).canProduceElectricity())
                    {
                        if (!this.mergeItemStack(var4, 1, 2, false))
                        {
                            return null;
                        }
                    }
                    else
                    {
                        if (!this.mergeItemStack(var4, 0, 1, false))
                        {
                            return null;
                        }
                    }
                }
                else if (par1 >= 30 && par1 < 38 && !this.mergeItemStack(var4, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 3, 38, false))
            {
                return null;
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.onPickupFromSlot(var4);
        }

        return var2;
    }
}
