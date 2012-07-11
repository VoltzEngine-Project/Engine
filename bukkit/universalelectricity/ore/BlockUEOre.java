package universalelectricity.ore;

import java.util.ArrayList;

import net.minecraft.server.Block;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Material;


/**
 * The Class BlockUEOre.
 */
public class BlockUEOre extends Block
{
    
    /** The ores. */
    public OreData ores[];

    /**
     * Instantiates a new block ue ore.
     *
     * @param i the i
     */
    public BlockUEOre(int i)
    {
        super(i, Material.STONE);
        ores = new OreData[16];
        a("Ore");
        c(3F);
        b(5F);
        a(h);
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
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     *
     * @param i the i
     * @param j the j
     * @return the int
     */
    public int a(int i, int j)
    {
        return ores[j].getBlockTextureFromSide(i);
    }

    /* (non-Javadoc)
     * @see net.minecraft.server.Block#addCreativeItems(java.util.ArrayList)
     */
    public void addCreativeItems(ArrayList arraylist)
    {
        for (int i = 0; i < ores.length; i++)
        {
            if (ores[i] != null && ores[i].addToCreativeList())
            {
                arraylist.add(new ItemStack(this, 1, i));
            }
        }
    }
}
