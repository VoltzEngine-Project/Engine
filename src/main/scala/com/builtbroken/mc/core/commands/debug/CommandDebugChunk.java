package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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
        sender.sendMessage(new TextComponentString("This command is not supported from console."));
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

                Chunk chunk = world.getChunkFromChunkCoords((int) Math.round(player.posX) >> 4, (int) Math.round(player.posZ) >> 4);
                int chunkX = chunk.x << 4;
                int chunkZ = chunk.z << 4;
                for (int y = 1; y < 256; y++)
                {
                    for (int x = 0; x < 16; x++)
                    {
                        for (int z = 0; z < 16; z++)
                        {
                            BlockPos pos = new BlockPos(x + chunkX, y, z + chunkZ);
                            IBlockState state = world.getBlockState(pos);
                            Block block = state.getBlock();
                            if (block == Blocks.STONE || block == Blocks.SAND || block == Blocks.SANDSTONE || block == Blocks.DIRT || block == Blocks.GRASS || block == Blocks.GRAVEL)
                            {
                                world.setBlockToAir(pos);
                            }
                            else if (block == Blocks.WATER || block == Blocks.FLOWING_WATER)
                            {
                                world.setBlockState(pos, Blocks.STAINED_GLASS.getStateFromMeta(4), 2);
                            }
                            else if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA)
                            {
                                world.setBlockState(pos, Blocks.STAINED_GLASS.getStateFromMeta(1), 2);
                            }
                        }
                    }
                }
                player.sendMessage(new TextComponentString("Generating world hole! Watch your step :P"));
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
