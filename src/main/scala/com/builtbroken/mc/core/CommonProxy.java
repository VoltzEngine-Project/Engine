package com.builtbroken.mc.core;

import com.builtbroken.mc.core.content.entity.EntityExCreeper;
import com.builtbroken.mc.core.network.packet.PacketSpawnParticle;
import com.builtbroken.mc.lib.mod.AbstractProxy;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

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

    public void spawnParticle(String name, World world, double x, double y, double z, double xx, double yy, double zz)
    {
        if (!world.isRemote)
        {
            Engine.instance.packetHandler.sendToAllAround(new PacketSpawnParticle(name, world.provider.dimensionId, x, y, z, xx, yy, zz), new NetworkRegistry.TargetPoint(world.provider.dimensionId, x, y, z, 64));
        }
    }
}
