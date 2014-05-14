package resonant.lib.prefab.block;

import resonant.api.blocks.IRotatableBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

/** A block that can rotate based on placed position and wrenching.
 * 
 * @author Calclavia */
public abstract class BlockRotatable extends BlockTile implements IRotatableBlock
{
    protected byte rotationMask = Byte.parseByte("111100", 2);
    protected boolean isFlipPlacement = false;

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
        if (MathHelper.abs((float) entityLiving.posX - x) < 2.0F && MathHelper.abs((float) entityLiving.posZ - z) < 2.0F)
        {
            double d0 = entityLiving.posY + 1.82D - entityLiving.yOffset;

            if (canRotate(1) && d0 - y > 2.0D)
            {
                return 1;
            }

            if (canRotate(0) && y - d0 > 0.0D)
            {
                return 0;
            }
        }

        int playerSide = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int returnSide = (playerSide == 0 && canRotate(2)) ? 2 : ((playerSide == 1 && canRotate(5)) ? 5 : ((playerSide == 2 && canRotate(3)) ? 3 : ((playerSide == 3 && canRotate(4)) ? 4 : 0)));

        if (isFlipPlacement)
            return ForgeDirection.getOrientation(returnSide).getOpposite().ordinal();

        return returnSide;
    }

    public boolean canRotate(int ord)
    {
        return (rotationMask & (1 << ord)) != 0;
    }

    @Override
    public boolean onSneakUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        return doRotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
    }

    /** Can be overriden for blocks that required extra functionalities. */
    @Override
    public boolean onUseWrench(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        return onSneakUseWrench(world, x, y, z, par5EntityPlayer, side, hitX, hitY, hitZ);
    }

    public boolean doRotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        int currentRotMeta = worldObj.getBlockMetadata(x, y, z);
        ForgeDirection orientation = ForgeDirection.getOrientation(currentRotMeta);
        ForgeDirection rotated = orientation.getRotation(axis);
        int rmeta = rotated.ordinal();
        int rmetaBit = 1 << rmeta;

        if ((rmetaBit & rotationMask) == rmetaBit && canRotate(rmeta))
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