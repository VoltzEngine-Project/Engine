package resonant.content.factory.resources.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
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
public class FactoryDust extends FactoryResource
{
	public static Item dust;
	public static Item dirtDust;

	public FactoryDust(ResourceFactoryHandler gen, String modID, String prefix)
	{
		super(gen, modID, prefix);
	}

	public Object generate(String objectName, Object... data)
	{
		if (dust == null)
		{
			dust = new ItemResourceDust(false);
			dust.setCreativeTab(CreativeTabs.tabMaterials);
			GameRegistry.registerItem(dust, "metalDust", modID);
		}
		if (dirtDust == null)
		{
			dirtDust = new ItemResourceDust(true);
			dirtDust.setCreativeTab(CreativeTabs.tabMaterials);
			GameRegistry.registerItem(dirtDust, "metalDirtyDust", modID);
		}
		ItemStack dustStack = null;
		ItemStack dirtStack = null;
		if (gen.materialIds.inverse().containsKey(objectName))
		{
			dustStack = new ItemStack(dust, 1, gen.materialIds.inverse().get(objectName));
			dirtStack = new ItemStack(dirtDust, 1, gen.materialIds.inverse().get(objectName));
			OreDictionary.registerOre("dirtyDust" + LanguageUtility.capitalizeFirst(objectName), dirtStack);
			OreDictionary.registerOre("dust" + LanguageUtility.capitalizeFirst(objectName), dirtStack);
			OreDictionary.registerOre("dust" + LanguageUtility.capitalizeFirst(objectName), dustStack);
			MachineRecipes.INSTANCE.addRecipe(RecipeType.GRINDER.name(), "rubble" + LanguageUtility.capitalizeFirst(objectName), dirtDust, dirtDust);
			MachineRecipes.INSTANCE.addRecipe(RecipeType.MIXER.name(), "dirtyDust" + LanguageUtility.capitalizeFirst(objectName), dust, dust);
			List<ItemStack> ingots = OreDictionary.getOres("ingot" + LanguageUtility.capitalizeFirst(objectName));
			if (ingots != null && !ingots.isEmpty())
			{
				ItemStack ingot = ingots.get(0).copy();
				ingot.stackSize = 1;
				GameRegistry.addSmelting(dirtStack, ingot, 0.7f);
				ingot.stackSize = 2;
				GameRegistry.addSmelting(dustStack, ingot, 0.7f);
			}
			Fluid fluid = FluidRegistry.getFluid("molten" + "_" + objectName);
			if (fluid != null)
			{

			}
		}
		return dustStack;
	}
}
