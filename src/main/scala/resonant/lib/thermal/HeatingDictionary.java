package resonant.lib.thermal;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import resonant.lib.type.Pair;
import universalelectricity.api.vector.IVector3;
import universalelectricity.api.vector.IVectorWorld;

/** Dictionary of heat values related to blocks
 * http://www.engineeringtoolbox.com/specific-heat-metals-d_152.html
 * http://www.engineeringtoolbox.com/specific-heat-solids-d_154.html
 * 
 * @author Darkguardsman */
public class HeatingDictionary
{
    private static HashMap<Block, Float> blockToHeatMap = new HashMap<Block, Float>();
    private static HashMap<Integer, Float> idToHeatMap = new HashMap<Integer, Float>();
    private static HashMap<Pair<Integer, Integer>, Float> idMetaToHeatMap = new HashMap<Pair<Integer, Integer>, Float>();
    private static HashMap<Material, Float> materialToHeatMap = new HashMap<Material, Float>();

    static
    {
        register(Material.iron, 0.45f);
        register(Material.air, 1f);
        register(Material.grass, 0.84f);
        register(Material.ground, 0.9f);
        register(Material.wood, 0.84f);
        register(Material.rock, 0.8f);
        register(Material.anvil, 0.5f);
        register(Material.water, 1f);
        register(Material.lava, 0.84f);
        register(Material.leaves, 0.84f);
        register(Material.plants, 0.84f);
        register(Material.vine, 0.84f);
        register(Material.sponge, 0.84f);
        register(Material.cloth, 2f);
        register(Material.fire, 1f);
        register(Material.sand, 1f);
        register(Material.circuits, 1f);
        register(Material.materialCarpet, 2f);
        register(Material.glass, 0.84f);
        register(Material.redstoneLight, 0.9f);
        register(Material.tnt, 2f);
        register(Material.coral, 0.84f);
        register(Material.ice, 1f);
        register(Material.snow, 1f);
        register(Material.craftedSnow, 1f);
        register(Material.cactus, 0.84f);
        register(Material.clay, 0.92f);
        register(Material.pumpkin, 0.84f);
        register(Material.dragonEgg, 0.84f);
        register(Material.portal, 1f);
        register(Material.cake, 2f);
        register(Material.web, 0.84f);
        register(Material.piston, 0.9f);
    }

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
