package com.builtbroken.mc.core.commands.sub;

import com.builtbroken.mc.prefab.commands.SubCommand;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVEButcher extends SubCommand
{
    public CommandVEButcher()
    {
        super("butcher");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer entityPlayer, String[] args)
    {
        List<Entity> list = EntitySelectors.MOB_SELECTOR.selector().getEntities(entityPlayer, 100);
        for (Entity entity : list)
        {
            entity.setDead();
        }
        entityPlayer.addChatComponentMessage(new ChatComponentText("Removed " + list.size() + " mobs entities within " + 100 + " block radius."));
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return false;
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("");
    }
}
