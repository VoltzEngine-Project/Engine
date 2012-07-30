package net.minecraft.src.universalelectricity.ore;

import net.minecraft.src.forge.Configuration;
import net.minecraft.src.universalelectricity.UniversalElectricity;

/**
 * This class is used for storing ore generation data from Universal Ores.
 * @author Calclavia
 *
 */
public class OreData
{
    public String name;

    public String oreDictionaryName;

    public int minGenerateLevel;

    public int maxGenerateLevel;

    public int amountPerChunk;

    public int amountPerBranch;

    public boolean shouldGenerate;

    public int blockIndexTexture;

    private boolean isAddToCreativeList;

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
    public OreData(String name, String oreDiectionaryName, int textureIndex, int minGenerateLevel, int maxGenerateLevel, int amountPerChunk, int amountPerBranch, boolean addToCreativeList, String harvestTool, int harvestLevel)
    {
        this.name = name;
        this.minGenerateLevel = minGenerateLevel;
        this.maxGenerateLevel = maxGenerateLevel;
        this.amountPerChunk = amountPerChunk;
        this.amountPerBranch = amountPerBranch;
        this.shouldGenerate = shouldGenerateOre(name);
        this.isAddToCreativeList = addToCreativeList;
        this.blockIndexTexture = textureIndex;
        this.harvestTool = harvestTool;
        this.harvestLevel = harvestLevel;
        this.oreDictionaryName = oreDiectionaryName;
    }

    //A simplified version of the constructor
    public OreData(String name, String oreDiectionaryName,  int textureIndex, int maxGenerateLevel, int amountPerChunk, int amountPerBranch)
    {
        this(name, oreDiectionaryName, textureIndex, 0, maxGenerateLevel, amountPerChunk, amountPerBranch, true, "pickaxe", 2);
    }

    //You may inherit from this class and change this function if you want a custom texture render for your ore.
    public int getBlockTextureFromSide(int side)
    {
        return this.blockIndexTexture;
    }

    //Checks the config file and see if Universal Electricity should generate this ore
    private static boolean shouldGenerateOre(String oreName)
    {
        UniversalElectricity.configuration.load();
        boolean shouldGenerate = Boolean.parseBoolean(UniversalElectricity.configuration.getOrCreateBooleanProperty("Generate " + oreName, Configuration.CATEGORY_GENERAL, true).value);
        UniversalElectricity.configuration.save();
        return shouldGenerate;
    }

    public boolean addToCreativeList()
    {
        return this.isAddToCreativeList;
    }
}
