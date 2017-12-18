package com.builtbroken.mc.core.commands;

import com.builtbroken.mc.core.commands.debug.*;
import com.builtbroken.mc.core.commands.energy.CommandChargeItem;
import com.builtbroken.mc.core.commands.ext.GroupSubCommand;
import com.builtbroken.mc.core.commands.ext.ModularCommandRemoveAdd;
import com.builtbroken.mc.core.commands.ext.SubCommandWithName;
import com.builtbroken.mc.core.commands.ext.UserSubCommand;
import com.builtbroken.mc.core.commands.json.CommandJsonRecipe;
import com.builtbroken.mc.core.commands.json.override.CommandJOGen;
import com.builtbroken.mc.core.commands.json.override.CommandJOGet;
import com.builtbroken.mc.core.commands.json.override.CommandJOSet;
import com.builtbroken.mc.core.commands.permissions.sub.CommandGroup;
import com.builtbroken.mc.core.commands.permissions.sub.CommandUser;
import com.builtbroken.mc.core.commands.prefab.AbstractCommand;
import com.builtbroken.mc.core.commands.prefab.ModularCommand;
import com.builtbroken.mc.core.commands.sub.CommandVEButcher;
import com.builtbroken.mc.core.commands.sub.CommandVEClear;
import com.builtbroken.mc.core.commands.sub.CommandVERemove;
import com.builtbroken.mc.core.commands.sub.CommandVEVersion;
import com.builtbroken.mc.core.commands.thread.CommandThreadClear;

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
    private ModularCommand sub_command_dump;
    private ModularCommand sub_command_add;
    private ModularCommand sub_command_add_user;
    private ModularCommand sub_command_remove_user;
    private ModularCommand sub_command_add_perm;
    private ModularCommand sub_command_remove_perm;
    private ModularCommand sub_command_group;
    private ModularCommand sub_command_user;
    private ModularCommand sub_command_debug;
    private ModularCommand sub_command_json;
    private ModularCommand sub_command_thread;
    private ModularCommand sub_command_power;

    private CommandVE()
    {
        super("ve");
        if (!disableButcherCommand)
        {
            addCommand(new CommandVEButcher());
        }
        if (!disableRemoveCommand)
        {
            addCommand(new CommandVERemove());
        }
        addCommand(new CommandVEVersion());
        if (!disableClearCommand)
        {
            addCommand(new CommandVEClear());
        }

        //Debug commands
        addToDebugCommand(new CommandDebugRecipes());
        addToDebugCommand(new CommandDebugItem());
        addToDebugCommand(new CommandDebugMap());
        addToDebugCommand(new CommandDebugChunk());
        addToDebugCommand(new CommandDebugInventory());

        //Json recipe gen command
        addToJsonCommand(new CommandJsonRecipe());

        //Json override commands
        ModularCommand commandJsonOverride = new ModularCommand("override");
        commandJsonOverride.addCommand(new CommandJOGen("gen"));
        commandJsonOverride.addCommand(new CommandJOGen("generate"));
        commandJsonOverride.addCommand(new CommandJOGet());
        commandJsonOverride.addCommand(new CommandJOSet());
        addToJsonCommand(commandJsonOverride);

        //Utility commands
        addToThreadCommand(new CommandThreadClear());
        addToPowerCommand(new CommandChargeItem());
    }

    public void addToPowerCommand(AbstractCommand command)
    {
        if (sub_command_power == null)
        {
            sub_command_power = new ModularCommand("power");
            addCommand(sub_command_power);
        }
        sub_command_power.addCommand(command);
    }

    public void addToThreadCommand(AbstractCommand command)
    {
        if (sub_command_thread == null)
        {
            sub_command_thread = new ModularCommand("thread");
            addCommand(sub_command_thread);
        }
        sub_command_thread.addCommand(command);
    }

    public void addToJsonCommand(AbstractCommand command)
    {
        if (sub_command_json == null)
        {
            sub_command_json = new ModularCommand("json");
            addCommand(sub_command_json);
        }
        sub_command_json.addCommand(command);
    }

    public void addToDebugCommand(AbstractCommand command)
    {
        if (sub_command_debug == null)
        {
            sub_command_debug = new ModularCommand("debug");
            addCommand(sub_command_debug);
        }
        sub_command_debug.addCommand(command);
    }

    public void addToNewCommand(AbstractCommand command)
    {
        if (sub_command_new == null)
        {
            sub_command_new = new ModularCommand("new");
            addCommand(sub_command_new);
        }
        sub_command_new.addCommand(command);
    }

    public void addToRemoveCommand(AbstractCommand command)
    {
        if (sub_command_remove == null)
        {
            sub_command_remove = new ModularCommand("remove");
            addCommand(sub_command_remove);
        }
        sub_command_remove.addCommand(command);
    }

    public void addToDumpCommand(AbstractCommand command)
    {
        if (sub_command_dump == null)
        {
            sub_command_dump = new ModularCommand("dump");
            addCommand(sub_command_dump);
        }
        sub_command_dump.addCommand(command);
    }

    public void addToAddCommand(AbstractCommand command)
    {
        if (sub_command_add == null)
        {
            sub_command_add = new ModularCommand("add");
            addCommand(sub_command_add);
        }
        sub_command_add.addCommand(command);
    }

    public void addToAddUserCommand(SubCommandWithName command)
    {
        if (sub_command_add_user == null)
        {
            sub_command_add_user = new ModularCommandRemoveAdd("user", "user", false);
            addToAddCommand(sub_command_add_user);
        }
        sub_command_add_user.addCommand(command);
    }

    public void addToRemoveUserCommand(SubCommandWithName command)
    {
        if (sub_command_remove_user == null)
        {
            sub_command_remove_user = new ModularCommandRemoveAdd("user", "user", true);
            addToRemoveCommand(sub_command_remove_user);
        }
        sub_command_remove_user.addCommand(command);
    }

    public void addToAddPermCommand(SubCommandWithName command)
    {
        if (sub_command_add_perm == null)
        {
            sub_command_add_perm = new ModularCommandRemoveAdd("perm", "node", false);
            addToAddCommand(sub_command_add_perm);
        }
        sub_command_add_perm.addCommand(command);
    }

    public void addToRemovePermCommand(SubCommandWithName command)
    {
        if (sub_command_remove_perm == null)
        {
            sub_command_remove_perm = new ModularCommandRemoveAdd("perm", "node", true);
            addToRemoveCommand(sub_command_remove_perm);
        }
        sub_command_remove_perm.addCommand(command);
    }

    public void addToGroupCommand(GroupSubCommand command)
    {
        if (sub_command_group == null)
        {
            sub_command_group = new CommandGroup();
            addCommand(sub_command_group);
        }
        sub_command_group.addCommand(command);
    }

    public void addToUserCommand(UserSubCommand command)
    {
        if (sub_command_user == null)
        {
            sub_command_user = new CommandUser();
            addCommand(sub_command_user);
        }
        sub_command_user.addCommand(command);
    }
}
