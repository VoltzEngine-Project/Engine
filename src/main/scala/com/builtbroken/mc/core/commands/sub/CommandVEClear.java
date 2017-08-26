package com.builtbroken.mc.core.commands.sub;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVEClear extends SubCommand
{
    public CommandVEClear()
    {
        super("clear");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer entityPlayer, String[] args) throws CommandException
    {
        if (!handleHelp(entityPlayer, args))
        {
            EntityPlayer player;
            if (args.length > 1)
            {
                player = getPlayer(FMLServerHandler.instance().getServer(), entityPlayer, args[1]);
            }
            else
            {
                player = entityPlayer;
            }
            if (player != null)
            {
                if (args[0].equalsIgnoreCase("armor"))
                {
                    for (int slot = 0; slot < player.inventory.armorInventory.size(); slot++)
                    {
                        player.inventory.armorInventory.set(slot, ItemStack.EMPTY);
                    }
                    player.inventoryContainer.detectAndSendChanges();
                    if (player != entityPlayer)
                    {
                        player.sendMessage(new TextComponentString("Your armor has been removed by an admin"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponentString("Your armor has been removed"));
                    }
                    return true;
                }
                else if (args[0].equalsIgnoreCase("inv"))
                {
                    for (int slot = 0; slot < player.inventory.mainInventory.size(); slot++)
                    {
                        player.inventory.mainInventory.set(slot, ItemStack.EMPTY);
                    }
                    player.inventoryContainer.detectAndSendChanges();
                    if (player != entityPlayer)
                    {
                        player.sendMessage(new TextComponentString("Your inventory has been cleared by an admin"));
                    }
                    else
                    {
                        player.sendMessage(new TextComponentString("Your inventory has been cleared"));
                    }
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return args != null && args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"));
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("armor");
        items.add("inv");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args != null && args.length > 0 && args[0] != null)
        {
            if (args[0].equalsIgnoreCase("inv") || args[0].equalsIgnoreCase("armor"))
            {
                return args.length == 2 ? getListOfStringsMatchingLastWord(args, this.playersOnlineByUsername()) : null;
            }
        }
        return null;
    }
}
