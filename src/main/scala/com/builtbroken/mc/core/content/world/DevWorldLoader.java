package com.builtbroken.mc.core.content.world;

import com.builtbroken.mc.core.content.world.chunks.ChunkProviderChess;
import com.builtbroken.mc.core.content.world.chunks.ChunkProviderEmpty;
import com.builtbroken.mc.core.content.world.chunks.ChunkProviderStone;
import com.builtbroken.mc.framework.mod.loadable.AbstractLoadable;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/29/2016.
 */
public final class DevWorldLoader extends AbstractLoadable
{
    public static WorldType emptyWorldGenerator;
    public static WorldType stoneWorldGenerator;

    private boolean hasWrapperedGenerators = false;

    @Override
    public void preInit()
    {
        emptyWorldGenerator = new WorldType("voidTestWorld")
        {
            @Override
            public IChunkProvider getChunkGenerator(World world, String generatorOptions)
            {
                return new ChunkProviderEmpty(world);
            }

            @Override
            public boolean getCanBeCreated()
            {
                return true;
            }
        };
        stoneWorldGenerator = new WorldType("stoneTestWorld")
        {
            @Override
            public IChunkProvider getChunkGenerator(World world, String generatorOptions)
            {
                return new ChunkProviderStone(world);
            }

            @Override
            public boolean getCanBeCreated()
            {
                return true;
            }
        };
        stoneWorldGenerator = new WorldType("chessTestWorld")
        {
            @Override
            public IChunkProvider getChunkGenerator(World world, String generatorOptions)
            {
                return new ChunkProviderChess(world);
            }

            @Override
            public boolean getCanBeCreated()
            {
                return true;
            }
        };
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (event.world.provider.dimensionId == 0)
        {
            wrapperWorldGenerators();
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

    protected void wrapperWorldGenerators()
    {
        if (!hasWrapperedGenerators)
        {
            hasWrapperedGenerators = true;
            try
            {
                Field worldGeneratorsField = GameRegistry.class.getDeclaredField("worldGenerators");
                worldGeneratorsField.setAccessible(true);

                Field worldGeneratorIndexField = GameRegistry.class.getDeclaredField("worldGeneratorIndex");
                worldGeneratorIndexField.setAccessible(true);

                Set<IWorldGenerator> oldSet = ((Set<IWorldGenerator>) worldGeneratorsField.get(null));
                Map<IWorldGenerator, Integer> oldSetIndex = ((Map<IWorldGenerator, Integer>) worldGeneratorIndexField.get(null));

                HashMap<IWorldGenerator, Integer> newGenerators = new HashMap();
                Iterator<IWorldGenerator> it = oldSet.iterator();
                while (it.hasNext())
                {
                    IWorldGenerator gen = it.next();
                    newGenerators.put(new WorldGenWrapper(gen), oldSetIndex.get(gen));
                    oldSetIndex.remove(gen);
                    it.remove();
                }

                for (Map.Entry<IWorldGenerator, Integer> entry : newGenerators.entrySet())
                {
                    GameRegistry.registerWorldGenerator(entry.getKey(), entry.getValue());
                }

            }
            catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }
}
