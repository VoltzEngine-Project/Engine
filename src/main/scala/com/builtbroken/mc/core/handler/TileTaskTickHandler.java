package com.builtbroken.mc.core.handler;

import com.builtbroken.mc.core.Engine;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Handles tasks that need to be done outside of the main world tick loop
 * Created by Dark on 8/20/2015.
 */
public class TileTaskTickHandler
{
    public static final TileTaskTickHandler INSTANCE = new TileTaskTickHandler();

    private List<TileEntity> removeList = new ArrayList();

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.isRemote)
        {
            FMLCommonHandler.instance().bus().unregister(this);
        }

        if (event.phase == TickEvent.Phase.END && removeList.size() > 0)
        {
            Iterator<TileEntity> it = removeList.iterator();
            while (it.hasNext())
            {
                TileEntity tile = it.next();
                if (tile.getWorldObj() == event.world)
                {
                    if(tile.getWorldObj().loadedTileEntityList.remove(this))
                    {
                        debug("\tremoved " + tile + " from tick system");
                    }
                    it.remove();
                }
            }
        }
    }

    /**
     * Sets the tile to be removed from the world's tick loop
     *
     * @param tile - tile that needs to be removed
     */
    public void addTileToBeRemoved(TileEntity tile)
    {
        if (!removeList.contains(tile) && tile.getWorldObj().loadedTileEntityList.contains(tile))
        {
            debug("Added " + tile);
            removeList.add(tile);
        }
    }

    private void debug(String msg)
    {
        if (Engine.runningAsDev)
        {
           // Engine.logger().info("TileTaskTickHandler: " + msg);
        }
    }
}
