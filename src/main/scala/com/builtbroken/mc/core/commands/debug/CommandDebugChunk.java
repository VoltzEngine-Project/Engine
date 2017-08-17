package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugChunk extends SubCommand
{
    public CommandDebugChunk()
    {
        super("chunk");
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        sender.addChatMessage(new ChatComponentText("This command is not supported from console."));
        return true;
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        //TODO add UNDO ability
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args[0].equalsIgnoreCase("ores"))
            {
                World world = player.getEntityWorld();

                Chunk chunk = world.getChunkFromBlockCoords((int) Math.round(player.posX), (int) Math.round(player.posZ));
                int chunkX = chunk.xPosition << 4;
                int chunkZ = chunk.zPosition << 4;
                for (int y = 1; y < 256; y++)
                {
                    for (int x = 0; x < 16; x++)
                    {
                        for (int z = 0; z < 16; z++)
                        {
                            Block block = world.getBlock(x + chunkX, y, z + chunkZ);
                            if (block == Blocks.stone || block == Blocks.sand || block == Blocks.sandstone || block == Blocks.dirt || block == Blocks.grass || block == Blocks.gravel)
                            {
                                world.setBlockToAir(x + chunkX, y, z + chunkZ);
                            }
                            else if (block == Blocks.water || block == Blocks.flowing_water)
                            {
                                world.setBlock(x + chunkX, y, z + chunkZ, Blocks.stained_glass, 4, 2);
                            }
                            else if (block == Blocks.lava || block == Blocks.flowing_lava)
                            {
                                world.setBlock(x + chunkX, y, z + chunkZ, Blocks.stained_glass, 1, 2);
                            }
                        }
                    }
                }
                player.addChatMessage(new ChatComponentText("Generating world hole! Watch your step :P"));
                return true;
            }
        }
        else
        {
            return handleHelp(player, args);
        }
        return false;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("ores - removes terrain to debug ore generation");
    }
}
