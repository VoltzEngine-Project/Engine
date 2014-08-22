package resonant.content.factory.resources.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import resonant.api.recipe.MachineRecipes;
import resonant.content.factory.resources.FactoryResource;
import resonant.content.factory.resources.RecipeType;
import resonant.content.factory.resources.ResourceFactoryHandler;
import resonant.content.factory.resources.block.BlockFluidMaterial;
import resonant.lib.prefab.block.FluidColored;
import resonant.lib.utility.LanguageUtility;

/**
 * Created by robert on 8/22/2014.
 */
public class FactoryFluidMolten extends FactoryResource
{
    public FactoryFluidMolten(ResourceFactoryHandler gen, String modID, String prefix)
    {
        super(gen, modID, prefix);
    }

    @Override
    public BlockFluidMaterial generate(String materialName, Object... data)
    {
        String moltenName = "molten" + "_" + materialName;

        Fluid fluidMolten = new FluidColored(moltenName).setDensity(7).setViscosity(5000).setTemperature(273 + 1538);
        if(FluidRegistry.registerFluid(fluidMolten))
        {
            fluidMolten = FluidRegistry.getFluid(moltenName);
        }

        BlockFluidMaterial blockFluidMaterial = new BlockFluidMaterial(fluidMolten);
        blockFluidMaterial.setBlockName(prefix +  "molten" + LanguageUtility.capitalizeFirst(materialName));
        GameRegistry.registerBlock(blockFluidMaterial, "molten" + LanguageUtility.capitalizeFirst(materialName));

        //TODO implement item melting for blocks that contain metals
        MachineRecipes.INSTANCE.addRecipe(RecipeType.SMELTER.name(), new FluidStack(fluidMolten, FluidContainerRegistry.BUCKET_VOLUME), "ingot" + LanguageUtility.capitalizeFirst(materialName));
        MachineRecipes.INSTANCE.addRecipe(RecipeType.SMELTER.name(), new FluidStack(fluidMolten, FluidContainerRegistry.BUCKET_VOLUME), "dust" + LanguageUtility.capitalizeFirst(materialName));
        return blockFluidMaterial;
    }
}
