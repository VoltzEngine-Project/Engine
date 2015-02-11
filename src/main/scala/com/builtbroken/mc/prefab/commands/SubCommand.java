package com.builtbroken.mc.prefab.commands;

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
}
