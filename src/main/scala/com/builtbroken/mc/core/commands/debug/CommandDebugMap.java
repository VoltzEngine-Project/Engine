package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.lib.world.map.TileMapRegistry;
import com.builtbroken.mc.lib.world.radar.RadarMap;
import com.builtbroken.mc.lib.world.radar.data.RadarObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Used to test for recipe conflicts and dump items without recipes
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugMap extends SubCommand
{
    public CommandDebugMap()
    {
        super("map");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args[0].equalsIgnoreCase("radar"))
            {

            }
            else if (args[0].equalsIgnoreCase("tile") && args.length > 1)
            {
                if (args[1].equalsIgnoreCase("enableDebug"))
                {
                    if (args.length > 2)
                    {
                        RadarMap map = TileMapRegistry.getRadarMapForWorld(player.worldObj);
                        if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("t"))
                        {
                            map.setDebugEnabled(true);
                            player.addChatComponentMessage(new ChatComponentText("Debug enabled for tile map in your world."));
                        }
                        else if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("f"))
                        {
                            map.setDebugEnabled(false);
                            player.addChatComponentMessage(new ChatComponentText("Debug disabled for tile map in your world."));
                        }
                        else
                        {
                            player.addChatComponentMessage(new ChatComponentText("Could not parse [" + args[2] + "], enable status can only be true or false"));
                        }
                        return true;
                    }
                    else
                    {
                        RadarMap map = TileMapRegistry.getRadarMapForWorld(player.worldObj);
                        if (!map.debugRadarMap)
                        {
                            map.setDebugEnabled(true);
                            player.addChatComponentMessage(new ChatComponentText("Debug enabled for tile map in your world."));
                        }
                        else
                        {
                            map.setDebugEnabled(false);
                            player.addChatComponentMessage(new ChatComponentText("Debug disabled for tile map in your world."));
                        }
                        return true;
                    }
                }
                else if (args[1].equalsIgnoreCase("objects"))
                {
                    RadarMap map = TileMapRegistry.getRadarMapForWorld(player.worldObj);
                    if (map.allEntities.size() > 0)
                    {
                        player.addChatComponentMessage(new ChatComponentText("There are " + map.allEntities.size() + " tiles in the map."));
                    }
                    else
                    {
                        player.addChatComponentMessage(new ChatComponentText("No tiles detected in global tile list."));
                    }
                    return true;
                }
                else if (args[1].equalsIgnoreCase("chunks"))
                {
                    RadarMap map = TileMapRegistry.getRadarMapForWorld(player.worldObj);
                    if (map.chunk_to_entities.size() > 0)
                    {
                        player.addChatComponentMessage(new ChatComponentText("There are " + map.chunk_to_entities.size() + " chunk locations in the map."));
                    }
                    else
                    {
                        player.addChatComponentMessage(new ChatComponentText("No chunks detected in map."));
                    }
                    return true;
                }
                else if (args[1].equalsIgnoreCase("around"))
                {
                    int range = 100;
                    if (args.length > 2)
                    {
                        try
                        {
                            range = Integer.parseInt(args[2]);
                        }
                        catch (NumberFormatException e)
                        {
                            player.addChatComponentMessage(new ChatComponentText("Invalid range number"));
                            return true;
                        }
                    }
                    RadarMap map = TileMapRegistry.getRadarMapForWorld(player.worldObj);
                    List<RadarObject> list = map.getRadarObjects(player.posX, player.posZ, range);
                    if (list.size() > 0)
                    {
                        player.addChatComponentMessage(new ChatComponentText("There are " + list.size() + " tiles within " + range + " meters"));
                    }
                    else
                    {
                        player.addChatComponentMessage(new ChatComponentText("No tiles detected in within " + range + " meters"));
                    }
                    return true;
                }
                return handleHelp(player, args);
            }
            else if (args[0].equalsIgnoreCase("heat"))
            {

            }
        }
        return handleHelp(player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        return false; //TODO implement console version
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("tile enableDebug <true/false> - toggles console debug");
        items.add("tile around [range] - number of tiles nearby");
        items.add("tile chunks - outputs number of chunks mapped");
        items.add("tile objects - outputs number of tiles mapped");
    }
}
