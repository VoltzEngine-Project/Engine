package basiccomponents.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.UniversalElectricity;
import universalelectricity.prefab.BlockMachine;
import universalelectricity.prefab.UETab;
import universalelectricity.prefab.implement.IRedstoneProvider;
import universalelectricity.prefab.tile.TileEntityAdvanced;
import basiccomponents.common.BCLoader;
import basiccomponents.common.BasicComponents;
import basiccomponents.common.tileentity.TileEntityBatteryBox;
import basiccomponents.common.tileentity.TileEntityCoalGenerator;
import basiccomponents.common.tileentity.TileEntityElectricFurnace;

public class BlockBasicMachine extends BlockMachine
{
	public static final int COAL_GENERATOR_METADATA = 0;
	public static final int BATTERY_BOX_METADATA = 4;
	public static final int ELECTRIC_FURNACE_METADATA = 8;

	public BlockBasicMachine(int id, int textureIndex)
	{
		super("bcMachine", id, UniversalElectricity.machine, UETab.INSTANCE);
		this.blockIndexInTexture = textureIndex;
		this.setStepSound(soundMetalFootstep);
	}

	@Override
	public String getTextureFile()
	{
		return BasicComponents.BLOCK_TEXTURE_FILE;
	}

	@Override
	public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
	{
		TileEntity tile = par1World.getBlockTileEntity(x, y, z);

		if (tile instanceof TileEntityCoalGenerator)
		{
			TileEntityCoalGenerator tileEntity = (TileEntityCoalGenerator) tile;
			if (tileEntity.generateWatts > 0)
			{
				int metadata = par1World.getBlockMetadata(x, y, z);
				float var7 = (float) x + 0.5F;
				float var8 = (float) y + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
				float var9 = (float) z + 0.5F;
				float var10 = 0.52F;
				float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

				if (metadata == 3)
				{
					par1World.spawnParticle("smoke", (double) (var7 - var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", (double) (var7 - var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
				}
				else if (metadata == 2)
				{
					par1World.spawnParticle("smoke", (double) (var7 + var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", (double) (var7 + var10), (double) var8, (double) (var9 + var11), 0.0D, 0.0D, 0.0D);
				}
				else if (metadata == 1)
				{
					par1World.spawnParticle("smoke", (double) (var7 + var11), (double) var8, (double) (var9 - var10), 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", (double) (var7 + var11), (double) var8, (double) (var9 - var10), 0.0D, 0.0D, 0.0D);
				}
				else if (metadata == 0)
				{
					par1World.spawnParticle("smoke", (double) (var7 + var11), (double) var8, (double) (var9 + var10), 0.0D, 0.0D, 0.0D);
					par1World.spawnParticle("flame", (double) (var7 + var11), (double) var8, (double) (var9 + var10), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata)
	{
		if (side == 0 || side == 1) { return this.blockIndexInTexture; }

		if (metadata >= ELECTRIC_FURNACE_METADATA)
		{
			metadata -= ELECTRIC_FURNACE_METADATA;

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.blockIndexInTexture + 2;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 6; }
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			metadata -= BATTERY_BOX_METADATA;

			// If it is the front side
			if (side == metadata + 2)
			{
				return this.blockIndexInTexture + 3;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 2; }

			return this.blockIndexInTexture + 4;
		}
		else
		{
			// If it is the front side
			if (side == metadata + 2)
			{
				return this.blockIndexInTexture + 3;
			}
			// If it is the back side
			else if (side == ForgeDirection.getOrientation(metadata + 2).getOpposite().ordinal()) { return this.blockIndexInTexture + 5; }
		}

		return this.blockIndexInTexture + 1;
	}

	/**
	 * Called when the block is placed in the world.
	 */
	@Override
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int change = 3;

		if (metadata >= ELECTRIC_FURNACE_METADATA)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadata(x, y, z, ELECTRIC_FURNACE_METADATA + 1);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, ELECTRIC_FURNACE_METADATA + 2);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, ELECTRIC_FURNACE_METADATA + 0);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, ELECTRIC_FURNACE_METADATA + 3);
					break;
			}
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 3);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 1);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 2);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, BATTERY_BOX_METADATA + 0);
					break;
			}
		}
		else
		{
			switch (angle)
			{
				case 0:
					par1World.setBlockMetadata(x, y, z, COAL_GENERATOR_METADATA + 1);
					break;
				case 1:
					par1World.setBlockMetadata(x, y, z, COAL_GENERATOR_METADATA + 2);
					break;
				case 2:
					par1World.setBlockMetadata(x, y, z, COAL_GENERATOR_METADATA + 0);
					break;
				case 3:
					par1World.setBlockMetadata(x, y, z, COAL_GENERATOR_METADATA + 3);
					break;
			}
		}

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();
		par1World.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
	}

	@Override
	public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);
		int original = metadata;

		int change = 0;

		if (metadata >= ELECTRIC_FURNACE_METADATA)
		{
			original -= ELECTRIC_FURNACE_METADATA;
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			original -= BATTERY_BOX_METADATA;
		}

		// Re-orient the block
		switch (original)
		{
			case 0:
				change = 3;
				break;
			case 3:
				change = 1;
				break;
			case 1:
				change = 2;
				break;
			case 2:
				change = 0;
				break;
		}

		if (metadata >= ELECTRIC_FURNACE_METADATA)
		{
			change += ELECTRIC_FURNACE_METADATA;
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			change += BATTERY_BOX_METADATA;
		}

		par1World.setBlockMetadata(x, y, z, change);

		((TileEntityAdvanced) par1World.getBlockTileEntity(x, y, z)).initiate();

		return true;
	}

	/**
	 * Called when the block is right clicked by the player
	 */
	@Override
	public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		int metadata = par1World.getBlockMetadata(x, y, z);

		if (!par1World.isRemote)
		{
			if (metadata >= ELECTRIC_FURNACE_METADATA)
			{
				par5EntityPlayer.openGui(BCLoader.instance, 2, par1World, x, y, z);
				return true;
			}
			else if (metadata >= BATTERY_BOX_METADATA)
			{
				par5EntityPlayer.openGui(BCLoader.instance, 0, par1World, x, y, z);
				return true;
			}
			else
			{
				par5EntityPlayer.openGui(BCLoader.instance, 1, par1World, x, y, z);
				return true;
			}
		}

		return true;
	}

	/**
	 * Is this block powering the block on the specified side
	 */
	@Override
	public boolean isProvidingStrongPower(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IRedstoneProvider) { return ((IRedstoneProvider) tileEntity).isPoweringTo(ForgeDirection.getOrientation(side)); }

		return false;
	}

	/**
	 * Is this block indirectly powering the block on the specified side
	 */
	@Override
	public boolean isProvidingWeakPower(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
	{
		TileEntity tileEntity = par1IBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity instanceof IRedstoneProvider) { return ((IRedstoneProvider) tileEntity).isIndirectlyPoweringTo(ForgeDirection.getOrientation(side)); }

		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		if (metadata >= ELECTRIC_FURNACE_METADATA)
		{
			return new TileEntityElectricFurnace();
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			return new TileEntityBatteryBox();
		}
		else
		{
			return new TileEntityCoalGenerator();
		}

	}

	public ItemStack getCoalGenerator()
	{
		return new ItemStack(this.blockID, 1, COAL_GENERATOR_METADATA);
	}

	public ItemStack getBatteryBox()
	{
		return new ItemStack(this.blockID, 1, BATTERY_BOX_METADATA);
	}

	public ItemStack getElectricFurnace()
	{
		return new ItemStack(this.blockID, 1, ELECTRIC_FURNACE_METADATA);
	}

	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(this.getCoalGenerator());
		par3List.add(this.getBatteryBox());
		par3List.add(this.getElectricFurnace());
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int id = idPicked(world, x, y, z);

		if (id == 0) { return null; }

		Item item = Item.itemsList[id];
		if (item == null) { return null; }

		int metadata = getDamageValue(world, x, y, z);

		if (metadata >= ELECTRIC_FURNACE_METADATA)
		{
			metadata = ELECTRIC_FURNACE_METADATA;
		}
		else if (metadata >= BATTERY_BOX_METADATA)
		{
			metadata = BATTERY_BOX_METADATA;
		}
		else
		{
			metadata = COAL_GENERATOR_METADATA;
		}

		return new ItemStack(id, 1, metadata);
	}
}
