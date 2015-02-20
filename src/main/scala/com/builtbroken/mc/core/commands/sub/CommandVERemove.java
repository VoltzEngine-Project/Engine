package com.builtbroken.mc.core.commands.sub;

import com.builtbroken.mc.prefab.commands.SubCommand;
import com.builtbroken.mc.prefab.entity.selector.EntitySelector;
import com.builtbroken.mc.prefab.entity.selector.EntitySelectors;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.List;

/**
 * Created by robert on 2/10/2015.
 */
public class CommandVERemove extends SubCommand
{
    public CommandVERemove()
    {
        super("remove");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer entityPlayer, String[] args)
    {
        if(!handleHelp(entityPlayer, args))
        {
            int radius = 100;

            //Get radius from player
            if (args.length >= 2 && args[1] != null)
            {
                try
                {
                    radius = Integer.parseInt(args[1]);
                    if (radius > 1000)
                    {
                        entityPlayer.addChatMessage(new ChatComponentText("To prevent lag/crashes radius is limited to 1000"));
                        return true;
                    }
                    else if (radius < 1)
                    {
                        entityPlayer.addChatMessage(new ChatComponentText("Radius needs to be positive"));
                        return true;
                    }
                } catch (NumberFormatException e)
                {
                    entityPlayer.addChatMessage(new ChatComponentText("Radius needs to be an integer"));
                    return true;
                }
            }

            EntitySelector selector = null;

            //Find out what entity selector to use
            if (args[0].equalsIgnoreCase("projectiles"))
            {
                selector = EntitySelectors.PLAYER_SELECTOR.selector();
            }
            else if (args[0].equalsIgnoreCase("mobs"))
            {
                selector = EntitySelectors.MOB_SELECTOR.selector();
            }
            else if (args[0].equalsIgnoreCase("living"))
            {
                selector = EntitySelectors.LIVING_SELECTOR.selector();
            }
            else if (args[0].equalsIgnoreCase("items"))
            {
                selector = EntitySelectors.ITEM_SELECTOR.selector();
            }

            if (selector != null)
            {
                List<Entity> list = selector.getEntities(entityPlayer, radius);
                for (Entity entity : list)
                {
                    entity.setDead();
                }
                entityPlayer.addChatMessage(new ChatComponentText("Removed " + list.size() + " " + args[0] + " entities within " + radius + " block radius"));
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isHelpCommand(String[] args)
    {
        return args != null && args.length > 0 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"));
    }

    @Override
    public void getHelpOutput(ICommandSender sender, List<String> items)
    {
        items.add("projectiles <radius>");
        items.add("mobs <radius>");
        items.add("living <radius>");
        items.add("items <radius>");
    }
}
