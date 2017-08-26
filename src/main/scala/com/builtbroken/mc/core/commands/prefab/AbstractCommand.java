package com.builtbroken.mc.core.commands.prefab;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefab for creating Minecraft based commands on the existing system. Contains helper methods
 * and checks to improve the speed of making new commands.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * <p/>
 * <p/>
 * Created by Dark(DarkGuardsman, Robert) on 2/10/2015.
 */
public class AbstractCommand extends CommandBase
{
    /** Name of the command */
    protected final String name;

    /**
     * Creats a new command instance, should only be one instance per command
     *
     * @param name - name of the command, used by the user to call the command
     */
    public AbstractCommand(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getUsage(ICommandSender p_71518_1_)
    {
        return "/" + getName() + " help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!handleHelp(sender, args))
        {
            if (!(sender instanceof EntityPlayer && handleEntityPlayerCommand((EntityPlayer) sender, args)) && !handleConsoleCommand(sender, args))
            {
                sender.sendMessage(new TextComponentString("Error: Unknown chat command"));
            }
        }
    }

    /**
     * Checks to see if the command is a help command. Override if you want to change when help is displayed.
     *
     * @param args - arguments for the command
     * @return true if is a help command
     */
    public boolean isHelpCommand(String[] args)
    {
        return args == null || args.length == 0 || args[0] == null || args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?");
    }

    /**
     * Called to handle help command
     *
     * @param sender - user running the command
     * @param args   - arguments for the command
     * @return true if the help command was handled or false if it wasn't a help command
     */
    public boolean handleHelp(ICommandSender sender, String[] args)
    {
        if (isHelpCommand(args))
        {
            int p = 0;
            if (args != null && args.length >= 2)
            {
                try
                {
                    p = Integer.parseInt(args[1]);
                } catch (NumberFormatException e)
                {

                }
            }
            printHelp(sender, p);
            return true;
        }
        return false;
    }

    /**
     * Called to handle a command run by a player in game
     *
     * @param player - user in game who ran the chat command
     * @param args   - args for the command
     * @return true if the command was consumed or was processed in some way
     */
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)  throws CommandException
    {
        return false;
    }

    /**
     * Called to handle a command run by something that is not a player
     *
     * @param sender - user running the command
     * @param args   - args for the command
     * @return true if the command was consumed or was processed in some way
     */
    public boolean handleConsoleCommand(ICommandSender sender, String[] args)  throws CommandException
    {
        return false;
    }

    /**
     * Added the help output to the player's chat
     *
     * @param sender - user running the command
     * @param p      - page number to output
     */
    protected void printHelp(ICommandSender sender, int p)
    {
        List<String> items = new ArrayList();
        getHelpOutput(sender, items);

        sender.sendMessage(new TextComponentString("====== help -" + getPrefix().replace("/", "") + "- page " + p + " ======"));
        for (int i = 0 + (p * 10); i < 10 + (p * 10) && i < items.size(); i++)
        {
            sender.sendMessage(new TextComponentString(getPrefix() + " " + items.get(i)));
        }
        sender.sendMessage(new TextComponentString(""));
    }

    /**
     * Gets the prefix for this command
     *
     * @return prefix for the command
     */
    public String getPrefix()
    {
        return "/" + getName();
    }

    /**
     * Called to add entries to the help output list. Only return the name
     * of the command and not it's parent prefix. As that will be applied
     * for you automatically to save time.
     *
     * @param sender - user running the command
     * @param items  - list to add entries too
     */
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("help");
    }

    /**
     * Gets all users only by username
     *
     * @return array of strings
     */
    protected final String[] playersOnlineByUsername()
    {
        return FMLServerHandler.instance().getServer().getPlayerList().getOnlinePlayerNames();
    }

    /**
     * Lists all players currently online using the player object
     *
     * @return list of players
     */
    protected final List<EntityPlayerMP> getPlayersOnline()
    {
        return FMLServerHandler.instance().getServer().getPlayerList().getPlayers();
    }

    /**
     * Adds a chat message to all players in the list
     *
     * @param players - list of players, shouldn't be null, can be empty, not checked
     * @param msg     - msg to display the the user, shouldn't be null or empty, not checked
     */
    protected void addChatToPlayers(List<EntityPlayerMP> players, String msg)
    {
        TextComponentString chatComponentText = new TextComponentString(msg);
        for (EntityPlayer player : players)
        {
            player.sendMessage(chatComponentText);
        }
    }

    /**
     * Adds a chat message to all players currently logged in
     *
     * @param msg - msg to display the the user, shouldn't be null or empty, not checked
     */
    protected void addChatToAllPlayers(String msg)
    {
        addChatToPlayers(getPlayersOnline(), msg);
    }

    /**
     * Turns the args array back into a string for commands
     * that have mutli-word values
     *
     * @param args - array of strings, can't be null, values can't be null, not checked
     * @return string combining the array
     */
    public final String combine(String[] args)
    {
        return combine(args, 0, args.length);
    }

    /**
     * Turns the args array back into a string for commands
     * that have mutli-word values
     *
     * @param args - array of strings, can't be null, values can't be null, not checked
     * @return string combining the array
     */
    public final String combine(String[] args, int start, int end)
    {
        String s = args[start];
        for (int i = start + 1; i < args.length && i < end; i++)
        {
            s += " " + args[i];
        }
        return s.trim();
    }

    /**
     * Removes the first entry from the array
     *
     * @param array - array to work on
     * @return new array missing the first entry
     */
    protected String[] removeFront(String[] array)
    {
        return removeFront(array, 1);
    }

    /**
     * Removes so many entries fromt he front of the array
     *
     * @param array - array to work on
     * @param count - number of entries to remove
     * @return new array
     */
    protected String[] removeFront(String[] array, int count)
    {
        if (count <= 0)
            count = 1;

        if (array.length > count)
        {
            String[] a = new String[array.length - count];
            for (int i = 0; i < a.length; i++)
            {
                a[i] = array[i + count];
            }
            return a;
        }
        return new String[0];
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
}
