package com.builtbroken.mc.lib.world.map.data;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.helper.NBTUtility;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.io.File;
import java.util.HashMap;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/21/2017.
 */
public abstract class ChunkMapManager<M extends ChunkMap>
{
    protected final HashMap<Integer, M> worldMaps = new HashMap();

    /** Name of the folder */
    public final String saveFolder;
    /** Prefix name of saved files */
    public final String fileSaveName;

    public ChunkMapManager(String saveFolder, String saveFileName)
    {
        this.saveFolder = saveFolder;
        this.fileSaveName = saveFileName;
        if (!Engine.isJUnitTest())
        {
            MinecraftForge.EVENT_BUS.register(this);
            FMLCommonHandler.instance().bus().register(this);
        }
    }

    public M getMap(World world, boolean create)
    {
        return getMapForDim(world.provider.getDimension(), create);
    }

    public M getMapForDim(int dim, boolean create)
    {
        if (worldMaps.containsKey(dim))
        {
            return worldMaps.get(dim);
        }
        else if (create)
        {

            loadOrCreate(dim);
            return worldMaps.get(dim);
        }
        return null;
    }

    protected void loadOrCreate(int dim)
    {
        File file = getSaveFile(dim);
        M map = createNewMap(dim);
        if (file.exists())
        {
            NBTTagCompound tag = NBTUtility.loadData(file);
            map.load(tag);
            map.onLoad();
        }
        worldMaps.put(dim, map);
    }

    public File getSaveFile(int dimID)
    {
        return new File(NBTUtility.getSaveDirectory(), NBTUtility.BBM_FOLDER + saveFolder + "/" + fileSaveName + "_" + dimID + ".dat");
    }

    protected abstract M createNewMap(int dim);

    @SubscribeEvent
    public void worldUpdateTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.provider != null && event.side == Side.SERVER && event.phase == TickEvent.Phase.END)
        {
            int dim = event.world.provider.getDimension();
            if (worldMaps.containsKey(dim))
            {
                M map = getMapForDim(dim, false);
                if (map.shouldUnload())
                {
                    worldMaps.remove(dim);
                }
                else
                {
                    map.update();
                }
            }
        }
    }

    @SubscribeEvent
    public void worldUnload(WorldEvent.Unload event)
    {
        if (event.getWorld().provider != null)
        {
            int dim = event.getWorld().provider.getDimension();
            if (worldMaps.containsKey(dim))
            {
                M map = getMapForDim(dim, false);
                map.onWorldUnload();
                worldMaps.remove(dim);
            }
        }
    }
}
