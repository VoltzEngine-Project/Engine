package com.builtbroken.mc.framework.json.data;

import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/26/2017.
 */
public class JsonBlockEntry
{
    /** Reference to a block */
    protected String block;

    public JsonBlockEntry(String block)
    {
        this.block = block;
    }

    public String getRegistryName()
    {
        return block;
    }

    public Block getBlock()
    {
        if (block != null && !block.isEmpty())
        {
            return InventoryUtility.getBlock(block);
        }
        return null;
    }
}
