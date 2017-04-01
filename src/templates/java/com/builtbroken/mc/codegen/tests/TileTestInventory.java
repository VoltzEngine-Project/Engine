package com.builtbroken.mc.codegen.tests;

import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.framework.logic.TileNode;
import com.builtbroken.mc.framework.logic.annotations.ExternalInventoryWrapped;
import com.builtbroken.mc.framework.logic.annotations.TileWrapped;
import com.builtbroken.mc.prefab.inventory.ExternalInventory;

/**
 * Test tile to see if {@link com.builtbroken.mc.codegen.Main} functions correctly for tile processing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@TileWrapped(id = "codeGenTestTileInventory", className = "TileEntityWrapperTestInventory")
@ExternalInventoryWrapped()
public final class TileTestInventory extends TileNode implements IInventoryProvider<ExternalInventory>
{
    public ExternalInventory inventory;

    @Override
    public ExternalInventory getInventory()
    {
        if (inventory == null)
        {
            inventory = new ExternalInventory(this, 10);
        }
        return inventory;
    }
}
