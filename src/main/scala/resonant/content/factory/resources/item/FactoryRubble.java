package resonant.content.factory.resources.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import resonant.api.recipe.MachineRecipes;
import resonant.content.factory.resources.FactoryResource;
import resonant.content.factory.resources.RecipeType;
import resonant.content.factory.resources.ResourceFactoryHandler;
import resonant.lib.utility.LanguageUtility;

import java.util.List;

/**
 * Created by robert on 8/22/2014.
 */
public class FactoryRubble extends FactoryResource
{
	public static Item rubble;

	public FactoryRubble(ResourceFactoryHandler gen, String modID, String prefix)
	{
		super(gen, modID, prefix);
	}

	public Object generate(String objectName, Object... data)
	{
		if (rubble == null)
		{
			rubble = new ItemResourceDust();
			rubble.setCreativeTab(CreativeTabs.tabMaterials);
			GameRegistry.registerItem(rubble, "oreRubble", modID);
		}

		ItemStack stack = null;
		if (gen.materialIds.inverse().containsKey(objectName))
		{
			stack = new ItemStack(rubble, 1, gen.materialIds.inverse().get(objectName));
			OreDictionary.registerOre("rubble" + LanguageUtility.capitalizeFirst(objectName), stack);
			MachineRecipes.INSTANCE.addRecipe(RecipeType.CRUSHER.name(), "ore" + LanguageUtility.capitalizeFirst(objectName), "rubble" + LanguageUtility.capitalizeFirst(objectName));
			List<ItemStack> ingots = OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(objectName));
			if (ingots != null && !ingots.isEmpty())
			{
				ItemStack ingot = ingots.get(0).copy();
				ingot.stackSize = 1;
				GameRegistry.addSmelting(rubble, ingot, 0.7f);
			}
		}
		return stack;
	}
}
