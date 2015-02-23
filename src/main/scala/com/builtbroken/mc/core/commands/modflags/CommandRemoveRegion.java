package com.builtbroken.mc.core.commands.modflags;

import com.builtbroken.mc.core.commands.ext.SubCommandRegion;
import com.builtbroken.mc.lib.modflags.Region;
import net.minecraft.command.ICommandSender;

/**
 * Created by robert on 2/17/2015.
 */
public class CommandRemoveRegion extends SubCommandRegion
{
    @Override
    public boolean handle(ICommandSender sender, Region region, String user, String[] args)
    {
        return false;
    }
}
