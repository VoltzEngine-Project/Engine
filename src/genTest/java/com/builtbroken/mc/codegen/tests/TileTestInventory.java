package com.builtbroken.mc.codegen.tests;

import com.builtbroken.mc.api.tile.provider.IInventoryProvider;
import com.builtbroken.mc.framework.logic.TileNode;
import com.builtbroken.mc.codegen.annotations.ExternalInventoryWrapped;
import com.builtbroken.mc.codegen.annotations.TileWrapped;
import com.builtbroken.mc.prefab.inventory.ExternalInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Test tile to see if {@link com.builtbroken.mc.codegen.Main} functions correctly for tile processing
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/31/2017.
 */
@TileWrapped(className = ".tile.TileEntityWrapperTestInventory")
@ExternalInventoryWrapped()
public final class TileTestInventory extends TileNode implements IInventoryProvider<ExternalInventory>
{
    public ExternalInventory inventory;

    public TileTestInventory()
    {
        super("tile.test.inventory", "null");
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
    public boolean canStore(ItemStack stack, ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean canRemove(ItemStack stack, ForgeDirection side)
    {
        return true;
    }
}
