package com.builtbroken.mc.prefab.tile.interfaces.tile;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * Applied to a {@link com.builtbroken.systemd.TileD} or wrappers that act as call back for
 * {@link net.minecraft.block.Block} method calls that can not be
 * directed towards a {@link com.builtbroken.systemd.TileD} {@link net.minecraft.tileentity.TileEntity} object
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/11/2016.
 */
public interface IBlockTile extends ITile
{
    /**
     * Sets the access pointer for the world
     *
     * @param access - access object
     */
    void setWorldAccess(IBlockAccess access);

    /** Sets the location of the tile */
    void setLocation(int x, int y, int z);

    float getExplosionResistance(Entity entity);

    int tickRate();

    void getSubBlocks(Item item, CreativeTabs creativeTabs, List list);

    int quantityDropped(int meta, int fortune);
}
