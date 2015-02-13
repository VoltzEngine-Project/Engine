package com.builtbroken.mc.prefab.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

/**
 * Designed to be used from inside ModularCommand
 * Created by robert on 2/10/2015.
 */
public class SubCommand extends AbstractCommand
{
    protected ModularCommand super_command;

    public SubCommand(String name)
    {
        super(name);
    }

    public void setSuperCommand(ModularCommand c)
    {
        this.super_command = c;
    }

    @Override
    public String getPrefix()
    {
        return "/" + (super_command != null ? super_command.getCommandName() : "") + " " + getCommandName();
    }
}
