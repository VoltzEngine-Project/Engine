package resonant.content.factory.resources.block;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import resonant.content.factory.resources.FactoryResource;
import resonant.content.factory.resources.ResourceFactoryHandler;
import resonant.lib.prefab.block.FluidColored;
import resonant.lib.render.RenderUtility;
import resonant.lib.utility.LanguageUtility;

import java.util.HashMap;

/**
 * Created by robert on 8/22/2014.
 */
public class FactoryFluidMixture extends FactoryResource
{
	public final HashMap<Integer, BlockFluidFinite> blockMixtureFluids = new HashMap<Integer, BlockFluidFinite>();

	public FactoryFluidMixture(ResourceFactoryHandler gen, String modID, String prefix)
	{
		super(gen, modID, prefix);
	}

	public BlockFluidMixture generate(String materialName, Object... data)
	{
		String mixtureName = "molten" + "_" + materialName;

		Fluid fluidMixture = new FluidColored(mixtureName).setDensity(3);
		if (FluidRegistry.registerFluid(fluidMixture))
		{
			fluidMixture = FluidRegistry.getFluid(mixtureName);
		}

		BlockFluidMixture blockFluidMixture = new BlockFluidMixture(fluidMixture);
		blockFluidMixture.setBlockName(prefix + "mixture" + LanguageUtility.capitalizeFirst(materialName));
		GameRegistry.registerBlock(blockFluidMixture, "mixture" + LanguageUtility.capitalizeFirst(materialName));

		return blockFluidMixture;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void postTextureHook(TextureStitchEvent.Post event)
	{
		for (BlockFluidFinite block : blockMixtureFluids.values())
		{
			block.getFluid().setIcons(RenderUtility.getIcon(prefix + "mixture_flow"));
			((FluidColored) block.getFluid()).setColor(gen.getColor(gen.mixtureToMaterial(block.getFluid().getName())));
		}
	}
}
