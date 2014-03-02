package calclavia.lib.prefab.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.utility.inventory.InventoryUtility;
import calclavia.lib.utility.nbt.NBTUtility;

/**
 * An item that can store a block's tile data.
 * 
 * @author Calclavia
 * 
 */
public class ItemBlockSaved extends ItemBlock
{
	public ItemBlockSaved(int par1)
	{
		super(par1);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		boolean flag = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile != null)
		{
			/**
			 * Inject essential tile data
			 */
			NBTTagCompound essentialNBT = new NBTTagCompound();
			tile.writeToNBT(essentialNBT);
			NBTTagCompound setNbt = NBTUtility.getNBTTagCompound(stack);

			if (essentialNBT.hasKey("id"))
			{
				setNbt.setString("id", essentialNBT.getString("id"));
				setNbt.setInteger("x", essentialNBT.getInteger("x"));
				setNbt.setInteger("y", essentialNBT.getInteger("y"));
				setNbt.setInteger("z", essentialNBT.getInteger("z"));
			}

			tile.readFromNBT(setNbt);
		}

		return flag;
	}

	public static ItemStack getItemStackWithNBT(Block block, World world, int x, int y, int z)
	{
		if (block != null)
		{
			int meta = world.getBlockMetadata(x, y, z);

			ItemStack dropStack = new ItemStack(block, block.quantityDropped(meta, 0, world.rand), meta);
			NBTTagCompound tag = new NBTTagCompound();

			TileEntity tile = world.getBlockTileEntity(x, y, z);

			if (tile != null)
				tile.writeToNBT(tag);

			tag.removeTag("id");
			tag.removeTag("x");
			tag.removeTag("y");
			tag.removeTag("z");
			dropStack.setTagCompound(tag);
			return dropStack;
		}

		return null;
	}

	public static void dropBlockWithNBT(Block block, World world, int x, int y, int z)
	{
		if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops"))
		{
			ItemStack itemStack = getItemStackWithNBT(block, world, x, y, z);

			if (itemStack != null)
				InventoryUtility.dropItemStack(world, new Vector3(x, y, z), itemStack);
		}
	}
}
