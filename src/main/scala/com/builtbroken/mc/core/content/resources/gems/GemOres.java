package com.builtbroken.mc.core.content.resources.gems;

import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.world.generator.OreGenReplace;
import com.builtbroken.mc.lib.world.generator.OreGeneratorSettings;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A list of gem ores that can be requested in Voltz Engine.
 *
 * @author - Kolatra
 */
@Deprecated //TODO move to JSON
public enum GemOres
{
    Amazonite(Gems.AMAZONITE, 5, 20, 2, 15),
    SmokeyQuartz(Gems.QUARTZ_SMOKEY, 5, 20, 2, 15),
    OnyxBlack(Gems.ONYX_BLACK, 1, 5, 3, 10),
    OnyxRed(Gems.ONYX_RED, 15, 60, 5, 20),
    GarnetGreen(Gems.GARNET_GREEN, 15, 60, 5, 20),
    GarnetOrange(Gems.GARNET_ORANGE, 10, 50, 3, 18),
    GarnetRed(Gems.GARNET_RED, 10, 50, 3, 18),
    GarnetYellow(Gems.GARNET_YELLOW, 10, 50, 3, 18);

    public final OreGeneratorSettings oreGenSettings;

    private Block block;
    private String oreDictName;

    public final Gems gem;

    GemOres(Gems gem, int min, int max, int amountPerBranch, int amountPerChunk)
    {
        this.gem = gem;
        oreGenSettings = new OreGeneratorSettings(min, max, amountPerBranch, amountPerChunk);
    }

    public ItemStack stack()
    {
        return stack(1);
    }

    public ItemStack stack(int stackSize)
    {
        return new ItemStack(block, stackSize, ordinal());
    }

    public static void registerSet(Block block, Configuration config)
    {
        if (block != null)
        {
            //TODO implement defined ore gen, for example have gems spawn only near lava, only in sand, only in dirt, etc
            for (GemOres ore : GemOres.values())
            {
                if (config.getBoolean("" + LanguageUtility.capitalizeFirst(ore.name()) + "_Ore", "WorldGen", true, "Enables generation of the ore in the world"))
                {
                    ore.block = block;
                    ore.oreDictName = "ore" + LanguageUtility.capitalizeFirst(ore.name().toLowerCase());
                    ItemStack stack = ore.stack();
                    GameRegistry.registerWorldGenerator(new OreGenReplace(block, ore.ordinal(), ore.oreGenSettings, "pickaxe", 1), 1);
                    OreDictionary.registerOre(ore.oreDictName, stack);
                }
            }
        }
    }

    public Item getOreItem()
    {
        return GemTypes.UNCUT.item;
    }
}
