package universalelectricity;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class is used for storing ore generation data. If you are too
 * lazy to generate your own ores, you can do {@link #OreGenerator.ORES_TO_GENERATE.add()}
 * to add your ore to the list of ores to generate.
 * @author Calclavia
 *
 */
public class OreGenData
{
    public String name;

    public String oreDictionaryName;

    public int minGenerateLevel;

    public int maxGenerateLevel;

    public int amountPerChunk;

    public int amountPerBranch;

    public boolean shouldGenerate;

    public int blockIndexTexture;
    
    public ItemStack oreStack;
    
    public boolean generateSurface = true;
    
    public boolean generateNether = false;
    
    public boolean generateEnd = false;

    /**
     * What harvest level does this machine need to be acquired?
     */
    public int harvestLevel;

    /**
     * The predefined tool classes are "pickaxe", "shovel", "axe".  You can add others for custom tools.
     */
    public String harvestTool;

    /**
     * @param name - The name of the ore for display
     * @param textureFile - The 16x16 png texture of your ore to override
     * @param minGenerateLevel - The highest generation level of your ore
     * @param maxGenerateLevel - The lowest generation level of your ore
     * @param amountPerChunk - The amount of ores to generate per chunk
     * @param amountPerBranch - The amount of ores to generate in a clutter. E.g coal generates with a lot of other coal next to it. How much do you want?
     */
    public OreGenData(String name, String oreDiectionaryName, ItemStack stack, int minGenerateLevel, int maxGenerateLevel, int amountPerChunk, int amountPerBranch, String harvestTool, int harvestLevel)
    {
        this.name = name;
        this.minGenerateLevel = minGenerateLevel;
        this.maxGenerateLevel = maxGenerateLevel;
        this.amountPerChunk = amountPerChunk;
        this.amountPerBranch = amountPerBranch;
        this.shouldGenerate = shouldGenerateOre(name);
        this.harvestTool = harvestTool;
        this.harvestLevel = harvestLevel;
        this.oreDictionaryName = oreDiectionaryName;
        this.oreStack = stack;
        
		OreDictionary.registerOre(oreDictionaryName, stack);
		MinecraftForge.setBlockHarvestLevel(Block.blocksList[stack.itemID], harvestTool, harvestLevel);
    }

    //A simplified version of the constructor
    public OreGenData(String name, String oreDiectionaryName, ItemStack stack, int maxGenerateLevel, int amountPerChunk, int amountPerBranch)
    {
        this(name, oreDiectionaryName, stack, 0, maxGenerateLevel, amountPerChunk, amountPerBranch, "pickaxe", 1);
    }

    //You may inherit from this class and change this function if you want a custom texture render for your ore.
    public int getBlockTextureFromSide(int side)
    {
        return this.blockIndexTexture;
    }

    //Checks the config file and see if Universal Electricity should generate this ore
    private static boolean shouldGenerateOre(String oreName)
    {
        UniversalElectricity.CONFIGURATION.load();
        boolean shouldGenerate = Boolean.parseBoolean(UniversalElectricity.CONFIGURATION.getOrCreateBooleanProperty("Generate_" + oreName.replace(" ", "_"), Configuration.CATEGORY_GENERAL, true).value);
        UniversalElectricity.CONFIGURATION.save();
        return shouldGenerate;
    }
}
