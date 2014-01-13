package calclavia.lib.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

/**
 * @author Calclavia
 * 
 */
public interface ICustomBlockRender
{
	void renderInventory(Block block, int metadata, int modelID, RenderBlocks renderer);

	boolean renderStatic(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer);

}
