package com.builtbroken.mc.codegen.tests;

import com.builtbroken.mc.api.energy.IEnergyBuffer;
import com.builtbroken.mc.api.energy.IEnergyBufferProvider;
import com.builtbroken.mc.api.tile.provider.IInventoryProvider;
import com.builtbroken.mc.codegen.annotations.TileWrapped;
import com.builtbroken.mc.framework.energy.data.EnergyBuffer;
import com.builtbroken.mc.framework.logic.TileNode;
import com.builtbroken.mc.prefab.inventory.ExternalInventory;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Test tile to see if CodeGen functions correctly for tile processing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@TileWrapped(className = ".tile.TileEntityWrapperTestEnergyInv", wrappers = "Energy;ExternalInventory")
public final class TileTestEnergyInventory extends TileNode implements IEnergyBufferProvider, IInventoryProvider<ExternalInventory>
{
    protected EnergyBuffer buffer;

    public ExternalInventory inventory;

    public TileTestEnergyInventory()
    {
        super("tile.test.energyInv", "null");
    }

    @Override
    public ExternalInventory getInventory()
    {
        if (inventory == null)
        {
            inventory = new ExternalInventory(this, 10);
        }
        return inventory;
    }

    @Override
    public IEnergyBuffer getEnergyBuffer(ForgeDirection side)
    {
        if(buffer == null)
        {
            buffer = new EnergyBuffer(10000);
        }
        return buffer;
    }
}
