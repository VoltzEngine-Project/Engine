package com.builtbroken.mc.core.commands.sub;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;

import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVEButcher extends SubCommand
{
    public CommandVEButcher()
    {
        super("butcher");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer entityPlayer, String[] args)
    {
        List<Entity> list = EntitySelectors.MOB_SELECTOR.selector().getEntities(entityPlayer, 100);
        for (Entity entity : list)
        {
            entity.setDead();
        }
        entityPlayer.sendMessage(new TextComponentString("Removed " + list.size() + " mobs entities within " + 100 + " block radius."));
        return true;
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        long time = System.nanoTime();
        //TODO add ability to set location and range
        int dim = 0;
        if (args != null && args.length > 0)
        {
            if (args[0].startsWith("dim"))
            {
                try
                {
                    dim = Integer.parseInt(args[0].replace("dim", ""));
                }
                catch (NumberFormatException e)
                {
                    sender.sendMessage(new TextComponentString("Dim id needs to be an int"));
                    return true;
                }
            }
            else
            {
                sender.sendMessage(new TextComponentString("Right now only /ve butcher dim[#] is supported, ex /ve butcher dim0"));
                return true;
            }
        }

        WorldServer world = DimensionManager.getWorld(dim);
        if (world != null)
        {

            int entitiesKilled = 0;
            int chunksSearched = 0;
            ChunkProviderServer provider = world.getChunkProvider();
            for (Chunk chunk : provider.getLoadedChunks())
            {
                chunksSearched++;
                for (ClassInheritanceMultiMap<Entity> l : chunk.getEntityLists())
                {
                    for (Entity e : l)
                    {
                        if (e instanceof IMob)
                        {
                            if (e.isEntityAlive())
                            {
                                e.setDead();
                                entitiesKilled++;
                            }
                        }
                    }
                }
            }
            time = System.nanoTime() - time;
            sender.sendMessage(new TextComponentString("Removed " + entitiesKilled + "mobs over " + chunksSearched + " chunks in " + StringHelpers.formatNanoTime(time)));
        }
        else
        {
            sender.sendMessage(new TextComponentString("World doesn't exist, this means it unloaded or the wrong id was provided."));
        }
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return false;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("");
    }
}
