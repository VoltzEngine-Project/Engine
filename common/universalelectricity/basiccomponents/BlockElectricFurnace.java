package universalelectricity.basiccomponents;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.extend.BlockMachine;

public class BlockElectricFurnace extends BlockMachine
{
    public BlockElectricFurnace(int id, int textureIndex)
    {
        super("Electric Furnace", id, Material.wood);
        this.blockIndexInTexture = textureIndex;
        this.setStepSound(soundMetalFootstep);
        this.setRequiresSelfNotify();
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.BLOCK_TEXTURE_FILE;
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int x, int y, int z, int side)
    {
        int metadata = par1IBlockAccess.getBlockMetadata(x, y, z);

        if (side == 0 || side == 1)
        {
            return this.blockIndexInTexture;
        }
        else
        {
            //If it is the front side
            if (side == metadata)
            {
                return this.blockIndexInTexture + 6;
            }
            //If it is the back side
            else if (side == ForgeDirection.getOrientation(metadata).getOpposite().ordinal())
            {
                return this.blockIndexInTexture + 2;
            }

            return this.blockIndexInTexture + 1;
        }
    }

    @Override
    public int getBlockTextureFromSide(int side)
    {
        if (side == 0 || side == 1)
        {
            return this.blockIndexInTexture;
        }
        else
        {
            //If it is the front side
            if (side == 3)
            {
                return this.blockIndexInTexture + 6;
            }
            //If it is the back side
            else if (side == 2)
            {
                return this.blockIndexInTexture + 2;
            }

            return this.blockIndexInTexture + 1;
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
                change = 2;
                break;

            case 1:
                change = 5;
                break;

            case 2:
                change = 3;
                break;

            case 3:
                change = 4;
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
            par5EntityPlayer.openGui(BasicComponents.instance, 2, par1World, x, y, z);
            return true;
        }

        return true;
    }

    /**
     * Returns the TileEntity used by this block.
     */
    @Override
    public TileEntity createNewTileEntity(World var1)
    {
        return new TileEntityElectricFurnace();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
