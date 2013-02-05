package universalelectricity.prefab.conductor;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import basiccomponents.common.tileentity.TileEntityCopperWire;

public abstract class ItemBlockConductor extends ItemBlock
{
	public ItemBlockConductor(int id)
	{
		super(id);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Resistance: " + ElectricInfo.getDisplay(TileEntityCopperWire.RESISTANCE, ElectricUnit.RESISTANCE));
		par3List.add("Max Amperage: " + ElectricInfo.getDisplay(TileEntityCopperWire.MAX_AMPS, ElectricUnit.AMPERE));
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int blockID = world.getBlockId(x, y, z);

		if (blockID == Block.snow.blockID)
		{
			side = 1;
		}
		else if (blockID != Block.vine.blockID && blockID != Block.tallGrass.blockID && blockID != Block.deadBush.blockID && (Block.blocksList[blockID] == null || !Block.blocksList[blockID].isBlockReplaceable(world, x, y, z)))
		{
			if (side == 0)
			{
				--y;
			}

			if (side == 1)
			{
				++y;
			}

			if (side == 2)
			{
				--z;
			}

			if (side == 3)
			{
				++z;
			}

			if (side == 4)
			{
				--x;
			}

			if (side == 5)
			{
				++x;
			}
		}

		System.out.println("Side: " + side);
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		int blockID = world.getBlockId(x, y, z);

		if (blockID == Block.snow.blockID)
		{
			side = 1;
		}
		else if (blockID != Block.vine.blockID && blockID != Block.tallGrass.blockID && blockID != Block.deadBush.blockID && (Block.blocksList[blockID] == null || !Block.blocksList[blockID].isBlockReplaceable(world, x, y, z)))
		{
			if (side == 0)
			{
				--y;
			}

			if (side == 1)
			{
				++y;
			}

			if (side == 2)
			{
				--z;
			}

			if (side == 3)
			{
				++z;
			}

			if (side == 4)
			{
				--x;
			}

			if (side == 5)
			{
				++x;
			}
		}

		if (itemStack.stackSize == 0)
		{
			return false;
		}
		else if (!entityPlayer.canPlayerEdit(x, y, z, side, itemStack))
		{
			return false;
		}
		else if (y == 255 && Block.blocksList[this.getBlockID()].blockMaterial.isSolid())
		{
			return false;
		}
		else if (world.canPlaceEntityOnSide(this.getBlockID(), x, y, z, false, side, entityPlayer))
		{
			Block var12 = Block.blocksList[this.getBlockID()];
			int var13 = this.getMetadata(itemStack.getItemDamage());
			int var14 = Block.blocksList[this.getBlockID()].onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, var13);

			if (placeBlockAt(itemStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ, var14))
			{
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
				itemStack.stackSize--;
			}

			return true;
		}
		else
		{
			return false;
		}
	}
}
