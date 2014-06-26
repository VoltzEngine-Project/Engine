package resonant.lib.utility;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import resonant.api.IRemovable;
import resonant.api.IRemovable.*;
import resonant.engine.References;
import resonant.lib.utility.inventory.InventoryUtility;
import universalelectricity.core.transform.vector.VectorWorld;

import java.util.List;

/**
 * Interaction handler for the player allowing the detection of block interaction without the block
 * detecting it.
 *
 * @author Darkguardsman
 */
public class PlayerInteractionHandler
{
	@SubscribeEvent
	public void onPlayInteract(PlayerInteractEvent event)
	{
		if (!event.entityPlayer.worldObj.isRemote)
		{
			if (event.entityPlayer != null)
			{
				VectorWorld vec = new VectorWorld(event.entityPlayer.worldObj, event.x, event.y, event.z);
				//int block_id = vec.getBlockID(); For later use
				//int block_meta = vec.getBlockMetadata();
				//Block block = Block.blocksList[block_id];
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
							}
							catch (Exception e)
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
}
