package com.builtbroken.mc.seven.framework.block.tile;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.ModManager;
import com.builtbroken.mc.seven.framework.block.BlockBase;
import com.builtbroken.mc.framework.json.IJsonGenMod;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/3/2017.
 */
public class TileProviderByClass extends JsonGenData implements ITileProvider
{
    public final String id;
    public final Class<? extends TileEntity> clazz;

    public TileProviderByClass(IJsonProcessor processor, String id, String clazz) throws ClassNotFoundException
    {
        super(processor);
        this.id = id;
        this.clazz = (Class<? extends TileEntity>) Class.forName(clazz);
    }

    @Override
    public TileEntity createNewTileEntity(BlockBase block, World world, int meta)
    {
        return create(clazz);
    }

    protected final TileEntity create(Class<? extends TileEntity> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            Engine.logger().error("Failed to create tile for TileEntity<" + id + ", " + clazz + ">", e);
        }
        catch (IllegalAccessException e)
        {
            Engine.logger().error("Failed to access constructor for TileEntity<" + id + ", " + clazz + ">", e);
        }
        return null;
    }

    @Override
    public void register(BlockBase block, IJsonGenMod mod, ModManager manager)
    {
        manager.registerTileEntity(id, block, create(clazz));
    }

    @Override
    public String getContentID()
    {
        return id;
    }
}
