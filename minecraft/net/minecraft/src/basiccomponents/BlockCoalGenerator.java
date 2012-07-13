package net.minecraft.src.basiccomponents;

import java.util.Random;

import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.UniversalElectricity;
import net.minecraft.src.universalelectricity.extend.BlockMachine;

public class BlockCoalGenerator extends BlockMachine implements ITextureProvider
{
    public BlockCoalGenerator(int id, int textureIndex)
    {
        super("Coal Generator", id, Material.wood);
        this.blockIndexInTexture = textureIndex;
        this.setStepSound(soundMetalFootstep);
        this.setRequiresSelfNotify();
        this.setTickRandomly(true);
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
            else if (side == UniversalElectricity.getOrientationFromSide((byte)metadata, (byte)2))
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
    public boolean machineActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
        int metadata = par1World.getBlockMetadata(x, y, z);

        if (!par1World.isRemote)
        {
            par5EntityPlayer.openGui(BasicComponents.getInstance(), 1, par1World, x, y, z);
            return true;
        }

        return true;
    }

    /**
     * Called whenever the block is removed.
     */
    @Override
    public void onBlockRemoval(World par1World, int par2, int par3, int par4)
    {
        IInventory tileEntity = (IInventory)par1World.getBlockTileEntity(par2, par3, par4);

        if (tileEntity != null)
        {
            for (int var6 = 0; var6 < tileEntity.getSizeInventory(); ++var6)
            {
                ItemStack var7 = tileEntity.getStackInSlot(var6);

                if (var7 != null)
                {
                    Random random = new Random();
                    float var8 = random.nextFloat() * 0.8F + 0.1F;
                    float var9 = random.nextFloat() * 0.8F + 0.1F;
                    float var10 = random.nextFloat() * 0.8F + 0.1F;

                    while (var7.stackSize > 0)
                    {
                        int var11 = random.nextInt(21) + 10;

                        if (var11 > var7.stackSize)
                        {
                            var11 = var7.stackSize;
                        }

                        var7.stackSize -= var11;
                        EntityItem var12 = new EntityItem(par1World, (par2 + var8), (par3 + var9), (par4 + var10), new ItemStack(var7.itemID, var11, var7.getItemDamage()));

                        if (var7.hasTagCompound())
                        {
                            var12.item.setTagCompound((NBTTagCompound)var7.getTagCompound().copy());
                        }

                        float var13 = 0.05F;
                        var12.motionX = ((float)random.nextGaussian() * var13);
                        var12.motionY = ((float)random.nextGaussian() * var13 + 0.2F);
                        var12.motionZ = ((float)random.nextGaussian() * var13);
                        par1World.spawnEntityInWorld(var12);
                    }
                }
            }
        }

        super.onBlockRemoval(par1World, par2, par3, par4);
    }

    /**
     * Returns the TileEntity used by this block.
     */
    @Override
    public TileEntity getBlockEntity()
    {
        return new TileEntityCoalGenerator();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
