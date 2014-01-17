package calclavia.lib.prefab.block;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import buildcraft.api.tools.IToolWrench;
import calclavia.lib.utility.inventory.InventoryUtility;

/**
 * An advanced block class that is to be extended for wrenching capabilities.
 */
public abstract class BlockAdvanced extends Block
{
	public BlockAdvanced(int id, Material material)
	{
		super(id, material);
		this.setHardness(0.6f);
	}

	/**
	 * DO NOT OVERRIDE THIS FUNCTION! Called when the block is right clicked by the player. This
	 * modified version detects electric items and wrench actions on your machine block. Do not
	 * override this function. Use onMachineActivated instead! (It does the same thing)
	 * 
	 * @param world The World Object.
	 * @param x , y, z The coordinate of the block.
	 * @param side The side the player clicked on.
	 * @param hitX , hitY, hitZ The position the player clicked on relative to the block.
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = world.getBlockMetadata(x, y, z);

		/**
		 * Check if the player is holding a wrench or an electric item. If so, call the wrench
		 * event.
		 */
		if (this.isUsableWrench(entityPlayer, entityPlayer.inventory.getCurrentItem(), x, y, z))
		{
			this.damageWrench(entityPlayer, entityPlayer.inventory.getCurrentItem(), x, y, z);

			if (entityPlayer.isSneaking())
			{
				if (this.onSneakUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ))
				{
					return true;
				}
			}

			if (this.onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ))
			{
				return true;
			}

			return false;
		}

		if (entityPlayer.isSneaking())
		{
			if (this.onSneakMachineActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ))
			{
				return true;
			}
		}

		return this.onMachineActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
	}

	/**
	 * A function that denotes if an itemStack is a wrench that can be used. Override this for more
	 * wrench compatibility. Compatible with Buildcraft and IC2 wrench API via reflection.
	 * 
	 * @return True if it is a wrench.
	 */
	public boolean isUsableWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
	{
		if (player != null && itemStack != null)
		{
			if (itemStack.getItem() instanceof IToolWrench)
			{
				return ((IToolWrench) itemStack.getItem()).canWrench(player, x, y, z);
			}

			/**
			 * Industrialcraft
			 */
			try
			{
				Class wrenchClass = itemStack.getItem().getClass();

				if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
				{
					return itemStack.getItemDamage() < itemStack.getMaxDamage();
				}
			}
			catch (Exception e)
			{
			}
		}

		return false;
	}

	/**
	 * This function damages a wrench. Works with Buildcraft and Industrialcraft wrenches.
	 * 
	 * @return True if damage was successfull.
	 */
	public boolean damageWrench(EntityPlayer player, ItemStack itemStack, int x, int y, int z)
	{
		if (this.isUsableWrench(player, itemStack, x, y, z))
		{
			if (itemStack.getItem() instanceof IToolWrench)
			{
				((IToolWrench) itemStack.getItem()).wrenchUsed(player, x, y, z);
				return true;
			}

			/**
			 * Industrialcraft
			 */
			try
			{
				Class wrenchClass = itemStack.getItem().getClass();

				if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
				{
					Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, EntityPlayer.class);
					methodWrenchDamage.invoke(itemStack.getItem(), itemStack, 1, player);
					return true;
				}
			}
			catch (Exception e)
			{
			}
		}

		return false;
	}

	/**
	 * Called when the machine is right clicked by the player
	 * 
	 * @return True if something happens
	 */
	public boolean onMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	/**
	 * Called when the machine is being wrenched by a player while sneaking.
	 * 
	 * @return True if something happens
	 */
	public boolean onSneakMachineActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	/**
	 * Called when a player uses a wrench on the machine
	 * 
	 * @return True if some happens
	 */
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return false;
	}

	/**
	 * Called when a player uses a wrench on the machine while sneaking. Only works with the UE
	 * wrench.
	 * 
	 * @return True if some happens
	 */
	public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return this.onUseWrench(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
	}

	/**
	 * Player-Inventory interaction methods.
	 */
	public boolean interactCurrentItem(IInventory inventory, int slotID, EntityPlayer player)
	{
		ItemStack stackInInventory = inventory.getStackInSlot(slotID);
		ItemStack current = player.inventory.getCurrentItem();

		/**
		 * Try to insert.
		 */
		if (current != null)
		{
			if (stackInInventory == null || stackInInventory.isItemEqual(current))
			{
				return insertCurrentItem(inventory, slotID, player);

			}
		}

		/**
		 * Try to extract.
		 */
		return extractItem(inventory, slotID, player);
	}

	public boolean insertCurrentItem(IInventory inventory, int slotID, EntityPlayer player)
	{
		ItemStack stackInInventory = inventory.getStackInSlot(slotID);
		ItemStack current = player.inventory.getCurrentItem();

		if (current != null)
		{
			if (stackInInventory == null || stackInInventory.isItemEqual(current))
			{
				if (inventory.isItemValidForSlot(slotID, current))
				{
					/**
					 * If control is down, insert one only.
					 */
					if (isControlDown(player))
					{
						if (stackInInventory == null)
						{
							inventory.setInventorySlotContents(slotID, current.splitStack(1));
						}
						else
						{
							stackInInventory.stackSize++;
							current.stackSize--;
						}
					}
					else
					{
						if (stackInInventory == null)
						{
							inventory.setInventorySlotContents(slotID, current);
						}
						else
						{
							stackInInventory.stackSize += current.stackSize;
							current.stackSize = 0;
						}

						current = null;
					}

					if (current == null || current.stackSize <= 0)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
					}

					return true;
				}
			}
		}

		return false;
	}

	public boolean extractItem(IInventory inventory, int slotID, EntityPlayer player)
	{
		ItemStack stackInInventory = inventory.getStackInSlot(slotID);

		if (stackInInventory != null)
		{
			/**
			 * If control is down, insert one only.
			 */
			if (isControlDown(player))
			{
				InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory.splitStack(1), 0);
			}
			else
			{
				InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory, 0);
				stackInInventory = null;
			}

			if (stackInInventory == null || stackInInventory.stackSize <= 0)
			{
				inventory.setInventorySlotContents(slotID, null);
			}

			return true;
		}

		return false;
	}

	public boolean isControlDown(EntityPlayer player)
	{
		return false;
	}
}
