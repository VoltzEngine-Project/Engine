package universalelectricity.extend;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.server.BlockContainer;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import basiccomponents.BasicComponents;


/**
 * The Class BlockMachine.
 */
public abstract class BlockMachine extends BlockContainer
{
    
    /**
     * Instantiates a new block machine.
     *
     * @param s the s
     * @param i the i
     * @param material the material
     */
    public BlockMachine(String s, int i, Material material)
    {
        super(i, material);
        a(s);
        c(0.5F);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     *
     * @param i the i
     * @return the drop data
     */
    protected int getDropData(int i)
    {
        return i;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     *
     * @param random the random
     * @return the int
     */
    public int a(Random random)
    {
        return 1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     *
     * @param i the i
     * @param random the random
     * @param j the j
     * @return the drop type
     */
    public int getDropType(int i, Random random, int j)
    {
        return id;
    }

    /**
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     * @param entityhuman the entityhuman
     * @return true, if successful
     */
    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        if (entityhuman.inventory.getItemInHand() != null)
        {
            if (entityhuman.inventory.getItemInHand().id == BasicComponents.ItemWrench.id)
            {
                if (onUseWrench(world, i, j, k, entityhuman))
                {
                    world.applyPhysics(i, j, k, id);
                    return true;
                }
            }
            else if ((entityhuman.inventory.getItemInHand().getItem() instanceof ItemElectric) && onUseElectricItem(world, i, j, k, entityhuman))
            {
                return true;
            }
        }

        if (entityhuman.isSneaking())
        {
            return false;
        }
        else
        {
            return machineActivated(world, i, j, k, entityhuman);
        }
    }

    /**
     * Machine activated.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     * @param entityhuman the entityhuman
     * @return true, if successful
     */
    public boolean machineActivated(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        return false;
    }

    /**
     * On use electric item.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     * @param entityhuman the entityhuman
     * @return true, if successful
     */
    public boolean onUseElectricItem(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        return false;
    }

    /**
     * On use wrench.
     *
     * @param world the world
     * @param i the i
     * @param j the j
     * @param k the k
     * @param entityhuman the entityhuman
     * @return true, if successful
     */
    public boolean onUseWrench(World world, int i, int j, int k, EntityHuman entityhuman)
    {
        return false;
    }

    /**
     * Returns the TileEntity used by this block.
     *
     * @return the tile entity
     */
    public TileEntity a_()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Block#addCreativeItems(java.util.ArrayList)
     */
    public void addCreativeItems(ArrayList arraylist)
    {
        arraylist.add(new ItemStack(this, 1, 0));
    }
}
