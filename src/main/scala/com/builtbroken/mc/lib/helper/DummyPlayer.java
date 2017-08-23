package com.builtbroken.mc.lib.helper;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Fake player used by machines, also includes utilities for using the fake player better.
 *
 * @author DarkGuardsman
 */
public class DummyPlayer extends FakePlayer
{
    private static final LinkedHashMap<World, DummyPlayer> FAKE_PLAYERS = new LinkedHashMap<>();

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
        this(world, "(" + world.provider.getDimension() + ")");
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
}
