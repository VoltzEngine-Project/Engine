package com.builtbroken.mc.seven.abstraction.data;

import com.builtbroken.mc.api.abstraction.data.ITileData;
import net.minecraft.block.Block;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/15/2017.
 */
public class TileData implements ITileData
{
    public final Block block;

    public TileData(Block block)
    {
        this.block = block;
    }

    @Override
    public Block unwrap()
    {
        return block;
    }
}
