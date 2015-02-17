package com.builtbroken.mc.core;

import com.builtbroken.mc.core.commands.CommandVE;
import com.builtbroken.mc.core.commands.permissions.*;
import com.builtbroken.mc.lib.helper.ReflectionUtility;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;

/**
 * Created by robert on 2/17/2015.
 */
public class ServerProxy extends CommonProxy
{
    @Override
    public void init()
    {
        super.init();
        if (CommandPermissionHandler.enablePermissions)
        {
            Engine.instance.logger().info("Overriding MC's CommandManager");
            Field field = ReflectionUtility.getMCField(MinecraftServer.class, "commandManager");
            field.setAccessible(true);
            try
            {
                ReflectionUtility.setFinalField(FMLCommonHandler.instance().getMinecraftServerInstance(), field, new PermissionsCommandManager());
                Engine.instance.logger().info("New command manager set to " + FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager());
            } catch (NoSuchFieldException e)
            {
                Engine.instance.logger().error("Failed to override command manager as the field was not found");
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                Engine.instance.logger().error("Failed to override command manager as our access was blocked");
                e.printStackTrace();
            }
        }

        if (FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager() instanceof PermissionsCommandManager)
        {
            //Load up permission commands here, since they don't work without this the permission manager being setup
            CommandVE.INSTANCE.addToNewCommand(new CommandNewGroup());
            CommandVE.INSTANCE.addToRemoveCommand(new CommandRemoveGroup());
            CommandVE.INSTANCE.addToDumpCommand(new CommandDumpPermissions());
        }
    }
}
