package resonant.lib.prefab.slot;

import ic2.api.item.IElectricItem;
import net.minecraft.inventory.IInventory;
import universalelectricity.api.item.IEnergyItem;
import cofh.api.energy.IEnergyContainerItem;

public class SlotEnergyItem extends SlotSpecific
{
    public SlotEnergyItem(IInventory par2iInventory, int par3, int par4, int par5)
    {
        super(par2iInventory, par3, par4, par5, IEnergyItem.class, IElectricItem.class, IEnergyContainerItem.class);
    }

}
