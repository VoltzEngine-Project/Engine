package com.builtbroken.mc.framework.block.tile;

import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.framework.block.BlockBase;
import com.builtbroken.mc.framework.block.meta.BlockMeta;
import com.builtbroken.mc.framework.block.meta.MetaData;
import com.builtbroken.mc.framework.json.IJsonGenMod;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public class TileProviderMeta extends JsonGenData implements ITileProvider
{
    public ITileProvider backupProvider;

    public TileProviderMeta()
    {
        super(null);
    }

    @Override
    public TileEntity createNewTileEntity(BlockBase block, World world, int meta)
    {
        if (block instanceof BlockMeta && ((BlockMeta) block).metaDataValues[meta] != null && ((BlockMeta) block).metaDataValues[meta].tileEntityProvider != null)
        {
            return ((BlockMeta) block).metaDataValues[meta].tileEntityProvider.createNewTileEntity(block, world, meta);
        }
        return backupProvider != null ? backupProvider.createNewTileEntity(block, world, meta) : null;
    }

    @Override
    public void register(BlockBase block, IJsonGenMod mod, ModManager manager)
    {
        if (block instanceof BlockMeta)
        {
            for (MetaData data : ((BlockMeta) block).metaDataValues)
            {
                if (data != null && data.tileEntityProvider != null)
                {
                    data.tileEntityProvider.register(block, mod, manager);
                }
            }
        }
        if (backupProvider != null)
        {
            backupProvider.register(block, mod, manager);
        }
    }

    @Override
    public String getContentID()
    {
        return null;
    }
}
