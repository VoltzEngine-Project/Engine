package resonant.content.factory.resources.block;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.*;
import resonant.api.recipe.MachineRecipes;
import resonant.content.factory.resources.FactoryResource;
import resonant.content.factory.resources.RecipeType;
import resonant.content.factory.resources.ResourceFactoryHandler;
import resonant.lib.prefab.block.FluidColored;
import resonant.lib.render.RenderUtility;
import resonant.lib.utility.LanguageUtility;

import java.util.HashMap;

/**
 * Created by robert on 8/22/2014.
 */
public class FactoryFluidMolten extends FactoryResource
{
	public final HashMap<Integer, BlockFluidFinite> blockMoltenFluids = new HashMap<Integer, BlockFluidFinite>();

	public FactoryFluidMolten(ResourceFactoryHandler gen, String modID, String prefix)
	{
		super(gen, modID, prefix);
	}

	@Override
	public BlockFluidMaterial generate(String materialName, Object... data)
	{
		String moltenName = "molten" + "_" + materialName;

		Fluid fluidMolten = new FluidColored(moltenName).setDensity(7).setViscosity(5000).setTemperature(273 + 1538);
		if (FluidRegistry.registerFluid(fluidMolten))
		{
			fluidMolten = FluidRegistry.getFluid(moltenName);
		}

		BlockFluidMaterial blockFluidMaterial = new BlockFluidMaterial(fluidMolten);
		blockFluidMaterial.setBlockName(prefix + "molten" + LanguageUtility.capitalizeFirst(materialName));
		GameRegistry.registerBlock(blockFluidMaterial, "molten" + LanguageUtility.capitalizeFirst(materialName));

		//TODO implement item melting for blocks that contain metals
		MachineRecipes.INSTANCE.addRecipe(RecipeType.SMELTER.name(), new FluidStack(fluidMolten, FluidContainerRegistry.BUCKET_VOLUME), "ingot" + LanguageUtility.capitalizeFirst(materialName));
		MachineRecipes.INSTANCE.addRecipe(RecipeType.SMELTER.name(), new FluidStack(fluidMolten, FluidContainerRegistry.BUCKET_VOLUME), "dust" + LanguageUtility.capitalizeFirst(materialName));
		return blockFluidMaterial;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void postTextureHook(TextureStitchEvent.Post event)
	{
		for (BlockFluidFinite block : blockMoltenFluids.values())
		{
			block.getFluid().setIcons(RenderUtility.getIcon(prefix + "molten_flow"));
			((FluidColored) block.getFluid()).setColor(gen.getColor(gen.moltenToMaterial(block.getFluid().getName())));
		}
	}
}
