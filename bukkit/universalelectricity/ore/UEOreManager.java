package universalelectricity.ore;

import java.util.Random;

import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;
import universalelectricity.UniversalElectricity;
import forge.MinecraftForge;
import forge.oredict.OreDictionary;


/**
 * The Class UEOreManager.
 */
public class UEOreManager
{
    
    /** The Constant maxOreBlocks. */
    public static final int maxOreBlocks = 2;
    
    /** The ore block id. */
    public static int oreBlockID;
    
    /** The Constant BlockOre. */
    public static final BlockUEOre BlockOre[];

    /**
     * Instantiates a new uE ore manager.
     */
    public UEOreManager()
    {
    }

    /**
     * Adds the ore.
     *
     * @param i the i
     * @param oredata the oredata
     */
    public static void addOre(int i, OreData oredata)
    {
        if (i > 32)
        {
            throw new RuntimeException("Universal ore ID is too high!");
        }

        int j = (int)Math.floor(i / 16);
        int k = i - 16 * j;

        if (BlockOre[j].ores[k] != null)
        {
            throw new RuntimeException((new StringBuilder()).append("Universal Electricity ore ").append(i).append(" ID has been taken already!").toString());
        }
        else
        {
            BlockOre[j].ores[k] = oredata;
            ModLoader.addName(new ItemStack(BlockOre[j], 1, k), oredata.name);
            MinecraftForge.setBlockHarvestLevel(BlockOre[j], k, oredata.harvestTool, oredata.harvestLevel);
            OreDictionary.registerOre(oredata.oreDiectionaryName, new ItemStack(BlockOre[j], 1, k));
            return;
        }
    }

    /**
     * Gets the ore.
     *
     * @param i the i
     * @return the ore
     */
    public static BlockUEOre getOre(int i)
    {
        if (i <= 32)
        {
            int j = (int)Math.floor(i / 16);

            if (BlockOre[j] != null)
            {
                return BlockOre[j];
            }
        }

        return null;
    }

    /**
     * Gets the ore metadata.
     *
     * @param i the i
     * @return the ore metadata
     */
    public static int getOreMetadata(int i)
    {
        if (i <= 32)
        {
            int j = (int)Math.floor(i / 16);

            if (BlockOre[j] != null)
            {
                return i - 16 * j;
            }
        }

        return -1;
    }

    /**
     * Generate surface.
     *
     * @param world the world
     * @param random the random
     * @param i the i
     * @param j the j
     */
    public static void generateSurface(World world, Random random, int i, int j)
    {
        for (int k = 0; k < BlockOre.length; k++)
        {
            if (BlockOre[k] == null)
            {
                continue;
            }

            BlockUEOre blockueore = BlockOre[k];

            for (int l = 0; l < blockueore.ores.length; l++)
            {
                if (blockueore.ores[l] == null || !blockueore.ores[l].shouldGenerate)
                {
                    continue;
                }

                for (int i1 = 0; i1 < blockueore.ores[l].amountPerChunk; i1++)
                {
                    int j1 = i + random.nextInt(16);
                    int k1 = random.nextInt(blockueore.ores[l].maxGenerateLevel) + blockueore.ores[l].minGenerateLevel;
                    int l1 = j + random.nextInt(16);
                    (new WorldGenMinableMetadata(blockueore.id, blockueore.ores[l].amountPerBranch, l)).a(world, random, j1, k1, l1);
                }
            }
        }
    }

    static
    {
        oreBlockID = UniversalElectricity.getConfigID(UniversalElectricity.configuration, "Universal Ores", 3968, true);
        BlockOre = (new BlockUEOre[]
                {
                    new BlockUEOre(oreBlockID), new BlockUEOre(oreBlockID + 1)
                });
    }
}
