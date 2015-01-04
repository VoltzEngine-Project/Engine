package com.builtbroken.lib.utility;

import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import com.builtbroken.lib.transform.vector.VectorWorld;

import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Fake player used by machines, also includes utilities for using the fake player better.
 *
 * @author DarkGuardsman
 */
public class DummyPlayer extends FakePlayer
{
    private static final LinkedHashMap<World, DummyPlayer> FAKE_PLAYERS = new LinkedHashMap<World, DummyPlayer>();

    public DummyPlayer(World world, String name, String sufix)
    {
        super((WorldServer) world, new GameProfile(UUID.randomUUID(), name + sufix));
    }

    public DummyPlayer(World world, String sufix)
    {
        this(world, "[RE]FakePlayer", sufix);
    }

    public DummyPlayer(World world)
    {
        this(world, "(" + world.provider.dimensionId + ")");
    }

    /**
     * Gets the fake player for the world
     */
    public static DummyPlayer get(World world)
    {
        if (!FAKE_PLAYERS.containsKey(world) || FAKE_PLAYERS.get(world) == null)
        {
            FAKE_PLAYERS.put(world, new DummyPlayer(world));
        }

        return FAKE_PLAYERS.get(world);
    }

    public static boolean useItemAt(ItemStack itemStack, VectorWorld location, ForgeDirection direction)
    {
        return useItemAt(itemStack, location.world(), location.xi(), location.yi(), location.zi(), direction.ordinal(), 0, 0, 0);
    }

    public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side)
    {
        return useItemAt(itemStack, world, x, y, z, side, 0, 0, 0);
    }

    public static boolean useItemAt(ItemStack itemStack, World world, int x, int y, int z, int side, int hitX, int hitY, int hitZ)
    {
        return itemStack.getItem().onItemUse(itemStack, get(world), world, x, y, z, side, hitX, hitY, hitZ);
    }

}
