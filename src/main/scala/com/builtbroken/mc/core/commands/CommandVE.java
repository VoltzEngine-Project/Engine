package com.builtbroken.mc.core.commands;

import com.builtbroken.mc.core.commands.modflags.CommandNewRegion;
import com.builtbroken.mc.core.commands.modflags.CommandRemoveRegion;
import com.builtbroken.mc.prefab.commands.AbstractCommand;
import com.builtbroken.mc.prefab.commands.ModularCommand;

/**
 * Created by robert on 1/23/2015.
 */
public class CommandVE extends ModularCommand
{
    public static boolean disableCommands = false;
    public static boolean disableRemoveCommand = false;
    public static boolean disableButcherCommand = false;
    public static boolean disableClearCommand = false;
    public static boolean disableRegionCommand = false;
    public static boolean disableModflagCommands = false;

    public static final CommandVE INSTANCE = new CommandVE();
    private ModularCommand sub_command_new;
    private ModularCommand sub_command_remove;

    private CommandVE()
    {
        super("ve");
        if (!disableButcherCommand)
            addCommand(new CommandVEButcher());
        if (!disableRemoveCommand)
            addCommand(new CommandVERemove());
        addCommand(new CommandVEVersion());
        if (!disableClearCommand)
            addCommand(new CommandVEClear());
        if(!disableModflagCommands)
        {
            addToNewCommand(new CommandNewRegion());
            addToRemoveCommand(new CommandRemoveRegion());
        }
    }

    public void addToNewCommand(AbstractCommand command)
    {
        if(sub_command_new == null)
        {
            sub_command_new = new ModularCommand("new");
            INSTANCE.addCommand(sub_command_new);
        }
        sub_command_new.addCommand(command);
    }

    public void addToRemoveCommand(AbstractCommand command)
    {
        if(sub_command_remove == null)
        {
            sub_command_remove = new ModularCommand("remove");
            INSTANCE.addCommand(sub_command_remove);
        }
        sub_command_remove.addCommand(command);
    }
}
