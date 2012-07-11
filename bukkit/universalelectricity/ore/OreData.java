package universalelectricity.ore;

import universalelectricity.UniversalElectricity;


/**
 * The Class OreData.
 */
public class OreData
{
    
    /** The name. */
    public String name;
    
    /** The ore diectionary name. */
    public String oreDiectionaryName;
    
    /** The min generate level. */
    public int minGenerateLevel;
    
    /** The max generate level. */
    public int maxGenerateLevel;
    
    /** The amount per chunk. */
    public int amountPerChunk;
    
    /** The amount per branch. */
    public int amountPerBranch;
    
    /** The should generate. */
    public boolean shouldGenerate;
    
    /** The block index texture. */
    public int blockIndexTexture;
    
    /** The is add to creative list. */
    private boolean isAddToCreativeList;
    
    /** The harvest level. */
    public int harvestLevel;
    
    /** The harvest tool. */
    public String harvestTool;

    /**
     * Instantiates a new ore data.
     *
     * @param s the s
     * @param s1 the s1
     * @param i the i
     * @param j the j
     * @param k the k
     * @param l the l
     * @param i1 the i1
     * @param flag the flag
     * @param s2 the s2
     * @param j1 the j1
     */
    public OreData(String s, String s1, int i, int j, int k, int l, int i1, boolean flag, String s2, int j1)
    {
        name = s;
        minGenerateLevel = j;
        maxGenerateLevel = k;
        amountPerChunk = l;
        amountPerBranch = i1;
        shouldGenerate = shouldGenerateOre(s);
        isAddToCreativeList = flag;
        blockIndexTexture = i;
        harvestTool = s2;
        harvestLevel = j1;
        oreDiectionaryName = s1;
    }

    /**
     * Instantiates a new ore data.
     *
     * @param s the s
     * @param s1 the s1
     * @param i the i
     * @param j the j
     * @param k the k
     * @param l the l
     */
    public OreData(String s, String s1, int i, int j, int k, int l)
    {
        this(s, s1, i, 0, j, k, l, true, "pickaxe", 2);
    }

    /**
     * Gets the block texture from side.
     *
     * @param i the i
     * @return the block texture from side
     */
    public int getBlockTextureFromSide(int i)
    {
        return blockIndexTexture;
    }

    /**
     * Should generate ore.
     *
     * @param s the s
     * @return true, if successful
     */
    private static boolean shouldGenerateOre(String s)
    {
        UniversalElectricity.configuration.load();
        boolean flag = Boolean.parseBoolean(UniversalElectricity.configuration.getOrCreateBooleanProperty((new StringBuilder()).append("Generate ").append(s).toString(), "general", true).value);
        UniversalElectricity.configuration.save();
        return flag;
    }

    /**
     * Adds the to creative list.
     *
     * @return true, if successful
     */
    public boolean addToCreativeList()
    {
        return isAddToCreativeList;
    }
}
