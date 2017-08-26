package com.builtbroken.mc.core.handler;

import com.builtbroken.mc.api.tile.IRemovable;
import com.builtbroken.mc.api.tile.IRemovable.*;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.imp.transform.vector.Location;
import com.builtbroken.mc.lib.helper.WrenchUtility;
import com.builtbroken.mc.prefab.inventory.InventoryUtility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Handles interaction with the player providing helper methods and
 * event handling.
 *
 * @author Darkguardsman
 */
public class InteractionHandler
{
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (player != null && !player.world.isRemote)
        {
            Location vec = new Location(event.getEntityPlayer().world, event.getPos());
            TileEntity tile = vec.getTileEntity();

            //Handle IRemovable allow more uniform removal of blocks
            if (tile instanceof IRemovable)
            {
                boolean do_drop = false;
                List<ItemStack> drops;

                if (tile instanceof ICustomRemoval)
                {
                    do_drop = ((ICustomRemoval) tile).canBeRemoved(player);
                }
                else if (tile instanceof ISneakWrenchable)
                {
                    do_drop = player.isSneaking() && WrenchUtility.isHoldingWrench(player, event.getHand());
                }
                else if (tile instanceof IWrenchable)
                {
                    do_drop = WrenchUtility.isHoldingWrench(player, event.getHand());
                }
                else if (tile instanceof ISneakPickup)
                {
                    do_drop = player.isSneaking() && player.getHeldItem(event.getHand()) == null;
                }
                else
                {
                    do_drop = tile instanceof IPickup && player.getHeldItem(event.getHand()) == null;
                }

                if (do_drop)
                {
                    drops = ((IRemovable) tile).getRemovedItems(player);
                    //Not sure if we need to cancel but there is nothing to right click after this
                    if (event.isCancelable())
                    {
                        event.setCanceled(true);
                    }

                    //Drop all items
                    try
                    {
                        vec.oldWorld().removeTileEntity(event.getPos());
                        vec.setBlock(Blocks.AIR);

                        if (drops != null && !drops.isEmpty())
                        {
                            for (ItemStack item : drops)
                            {
                                if (!player.inventory.addItemStackToInventory(item))
                                {
                                    InventoryUtility.dropItemStack(vec, item);
                                }
                                else
                                {
                                    player.inventory.markDirty();
                                }
                            }
                            player.inventoryContainer.detectAndSendChanges();
                        }
                    }
                    catch (Exception e)
                    {
                        Engine.logger().error("Failed to pick up block using event system");
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
