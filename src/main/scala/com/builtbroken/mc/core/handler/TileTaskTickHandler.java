package com.builtbroken.mc.core.handler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles tasks that need to be done outside of the main world tick loop
 * Created by Dark on 8/20/2015.
 */
public class TileTaskTickHandler
{
    public static final TileTaskTickHandler INSTANCE = new TileTaskTickHandler();

    private List<TileEntity> removeList = new ArrayList();

    private TileTaskTickHandler()
    {

    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.isRemote)
            FMLCommonHandler.instance().bus().unregister(this);

        if (event.phase == TickEvent.Phase.END && removeList.size() > 0)
        {
            for (TileEntity tile : removeList)
            {
                if (tile.getWorldObj() == event.world)
                {
                    tile.getWorldObj().loadedTileEntityList.remove(this);
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
        if (!removeList.contains(tile))
            removeList.add(tile);
    }
}
