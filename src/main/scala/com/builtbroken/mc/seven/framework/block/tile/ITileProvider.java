package com.builtbroken.mc.seven.framework.block.tile;

import com.builtbroken.mc.framework.json.IJsonGenMod;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public interface ITileProvider extends IJsonGenObject
{
    TileEntity createNewTileEntity(BlockBase block, World world, int meta);

    void register(BlockBase block, IJsonGenMod mod);
}
