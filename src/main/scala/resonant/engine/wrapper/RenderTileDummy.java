package resonant.engine.wrapper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.transform.vector.Vector3;

/**
 * @author Calclavia
 */
@SideOnly(Side.CLIENT)
public class RenderTileDummy extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f)
	{
		if (tile instanceof TileBlock)
		{
			TileBlock tileBlock = ((TileBlock) tile);

			if (tileBlock.getRenderer() != null)
			{
				tileBlock.getRenderer().renderDynamic(new Vector3(x, y, z), false, f);
			}
		}
	}
}
