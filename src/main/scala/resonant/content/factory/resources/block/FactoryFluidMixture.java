package resonant.content.factory.resources.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import resonant.content.factory.resources.FactoryResource;
import resonant.content.factory.resources.ResourceFactoryHandler;
import resonant.content.factory.resources.block.BlockFluidMaterial;
import resonant.content.factory.resources.block.BlockFluidMixture;
import resonant.lib.prefab.block.FluidColored;
import resonant.lib.utility.LanguageUtility;

/**
 * Created by robert on 8/22/2014.
 */
public class FactoryFluidMixture extends FactoryResource {
    public FactoryFluidMixture(ResourceFactoryHandler gen, String modID, String prefix) {
        super(gen, modID, prefix);
    }

    public BlockFluidMixture generate(String materialName, Object... data) {
        String mixtureName = "molten" + "_" + materialName;

        Fluid fluidMixture = new FluidColored(mixtureName).setDensity(3);
        if (FluidRegistry.registerFluid(fluidMixture)) {
            fluidMixture = FluidRegistry.getFluid(mixtureName);
        }

        BlockFluidMixture  blockFluidMixture = new BlockFluidMixture(fluidMixture);
        blockFluidMixture.setBlockName(prefix +  "mixture" + LanguageUtility.capitalizeFirst(materialName));
        GameRegistry.registerBlock(blockFluidMixture, "mixture" + LanguageUtility.capitalizeFirst(materialName));

        return blockFluidMixture;
    }
}
