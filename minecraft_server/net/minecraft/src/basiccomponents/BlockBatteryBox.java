package net.minecraft.src.basiccomponents;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.universalelectricity.Vector3;
import net.minecraft.src.universalelectricity.extend.BlockMachine;
import net.minecraft.src.universalelectricity.extend.IRedstoneProvider;

public class BlockBatteryBox extends BlockMachine
{
    public BlockBatteryBox(int id, int textureIndex)
    {
        super("Battery Box", id, Material.wood);
        this.blockIndexInTexture = textureIndex;
        this.setStepSound(soundMetalFootstep);
        this.setRequiresSelfNotify();
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.blockTextureFile;
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata)
    {
        if (side == 0 || side == 1)
        {
            return this.blockIndexInTexture;
        }
        else
        {
            //If it is the front side
            if (side == metadata)
            {
                return this.blockIndexInTexture + 3;
            }
            //If it is the back side
            else if (side == Vector3.getOrientationFromSide((byte)metadata, (byte)2))
            {
                return this.blockIndexInTexture + 2;
            }

            return this.blockIndexInTexture + 4;
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLiving par5EntityLiving)
    {
        int angle = MathHelper.floor_double((par5EntityLiving.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int change = 3;

        switch (angle)
        {
            case 0:
                change = 5;
                break;

            case 1:
                change = 3;
                break;

            case 2:
                change = 4;
                break;

            case 3:
                change = 2;
                break;
        }

        par1World.setBlockMetadataWithNotify(x, y, z, change);
    }

    @Override
    public boolean onUseWrench(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
        //Reorient the block
        switch (par1World.getBlockMetadata(x, y, z))
        {
            case 2:
                par1World.setBlockMetadataWithNotify(x, y, z, 5);
                break;

            case 5:
                par1World.setBlockMetadataWithNotify(x, y, z, 3);
                break;

            case 3:
                par1World.setBlockMetadataWithNotify(x, y, z, 4);
                break;

            case 4:
                par1World.setBlockMetadataWithNotify(x, y, z, 2);
                break;
        }

        return true;
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean onMachineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);

        if (!par1World.isRemote)
        {
            par5EntityPlayer.openGui(BasicComponents.getInstance(), 0, par1World, x, y, z);
            return true;
        }

        return true;
    }

    /**
     * Is this block powering the block on the specified side
     */
    @Override
    public boolean isPoweringTo(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
        IRedstoneProvider tileEntity = (IRedstoneProvider)par1IBlockAccess.getBlockTileEntity(x, y, z);
        return tileEntity.isPoweringTo((byte)side);
    }

    /**
     * Is this block indirectly powering the block on the specified side
     */
    @Override
    public boolean isIndirectlyPoweringTo(World par1World, int x, int y, int z, int side)
    {
        IRedstoneProvider tileEntity = (IRedstoneProvider)par1World.getBlockTileEntity(x, y, z);
        return tileEntity.isIndirectlyPoweringTo((byte)side);
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
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileEntityBatteryBox();
	}

}
