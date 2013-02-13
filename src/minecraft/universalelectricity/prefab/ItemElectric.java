package universalelectricity.prefab;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IItemElectric;

/**
 * ItemElectric is a base class for electrical items.
 * 
 * @author Calclavia
 * 
 */
public abstract class ItemElectric extends Item implements IItemElectric
{
	public ItemElectric(int id)
	{
		super(id);
		this.setMaxStackSize(1);
		this.setMaxDamage((int) this.getMaxJoules(null));
		this.setNoRepair();
	}

	/**
	 * Allows items to add custom lines of information to the mouseover description. If you want to
	 * add more information to your item, you can super.addInformation() to keep the electiricty
	 * info in the item info bar.
	 */
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		String color = "";
		double joules = this.getJoules(par1ItemStack);

		if (joules <= this.getMaxJoules(par1ItemStack) / 3)
		{
			color = "\u00a74";
		}
		else if (joules > this.getMaxJoules(par1ItemStack) * 2 / 3)
		{
			color = "\u00a72";
		}
		else
		{
			color = "\u00a76";
		}

		par3List.add(color + ElectricInfo.getDisplay(joules, ElectricUnit.JOULES) + " - " + Math.round((joules / this.getMaxJoules(par1ItemStack)) * 100) + "%");
	}

	/**
	 * Make sure you super this method!
	 */
	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity, int par4, boolean par5)
	{
		if (!world.isRemote)
		{
			// Makes sure the damage is set correctly for this electric item!
			ItemElectric item = ((ItemElectric) itemStack.getItem());
			item.setJoules(item.getJoules(itemStack), itemStack);

			// For items that can change electricity capacity
			this.setMaxDamage((int) this.getMaxJoules(itemStack));
		}
	}

	/**
	 * Makes sure the item is uncharged when it is crafted and not charged.
	 */
	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par1ItemStack = this.getUncharged();
	}

	@Override
	public ElectricityPack onReceive(ElectricityPack electricityPack, ItemStack itemStack)
	{
		double totalJoules = this.getJoules(itemStack) + electricityPack.getWatts();
		double rejectedJoules = Math.max(totalJoules - this.getMaxJoules(itemStack), 0);
		this.setJoules(totalJoules - rejectedJoules, itemStack);
		return new ElectricityPack(rejectedJoules / this.getJoules(itemStack), this.getJoules(itemStack));
	}

	@Override
	public ElectricityPack onRequest(ElectricityPack electricityPack, ItemStack itemStack)
	{
		double joulesToGive = Math.min(this.getJoules(itemStack), electricityPack.getWatts());
		this.setJoules(this.getJoules(itemStack) - joulesToGive, itemStack);
		return new ElectricityPack(joulesToGive / this.getVoltage(itemStack), this.getVoltage(itemStack));
	}

	@Override
	public void setJoules(double joules, ItemStack itemStack)
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		joules = Math.max(Math.min(joules, this.getMaxJoules(itemStack)), 0);
		itemStack.getTagCompound().setDouble("joules", joules);
		itemStack.setItemDamage((int) (this.getMaxJoules(itemStack) - joules));
	}

	@Override
	public double getJoules(ItemStack itemStack)
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
			return 0;
		}

		double joules = Math.max(Math.min(itemStack.getTagCompound().getDouble("joules"), this.getMaxJoules(itemStack)), 0);
		itemStack.setItemDamage((int) (getMaxJoules(itemStack) - joules));

		return joules;
	}

	/**
	 * Returns an uncharged version of the electric item. Use this if you want the crafting recipe
	 * to use a charged version of the electric item instead of an empty version of the electric
	 * item
	 * 
	 * @return The ItemStack of a fully charged electric item
	 */
	public ItemStack getUncharged()
	{
		ItemStack chargedItem = new ItemStack(this);
		chargedItem.setItemDamage((int) this.getMaxJoules(chargedItem));
		return chargedItem;
	}

	public static ItemStack getUncharged(ItemStack itemStack)
	{
		if (itemStack.getItem() instanceof IItemElectric)
		{
			ItemStack chargedItem = itemStack.copy();
			chargedItem.setItemDamage((int) ((IItemElectric) itemStack.getItem()).getMaxJoules(chargedItem));
			return chargedItem;
		}

		return null;
	}

	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		// Add an uncharged version of the electric item
		ItemStack unchargedItem = new ItemStack(this, 1);
		unchargedItem.setItemDamage((int) this.getMaxJoules(unchargedItem));
		par3List.add(unchargedItem);

		// Add an electric item to the creative list that is fully charged
		ItemStack chargedItem = new ItemStack(this, 1);
		this.setJoules(((IItemElectric) chargedItem.getItem()).getMaxJoules(chargedItem), chargedItem);
		par3List.add(chargedItem);
	}

	@Override
	public double getVoltage(ItemStack itemStack)
	{
		return 30;
	}

	@Override
	public ElectricityPack getTransferRate(ItemStack itemStack)
	{
		return new ElectricityPack((this.getMaxJoules(itemStack) * 0.005) / this.getVoltage(itemStack), this.getVoltage(itemStack));
	}
}
