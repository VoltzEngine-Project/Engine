package universalelectricity.basiccomponents;

import java.util.Random;

import universalelectricity.Vector3;
import universalelectricity.extend.BlockMachine;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockCoalGenerator extends BlockMachine
{
    public BlockCoalGenerator(int id, int textureIndex)
    {
        super("Coal Generator", id, Material.wood);
        this.blockIndexInTexture = textureIndex;
        this.setStepSound(soundMetalFootstep);
        this.setRequiresSelfNotify();
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.blockTextureFile;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    public void randomDisplayTick(World par1World, int x, int y, int z, Random par5Random)
    {
        TileEntityCoalGenerator tileEntity = (TileEntityCoalGenerator)par1World.getBlockTileEntity(x, y, z);

        if (tileEntity.generateRate > 0)
        {
            int var6 = par1World.getBlockMetadata(x, y, z);
            float var7 = (float)x + 0.5F;
            float var8 = (float)y + 0.0F + par5Random.nextFloat() * 6.0F / 16.0F;
            float var9 = (float)z + 0.5F;
            float var10 = 0.52F;
            float var11 = par5Random.nextFloat() * 0.6F - 0.3F;

            if (var6 == 4)
            {
                par1World.spawnParticle("smoke", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 5)
            {
                par1World.spawnParticle("smoke", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
                par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
                par1World.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
                par1World.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
            }
        }
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
                return this.blockIndexInTexture + 5;
            }
            //If it is the back side
            else if (side == Vector3.getOrientationFromSide((byte)metadata, (byte)2))
            {
                return this.blockIndexInTexture + 3;
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
                return this.blockIndexInTexture + 5;
            }
            //If it is the back side
            else if (side == 2)
            {
                return this.blockIndexInTexture + 3;
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
            par5EntityPlayer.openGui(BasicComponents.instance, 1, par1World, x, y, z);
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
        return new TileEntityCoalGenerator();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
