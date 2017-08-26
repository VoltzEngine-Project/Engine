package com.builtbroken.mc.core.commands.sub;

import com.builtbroken.jlib.lang.StringHelpers;
import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.prefab.entity.selector.EntitySelector;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;

import java.util.Collection;
import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVERemove extends SubCommand
{
    public CommandVERemove()
    {
        super("remove");
    }

    private EntitySelector getSelector(String name)
    {
        //Find out what entity selector to use
        if (name.equalsIgnoreCase("projectiles"))
        {
            return EntitySelectors.PROJECTILE_SELECTOR.selector();
        }
        else if (name.equalsIgnoreCase("mobs"))
        {
            return EntitySelectors.MOB_SELECTOR.selector();
        }
        else if (name.equalsIgnoreCase("living"))
        {
            return EntitySelectors.LIVING_SELECTOR.selector();
        }
        else if (name.equalsIgnoreCase("items"))
        {
            return EntitySelectors.ITEM_SELECTOR.selector();
        }
        else if (name.equalsIgnoreCase("xp"))
        {
            return EntitySelectors.XP_SELECTOR.selector();
        }
        return null;
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer entityPlayer, String[] args)
    {
        if (args.length > 0)
        {
            int radius = 100;
            EntitySelector selector = getSelector(args[0]);
            if (selector == null)
            {
                return false; //TODO return error
            }

            //Get radius from player
            if (args.length > 1 && args[1] != null)
            {
                try
                {
                    radius = Integer.parseInt(args[1]);
                    if (radius > 1000)
                    {
                        entityPlayer.sendMessage(new TextComponentString("To prevent lag/crashes radius is limited to 1000"));
                        return true;
                    }
                    else if (radius < 1)
                    {
                        entityPlayer.sendMessage(new TextComponentString("Radius needs to be positive"));
                        return true;
                    }
                }
                catch (NumberFormatException e)
                {
                    entityPlayer.sendMessage(new TextComponentString("Radius needs to be an integer"));
                    return true;
                }
            }

            if (selector != null)
            {
                List<Entity> list = selector.getEntities(entityPlayer, radius);
                for (Entity entity : list)
                {
                    entity.setDead();
                }
                entityPlayer.sendMessage(new TextComponentString("Removed " + list.size() + " " + args[0] + " entities within " + radius + " block radius"));
            }
            else
            {
                entityPlayer.sendMessage(new TextComponentString("Error unknown selector"));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        long time = System.nanoTime();
        //TODO add ability to set location and range
        int dim = 0;

        EntitySelector selector = getSelector(args[0]);
        if (selector == null)
        {
            return false; //TODO return error
        }

        if (args != null && args.length > 1)
        {
            if (args[1].startsWith("dim"))
            {
                try
                {
                    dim = Integer.parseInt(args[1].replace("dim", ""));
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
            for (Object object : provider.getLoadedChunks())
            {
                if (object instanceof Chunk)
                {
                    chunksSearched++;
                    for (Object l : ((Chunk) object).getEntityLists())
                    {
                        if (l instanceof Collection)
                        {
                            for (Object e : (Collection) l)
                            {
                                if (e instanceof Entity && ((Entity) e).isEntityAlive() && selector.isEntityApplicable((Entity) e))
                                {
                                    ((Entity) e).setDead();
                                    entitiesKilled++;
                                }
                            }
                        }
                    }
                }
            }
            time = System.nanoTime() - time;
            sender.sendMessage(new TextComponentString("Removed " + entitiesKilled + " entities over " + chunksSearched + " chunks in " + StringHelpers.formatNanoTime(time)));
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
        return args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"));
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("projectiles <radius>");
        items.add("mobs <radius>");
        items.add("living <radius>");
        items.add("items <radius>");
        items.add("xp <radius>");
    }
}
