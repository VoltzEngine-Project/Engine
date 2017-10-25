package com.builtbroken.mc.core.commands.debug;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/27/2016.
 */
public class CommandDebugInventory extends SubCommand
{
    public CommandDebugInventory()
    {
        super("inventory");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        return handle(player, player, args);
    }

    @Override
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)
    {
        return handle(sender, null, args);
    }

    protected boolean handle(ICommandSender sender, EntityPlayer player, String[] args)
    {
        if (args.length > 0)
        {
            String type = args[0];
            if (type.equalsIgnoreCase("size"))
            {
                if (args.length > 1)
                {
                    player = getPlayer(sender, args[1]);
                    if (player != null)
                    {
                        sender.addChatMessage(new ChatComponentText("Could not find player for name '" + player + "'"));
                        return true;
                    }
                }
                if (player == null)
                {
                    sender.addChatMessage(new ChatComponentText("Too few arguments, missing player name"));
                    return true;
                }
                sender.addChatMessage(new ChatComponentText("Total: " + player.inventory.getSizeInventory()));
                sender.addChatMessage(new ChatComponentText("Main: " + player.inventory.mainInventory.length));
                sender.addChatMessage(new ChatComponentText("Armor: " + player.inventory.armorInventory.length));
                return true;
            }
        }
        return false;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        if (sender instanceof EntityPlayer)
        {
            items.add("size - tests player inventory limits to check for mods editing sizes");
        }
        items.add("size <player>");
    }
}
