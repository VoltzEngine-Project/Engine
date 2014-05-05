package calclavia.lib.thermal;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.IVectorWorld;
import calclavia.lib.type.Pair;

/** Dictionary of heat values related to blocks
 * 
 * @author Darkguardsman */
public class HeatingDictionary
{
    private static HashMap<Block, Float> blockToHeatMap = new HashMap<Block, Float>();
    private static HashMap<Integer, Float> idToHeatMap = new HashMap<Integer, Float>();
    private static HashMap<Pair<Integer, Integer>, Float> idMetaToHeatMap = new HashMap<Pair<Integer, Integer>, Float>();
    private static HashMap<Material, Float> materialToHeatMap = new HashMap<Material, Float>();

    /** Registers a block with a specific heating value */
    public static void register(Block block, float f)
    {
        if (block != null && f > 0)
        {
            blockToHeatMap.put(block, f);
        }
    }

    /** Registers a block with a specific heating value */
    public static void register(int id, float f)
    {
        if (Block.blocksList[id] != null && f > 0)
        {
            idToHeatMap.put(id, f);
        }
    }

    /** Registers a block with a specific heating value */
    public static void register(int id, int meta, float f)
    {
        if (Block.blocksList[id] != null && f > 0)
        {
            idMetaToHeatMap.put(new Pair<Integer, Integer>(id, meta), f);
        }
    }

    /** Registers a material with a specific heating value */
    public static void register(Material m, float f)
    {
        if (m != null && f > 0)
        {
            materialToHeatMap.put(m, f);
        }
    }

    /** Graps the specific heating point of a block at the location */
    public static float getSpecificHeat(IVectorWorld vec)
    {
        return getSpecificHeat(vec.world(), (int) vec.x(), (int) vec.y(), (int) vec.z());
    }

    /** Graps the specific heating point of a block at the location */
    public static float getSpecificHeat(World world, IVector3 vec)
    {
        return getSpecificHeat(world, (int) vec.x(), (int) vec.y(), (int) vec.z());
    }

    /** Graps the specific heating point of a block at the location */
    public static float getSpecificHeat(World world, int x, int y, int z)
    {
        Block block = Block.blocksList[world.getBlockId(x, y, z)];
        int meta = world.getBlockMetadata(x, y, z);
        if (block != null)
        {
            if (blockToHeatMap.containsKey(block))
            {
                return blockToHeatMap.get(block);
            }
            else if (idToHeatMap.containsKey(block.blockID))
            {
                return idToHeatMap.get(block.blockID);
            }
            else if (idMetaToHeatMap.containsKey(new Pair<Integer, Integer>(block.blockID, meta)))
            {
                return idMetaToHeatMap.get(new Pair<Integer, Integer>(block.blockID, meta));
            }
            else if (materialToHeatMap.containsKey(block.blockMaterial))
            {
                return materialToHeatMap.get(block.blockMaterial);
            }
        }
        return 5;
    }
}
