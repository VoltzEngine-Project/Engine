package com.builtbroken.mc.codegen.tests;

import com.builtbroken.mc.api.tile.IInventoryProvider;
import com.builtbroken.mc.framework.logic.ITileController;
import com.builtbroken.mc.framework.logic.annotations.ExternalInventoryWrapped;
import com.builtbroken.mc.framework.logic.annotations.LogicTile;
import com.builtbroken.mc.prefab.inventory.ExternalInventory;

/**
 * Test tile to see if {@link com.builtbroken.mc.codegen.Main} functions correctly for tile processing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@LogicTile(id = "codeGenTestTile", className = "TileEntityWrapperTest")
@ExternalInventoryWrapped
public class TileTest implements ITileController, IInventoryProvider<ExternalInventory>
{
    public ExternalInventory inventory;

    @Override
    public void update(long tick)
    {

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
}
