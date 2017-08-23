package com.builtbroken.mc.core.commands.energy;

import com.builtbroken.mc.core.commands.prefab.SubCommand;
import com.builtbroken.mc.framework.energy.UniversalEnergySystem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/7/2017.
 */
public class CommandChargeItem extends SubCommand
{
    public CommandChargeItem()
    {
        super("charge");
    }

    @Override
    public boolean handleEntityPlayerCommand(EntityPlayer player, String[] args)
    {
        if (args != null && args.length > 0 && !"help".equalsIgnoreCase(args[0]))
        {
            if (args[0].equalsIgnoreCase("item")) //TODO consider making held version and full inventory version
            {
                boolean full = false;
                int energy = 0;

                if (args.length > 1)
                {
                    if (args[1].equalsIgnoreCase("full"))
                    {
                        full = true;
                    }
                    else
                    {
                        try
                        {
                            energy = Integer.parseInt(args[1]);
                            if (energy <= 0)
                            {
                                player.sendMessage(new TextComponentString("Energy value must be greater than zero!"));
                                return true;
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            player.sendMessage(new TextComponentString("Need charge value of integer (ex 1, 10, 100) or use the word 'full' complete charge!"));
                            return true;
                        }
                    }
                }
                else
                {
                    full = true;
                }

                ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
                if (stack != null)
                {
                    if (UniversalEnergySystem.isHandler(stack, null))
                    {
                        int received = 0;
                        if (full)
                        {
                            received = (int) UniversalEnergySystem.setFullCharge(stack);
                            player.inventoryContainer.detectAndSendChanges();
                        }
                        else if (energy > 0)
                        {
                            received = (int) UniversalEnergySystem.fill(stack, null, energy, true);
                            player.inventoryContainer.detectAndSendChanges();
                        }
                        else
                        {
                            player.sendMessage(new TextComponentString("Error: Failed to select charge type of 'energy' or 'full' value, this is a logical error (bug) and not a user error"));
                        }

                        //TODO customize output based on mod power system
                        if (received == 1)
                        {
                            player.sendMessage(new TextComponentString("Item charged with " + received + " watt of energy"));
                        }
                        else if (received > 0)
                        {
                            player.sendMessage(new TextComponentString("Item charged with " + received + " watts of energy"));
                        }
                    }
                    else
                    {
                        player.sendMessage(new TextComponentString("Held item is not supported for energy based charging. If this is a mistake, make an issue at https://github.com/VoltzEngine-Project/Engine/issues asking for support to be added."));
                    }
                }
                else
                {
                    player.sendMessage(new TextComponentString("No held item to charge!"));
                }
            }
            else if (args[0].equalsIgnoreCase("machine") || args[0].equalsIgnoreCase("tile"))
            {
                player.sendMessage(new TextComponentString("Not implemented yet!"));
            }
            return true;
        }
        return false;
    }
}
