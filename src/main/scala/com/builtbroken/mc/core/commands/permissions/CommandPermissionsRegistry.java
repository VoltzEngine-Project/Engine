package com.builtbroken.mc.core.commands.permissions;

import com.builtbroken.mc.lib.access.Permission;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandPermissionsRegistry
{
    public static final Permission ALL = new Permission("root");


    public static void init(ICommandManager commandManager, ServerCommandManager serverCommandManager)
    {
        //TODO loop threw all registered commands and create permissions nodes for them
    }
}
