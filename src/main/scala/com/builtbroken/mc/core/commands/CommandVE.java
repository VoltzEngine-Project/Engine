package com.builtbroken.mc.core.commands;

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

    public CommandVE()
    {
        super("ve");
        if (!disableButcherCommand)
            addCommand(new CommandVEButcher());
        if (!disableRemoveCommand)
            addCommand(new CommandVERemove());
        addCommand(new CommandVEVersion());
        if (!disableClearCommand)
            addCommand(new CommandVEClear());
    }
}
