package com.builtbroken.mc.framework.multiblock.listeners;

import com.builtbroken.mc.api.tile.multiblock.IMultiTile;
import com.builtbroken.mc.api.tile.node.ITileNode;
import com.builtbroken.mc.imp.transform.vector.Pos;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Temp listener used to pass interaction to a node. Will be replaced by {@link com.builtbroken.mc.api.tile.multiblock.IMultiBlockProvider}
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/3/2018.
 */
public interface IMultiBlockNodeListener extends ITileNode
{
    /**
     * Called when multi-block is added
     *
     * @param tileMulti   - tile
     * @param relativePos - relative position
     */
    void onMultiTileAdded(IMultiTile tileMulti, Pos relativePos);

    boolean onMultiTileActivated(IMultiTile tile, Pos relativePos, EntityPlayer player, int side, float xHit, float yHit, float zHit);

    default String getMultiBlockLayoutKey()
    {
        return null;
    }
}
