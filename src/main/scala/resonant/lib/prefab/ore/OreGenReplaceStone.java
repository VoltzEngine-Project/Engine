package resonant.lib.prefab.ore;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class OreGenReplaceStone extends OreGenReplace
{
	public OreGenReplaceStone(String name, ItemStack stack, int minGenerateLevel, int maxGenerateLevel, int amountPerChunk, int amountPerBranch, String harvestTool, int harvestLevel)
	{
		super(name, stack, Blocks.stone, minGenerateLevel, maxGenerateLevel, amountPerChunk, amountPerBranch, harvestTool, harvestLevel);
	}

	// A simplified version of the constructor
	public OreGenReplaceStone(String name, ItemStack stack, int maxGenerateLevel, int amountPerChunk, int amountPerBranch)
	{
		this(name, stack, 0, maxGenerateLevel, amountPerChunk, amountPerBranch, "pickaxe", 1);
	}
}
