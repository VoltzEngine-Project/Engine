package net.minecraft.src.basiccomponents;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.forge.ITextureProvider;
import net.minecraft.src.universalelectricity.Vector3;
import net.minecraft.src.universalelectricity.extend.BlockConductor;

public class BlockCopperWire extends BlockConductor implements ITextureProvider
{
    public BlockCopperWire(int id)
    {
        super(id, Material.cloth);
        this.setBlockName("Copper Wire");
        this.setStepSound(soundClothFootstep);
        this.setResistance(0.2F);
        this.setBlockBounds(0.30F, 0.30F, 0.30F, 0.70F, 0.70F, 0.70F);
        this.setRequiresSelfNotify();
        this.blockIndexInTexture = 7;
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);
        this.checkHostileBlock(world, x, y, z);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    @Override
    public void onNeighborBlockChange(World par1World, int x, int y, int z, int neighborID)
    {
        super.onNeighborBlockChange(par1World, x, y, z, neighborID);
        this.checkHostileBlock(par1World, x, y, z);
    }

    /**
     * Checks if there are any nearby blocks that are hostile to the copper wire
     * @return True if so
     */
    public void checkHostileBlock(World par1World, int x, int y, int z)
    {
        Vector3 originalPosition = new Vector3(x, y, z);

        for (byte i = 0; i < 6; i++)
        {
            switch (i)
            {
                case 0:
                    y -= 1;
                    break;

                case 1:
                    y += 1;
                    break;

                case 2:
                    z += 1;
                    break;

                case 3:
                    z -= 1;
                    break;

                case 4:
                    x += 1;
                    break;

                case 5:
                    x -= 1;
                    break;
            }

            int BlockID = par1World.getBlockId(x, y, z);

            if (BlockID == Block.fire.blockID || BlockID == Block.lavaMoving.blockID || BlockID == Block.lavaStill.blockID || BlockID == Block.waterMoving.blockID || BlockID == Block.waterStill.blockID)
            {
                par1World.setBlockWithNotify(originalPosition.intX(), originalPosition.intY(), originalPosition.intZ(), Block.fire.blockID);
                par1World.spawnParticle("largesmoke", originalPosition.intX(), originalPosition.intY(), originalPosition.intZ(), 0, 0, 0);
            }
        }
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
    */
    @Override
    public int getRenderType()
    {
        return -1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    @Override
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return BasicComponents.ItemCopperWire.shiftedIndex;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    @Override
    public TileEntity getBlockEntity()
    {
        return new TileEntityCopperWire();
    }

    @Override
    public String getTextureFile()
    {
        return BasicComponents.blockTextureFile;
    }
}