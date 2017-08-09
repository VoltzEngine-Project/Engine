package com.builtbroken.mc.core;

import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.core.network.packet.PacketSpawnParticle;
import com.builtbroken.mc.core.network.packet.PacketSpawnStream;
import com.builtbroken.mc.core.network.packet.callback.PacketAudio;
import com.builtbroken.mc.imp.transform.vector.Pos;
import com.builtbroken.mc.framework.mod.AbstractProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.awt.*;

/**
 * The Voltz Engine common proxy
 *
 * @author Calclavia
 */
public class CommonProxy extends AbstractProxy
{
    public boolean isPaused()
    {
        return false;
    }

    public EntityPlayer getClientPlayer()
    {
        return null;
    }

    @Override
    public void init()
    {
        EntityRegistry.registerGlobalEntityID(EntityExCreeper.class, "ExCreeper", EntityRegistry.findGlobalUniqueEntityId());
        EntityRegistry.registerModEntity(EntityExCreeper.class, "ExCreeper", 55, Engine.instance, 100, 1, true);
    }

    public int getPlayerDim()
    {
        throw new UnsupportedOperationException("This method can not be called server side");
    }

    /**
     * Called to spawn a particle in the world.
     * <p>
     * Server side this will send a packet to the client
     *
     * @param name  - name of the particle, this needs to be specific or it will not work
     * @param world - world to spawn the particle inside
     * @param x     - location
     * @param y     - location
     * @param z     - location
     * @param xx    - velocity, in some cases this is used as extra data
     * @param yy    - velocity, in some cases this is used as extra data
     * @param zz    - velocity, in some cases this is used as extra data
     */
    public void spawnParticle(String name, World world, double x, double y, double z, double xx, double yy, double zz)
    {
        if (!world.isRemote)
        {
            Engine.instance.packetHandler.sendToAllAround(new PacketSpawnParticle(name, world.provider.dimensionId, x, y, z, xx, yy, zz), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64));
        }
    }

    /**
     * Called to spawn an FX beam into the world with the desired properties
     * <p>
     * Server side this will send a packet to the client
     *
     * @param location    - texture to use for the renderer
     * @param world       - world to spawn the effect inside
     * @param start       - start of the laser
     * @param end         - end of the laser
     * @param color       - color of the laser
     * @param ticksToLive - how long to spawn the effect
     */
    public void spawnBeamFx(ResourceLocation location, World world, Pos start, Pos end, Color color, int ticksToLive)
    {
        if (!world.isRemote)
        {
            //TODO do a proper distance check as the 90 default is not going to work for very long range laser renders
            Engine.instance.packetHandler.sendToAllAround(new PacketSpawnStream(world.provider.dimensionId, start, end, 0), new NetworkRegistry.TargetPoint(world.provider.dimensionId, start.x(), start.y(), start.z(), 90));
        }
    }

    public void playJsonAudio(World world, String audioKey, double x, double y, double z, float pitch, float volume)
    {
        if (world != null && !world.isRemote)
        {
            PacketAudio packetAudio = new PacketAudio(world, audioKey, x, y, z, pitch, volume);
            Engine.instance.packetHandler.sendToAllAround(packetAudio, world, x, y, z, 100);
        }
    }

    public void playJsonEffect(World world, String key, double x, double y, double z, double mx, double my, double mz, boolean endPoint, NBTTagCompound nbt)
    {
        if (world != null && !world.isRemote)
        {
            PacketSpawnParticle packet = new PacketSpawnParticle("JSON_" + key, world.provider.dimensionId, x, y, z, mx, my, mz);
            packet.otherData = nbt;
            packet.endPoint = endPoint;
            Engine.instance.packetHandler.sendToAllAround(packet, world, x, y, z, 100);
        }
    }
}
