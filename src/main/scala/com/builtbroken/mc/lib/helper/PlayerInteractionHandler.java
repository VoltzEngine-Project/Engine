package com.builtbroken.mc.lib.helper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import com.builtbroken.mc.api.tile.IRemovable;
import com.builtbroken.mc.api.tile.IRemovable.*;
import com.builtbroken.mc.core.References;
import com.builtbroken.mc.lib.transform.vector.Vector3;
import com.builtbroken.mc.lib.transform.vector.VectorWorld;
import com.builtbroken.mc.lib.helper.inventory.InventoryUtility;

import java.util.List;

/**
 * Handles interaction with the player providing helper methods and
 * event handling.
 *
 * @author Darkguardsman
 */
public class PlayerInteractionHandler
{
    @SideOnly(Side.CLIENT)
    public static Block getMouseOverBlockClient()
    {
        MovingObjectPosition m = getMouseOverClient();
        if (m != null && m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            return new VectorWorld(Minecraft.getMinecraft().theWorld, m.blockX, m.blockY, m.blockZ).getBlock();
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static VectorWorld getAimHitClient()
    {
        MovingObjectPosition m = getMouseOverClient();
        if (m != null)
        {
            if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                return new VectorWorld(Minecraft.getMinecraft().theWorld, m.blockX, m.blockY, m.blockZ);
            }
            else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
            {
                return new VectorWorld(m.entityHit);
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static MovingObjectPosition getMouseOverClient()
    {
        return Minecraft.getMinecraft().objectMouseOver;
    }

    /**
     * Gets the block the entity is aiming
     *
     * @param entity   - entity to trace the aim from
     * @param distance - range to trace
     * @return hit position
     */
    public static VectorWorld getAimHit(EntityLivingBase entity, double distance)
    {
        MovingObjectPosition m = getMouseOver(entity, distance);
        if (m != null)
        {
            if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                return new VectorWorld(entity.worldObj, m.blockX, m.blockY, m.blockZ);
            }
            else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
            {
                return new VectorWorld(m.entityHit);
            }
        }
        return null;
    }

    /**
     * Gets the block the entity is aiming at up to 10 blocks away
     *
     * @param entity   - entity to trace the aim from
     * @param distance - range to trace
     * @return hit object
     */
    public static MovingObjectPosition getMouseOver(EntityLivingBase entity, double distance)
    {
        Vector3 start = new Vector3(entity);

        Vector3 aim = new Vector3(entity.getLook(1));
        Vector3 extended = aim.multiply(distance);
        aim.addEquals(start);
        extended.addEquals(start);

        return aim.rayTrace(entity.worldObj, extended);
    }

    @SubscribeEvent
    public void onPlayInteract(PlayerInteractEvent event)
    {
        if (!event.entityPlayer.worldObj.isRemote && event.entityPlayer != null)
        {
            VectorWorld vec = new VectorWorld(event.entityPlayer.worldObj, event.x, event.y, event.z);
            TileEntity tile = vec.getTileEntity();

            if (event.action == Action.RIGHT_CLICK_BLOCK)
            {
                //Handle IRemovable allow more uniform removal of blocks
                if (tile instanceof IRemovable)
                {
                    boolean do_drop = false;
                    List<ItemStack> drops;

                    if (tile instanceof ICustomRemoval)
                    {
                        do_drop = ((ICustomRemoval) tile).canBeRemoved(event.entityPlayer);
                    }
                    else if (tile instanceof ISneakWrenchable)
                    {
                        do_drop = event.entityPlayer.isSneaking() && WrenchUtility.isHoldingWrench(event.entityPlayer);
                    }
                    else if (tile instanceof IWrenchable)
                    {
                        do_drop = WrenchUtility.isHoldingWrench(event.entityPlayer);
                    }
                    else if (tile instanceof ISneakPickup)
                    {
                        do_drop = event.entityPlayer.isSneaking() && event.entityPlayer.getHeldItem() == null;
                    }
                    else
                    {
                        do_drop = tile instanceof IPickup && event.entityPlayer.getHeldItem() == null;
                    }

                    if (do_drop)
                    {
                        drops = ((IRemovable) tile).getRemovedItems(event.entityPlayer);
                        //Not sure if we need to cancel but there is nothing to right click after this
                        if (event.isCancelable())
                        {
                            event.setCanceled(true);
                        }

                        //Drop all items
                        try
                        {
                            vec.world().removeTileEntity(vec.xi(), vec.yi(), vec.zi());
                            vec.setBlock(Blocks.air);

                            if (drops != null && !drops.isEmpty())
                            {
                                for (ItemStack item : drops)
                                {
                                    if (!event.entityPlayer.inventory.addItemStackToInventory(item))
                                    {
                                        InventoryUtility.dropItemStack(vec, item);
                                    }
                                    else
                                    {
                                        event.entityPlayer.inventory.markDirty();
                                    }
                                }
                            }
                        } catch (Exception e)
                        {
                            References.LOGGER.error("Failed to pick up block using event system");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
