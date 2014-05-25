package resonant.lib.content.module;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import universalelectricity.core.transform.vector.Vector3;

@SideOnly(Side.CLIENT)
public class TileRender
{
	/**
	 * Render the static, unmoving faces of this part into the world renderer. The Tesselator is
	 * already drawing.
	 */
	public boolean renderStatic(RenderBlocks renderer, Vector3 position)
	{
		return false;
	}

	/**
	 * Render the dynamic, changing faces of this part and other gfx as in a TESR. The Tesselator
	 * will need to be started if it is to be used.
	 */
	public boolean renderDynamic(Vector3 position, boolean isItem, float frame)
	{
		return false;
	}

	/**
	 * Does an item rendering operation
	 */
	public boolean renderItem(ItemStack itemStack)
	{
		return false;
	}
}
