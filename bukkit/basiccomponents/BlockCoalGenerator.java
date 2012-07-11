package basiccomponents;

import java.util.Random;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityItem;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.IBlockAccess;
import net.minecraft.server.IInventory;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.MathHelper;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import universalelectricity.UniversalElectricity;
import universalelectricity.extend.BlockMachine;
import forge.ITextureProvider;


/**
 * The Class BlockCoalGenerator.
 */
public class BlockCoalGenerator extends BlockMachine implements ITextureProvider
{
    
    /**
     * Instantiates a new block coal generator.
     *
     * @param i the i
     * @param j the j
     */
    public BlockCoalGenerator(int i, int j)
    {
        super("Coal Generator", i, Material.WOOD);
        textureId = j;
        a(i);
        j();
        a(true);
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Block#getTextureFile()
     */
    public String getTextureFile()
    {
        return "/basiccomponents/textures/blocks.png";
    }

    /**
     * Gets the block texture.
     *
     * @param iblockaccess the iblockaccess
     * @param i the i
     * @param j the j
     * @param k the k
     * @param l the l
     * @return the block texture
     */
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        int i1 = iblockaccess.getData(i, j, k);

        if (l == 0 || l == 1)
        {
            return textureId;
        }

        if (l == i1)
        {
            return textureId + 5;
        }

        if (l == UniversalElectricity.getOrientationFromSide((byte)i1, (byte)2))
        {
            return textureId + 3;
        }
        else
        {
            return textureId + 1;
        }
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     *
     * @param i the i
     * @return the int
     */
    public int a(int i)
    {
        if (i == 0 || i == 1)
        {
            return textureId;
        }

        if (i == 3)
        {
            return textureId + 5;
        }

        if (i == 2)
        {
            return textureId + 3;
        }
        else
        {
            return textureId + 1;
        }
    }

    /**
     * Called when the block is placed in the world.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     * @param entityliving the entityliving
     */
    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving)
    {
        int l = MathHelper.floor((double)((entityliving.yaw * 4F) / 360F) + 0.5D) & 3;
        byte byte0 = 3;

        switch (l)
        {
            case 0:
                byte0 = 2;
                break;

            case 1:
                byte0 = 5;
                break;

            case 2:
                byte0 = 3;
                break;

            case 3:
                byte0 = 4;
                break;
        }

        world.setData(i, j, k, byte0);
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.BlockMachine#onUseWrench(net.minecraft.server.World, int, int, int, net.minecraft.server.EntityHuman)
     */
    public boolean onUseWrench(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        switch (world.getData(i, j, k))
        {
            case 2:
                world.setData(i, j, k, 5);
                break;

            case 5:
                world.setData(i, j, k, 3);
                break;

            case 3:
                world.setData(i, j, k, 4);
                break;

            case 4:
                world.setData(i, j, k, 2);
                break;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see universalelectricity.extend.BlockMachine#machineActivated(net.minecraft.server.World, int, int, int, net.minecraft.server.EntityHuman)
     */
    public boolean machineActivated(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        if (!world.isStatic)
        {
            entityhuman.openGui(BasicComponents.getInstance(), 1, world, i, j, k);
            return true;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called whenever the block is removed.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     */
    public void remove(World world, int i, int j, int k)
    {
        IInventory iinventory = (IInventory)world.getTileEntity(i, j, k);

        if (iinventory != null)
        {
            label0:

            for (int l = 0; l < iinventory.getSize(); l++)
            {
                ItemStack itemstack = iinventory.getItem(l);

                if (itemstack == null)
                {
                    continue;
                }

                Random random = new Random();
                float f = random.nextFloat() * 0.8F + 0.1F;
                float f1 = random.nextFloat() * 0.8F + 0.1F;
                float f2 = random.nextFloat() * 0.8F + 0.1F;

                do
                {
                    if (itemstack.count <= 0)
                    {
                        continue label0;
                    }

                    int i1 = random.nextInt(21) + 10;

                    if (i1 > itemstack.count)
                    {
                        i1 = itemstack.count;
                    }

                    itemstack.count -= i1;
                    EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.id, i1, itemstack.getData()));

                    if (itemstack.hasTag())
                    {
                        entityitem.itemStack.setTag((NBTTagCompound)itemstack.getTag().clone());
                    }

                    float f3 = 0.05F;
                    entityitem.motX = (float)random.nextGaussian() * f3;
                    entityitem.motY = (float)random.nextGaussian() * f3 + 0.2F;
                    entityitem.motZ = (float)random.nextGaussian() * f3;
                    world.addEntity(entityitem);
                }
                while (true);
            }
        }

        super.remove(world, i, j, k);
    }

    /**
     * Returns the TileEntity used by this block.
     *
     * @return the tile entity
     */
    public TileEntity a_()
    {
        return new TileEntityCoalGenerator();
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     *
     * @return true, if successful
     */
    public boolean a()
    {
        return false;
    }
}
