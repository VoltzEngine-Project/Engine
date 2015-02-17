package com.builtbroken.mc.core.commands;

import com.builtbroken.mc.core.commands.modflags.CommandNewRegion;
import com.builtbroken.mc.core.commands.permissions.CommandNewGroup;
import com.builtbroken.mc.prefab.commands.ModularCommand;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandNew extends ModularCommand
{
    public CommandNew()
    {
        super("new");
        addCommand(new CommandNewRegion());
        addCommand(new CommandNewGroup());
    }
}
