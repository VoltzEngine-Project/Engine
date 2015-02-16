package com.builtbroken.mc.lib.modflags.events;

import com.builtbroken.mc.lib.modflags.Region;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by robert on 2/16/2015.
 */
public class PlayerRegionEvent extends PlayerEvent
{
    public final Region region;

    public PlayerRegionEvent(EntityPlayer player, Region region)
    {
        super(player);
        this.region = region;
    }

    public static class PlayerExitRegionEvent extends PlayerRegionEvent
    {
        public PlayerExitRegionEvent(EntityPlayer player, Region region)
        {
            super(player, region);
        }
    }

    public static class PlayerEnterRegionEvent extends PlayerRegionEvent
    {
        public PlayerEnterRegionEvent(EntityPlayer player, Region region)
        {
            super(player, region);
        }
    }

    public static class PlayerTeleportIntoRegionEvent extends PlayerRegionEvent
    {
        public PlayerTeleportIntoRegionEvent(EntityPlayer player, Region region)
        {
            super(player, region);
        }
    }

    public static class PlayerTeleportOutOfRegionEvent extends PlayerRegionEvent
    {
        public PlayerTeleportOutOfRegionEvent(EntityPlayer player, Region region)
        {
            super(player, region);
        }
    }
}
