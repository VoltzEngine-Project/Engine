package com.builtbroken.mc.core.handler;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.network.packet.PacketSelectionData;
import com.builtbroken.mc.lib.transform.region.Cuboid;
import com.builtbroken.mc.lib.transform.vector.Pos;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Keeps track of the world cuboid selections for each player. As well which selections
 * should be rendered on the player's client.
 * <p/>
 * Created by robert on 2/15/2015.
 */
public class SelectionHandler
{
    public static final SelectionHandler INSTANCE = new SelectionHandler();

    private static final HashMap<EntityPlayer, Cuboid> selections = Maps.newHashMap();

    private SelectionHandler()
    {
    }

    /**
     * gets the selection of the player, or creates one if none exists.
     * This is mainly so we dont have to deal with NPEs later.
     */
    public static Cuboid getSelection(EntityPlayer player)
    {
        Cuboid out = INSTANCE.selections.get(player);

        if (out == null)
        {
            out = new Cuboid(null, null);
            INSTANCE.selections.put(player, out);
        }

        return out;
    }

    /**
     * gets the selection of the player, or creates one if none exists.
     * This is mainly so we dont have to deal with NPEs later.
     */
    public static void setSelection(EntityPlayer player, Cuboid cuboid)
    {
        INSTANCE.selections.put(player, cuboid);
        if (!player.worldObj.isRemote)
        {
            if (player instanceof EntityPlayerMP)
                updatePlayerRenderData((EntityPlayerMP) player);
        }
    }

    /**
     * Resets the selection of the player to a cube with null components
     */
    private void clearSelection(EntityPlayer player)
    {
        Cuboid select = selections.get(player);
        if (select != null)
        {
            select.min_$eq(null);
            select.max_$eq(null);
        }
    }

    public static void updatePlayerRenderData(EntityPlayerMP player)
    {
        List<Cuboid> cubes = new ArrayList();
        cubes.add(getSelection(player));
        for (Cuboid cube : selections.values())
        {
            if (cube.distance(new Pos(player)) <= 64)
            {
                cubes.add(cube);
            }
        }
        Engine.instance.packetHandler.sendToPlayer(new PacketSelectionData(cubes), player);
    }

    // ===========================================
    // player tracker things here.
    // tHandlers clearing of the selections
    // ===========================================


    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        clearSelection(event.player);
        if (event.player instanceof EntityPlayerMP)
            updatePlayerRenderData((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        clearSelection(event.player);
        if (event.player instanceof EntityPlayerMP)
            updatePlayerRenderData((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!event.world.isRemote && event.phase == TickEvent.Phase.END)
        {
            if (event.world.getWorldInfo().getWorldTime() % 5 == 0)
            {
                //Sort threw all players in world and update render data
                for (Object obj : event.world.playerEntities)
                {
                    if (obj instanceof EntityPlayerMP)
                    {
                        updatePlayerRenderData((EntityPlayerMP) obj);
                    }
                }
            }
        }
    }
}
