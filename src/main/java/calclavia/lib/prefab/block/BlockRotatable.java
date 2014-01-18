package calclavia.lib.prefab.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * A block that can rotate based on placed position and wrenching.
 * 
 * @author Calclavia
 */
public abstract class BlockRotatable extends BlockTile implements IRotatableBlock
{
	protected byte rotationMask = 0b111100;

	public BlockRotatable(int id, Material material)
	{
		super(id, material);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		world.setBlockMetadataWithNotify(x, y, z, determineOrientation(world, x, y, z, entityLiving), 3);
	}

	public int determineOrientation(World world, int x, int y, int z, EntityLivingBase entityLiving)
	{
		if (MathHelper.abs((float) entityLiving.posX - (float) x) < 2.0F && MathHelper.abs((float) entityLiving.posZ - (float) z) < 2.0F)
		{
			double d0 = entityLiving.posY + 1.82D - (double) entityLiving.yOffset;

			if ((rotationMask & (1 << 1)) != 0 && d0 - (double) y > 2.0D)
			{
				return 1;
			}

			if ((rotationMask & (1 << 0)) != 0 && (double) y - d0 > 0.0D)
			{
				return 0;
			}
		}

		int l = MathHelper.floor_double((double) (entityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
	}

	@Override
	public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		return this.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
	}

	@Override
	public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			world.setBlockMetadataWithNotify(x, y, z, side, 3);
		}

		return true;
	}

	@Override
	public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
	{
		int currentRotMeta = worldObj.getBlockMetadata(x, y, z);
		ForgeDirection orientation = ForgeDirection.getOrientation(currentRotMeta);
		ForgeDirection rotated = orientation.getRotation(axis);
		int rmeta = rotated.ordinal();
		int rmetaBit = 1 << rmeta;

		if ((rmetaBit & this.rotationMask) == rmetaBit)
		{
			worldObj.setBlockMetadataWithNotify(x, y, z, rmeta, 3);
			return true;
		}

		return false;
	}

	@Override
	public ForgeDirection getDirection(World world, int x, int y, int z)
	{
		return ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
	}

	@Override
	public void setDirection(World world, int x, int y, int z, ForgeDirection direction)
	{
		world.setBlockMetadataWithNotify(x, y, z, direction.ordinal(), 3);
	}
}