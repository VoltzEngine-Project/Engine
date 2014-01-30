package calclavia.lib.prefab.turbine;

import net.minecraft.tileentity.TileEntity;
import universalelectricity.api.vector.Vector3;
import calclavia.lib.multiblock.reference.MultiBlockHandler;

public class TurbineMultiBlockHandler extends MultiBlockHandler<TileTurbine>
{
	public TurbineMultiBlockHandler(TileTurbine wrapper)
	{
		super(wrapper);
	}

	public TileTurbine getWrapperAt(Vector3 position)
	{
		TileEntity tile = position.getTileEntity(self.getWorld());

		if (tile != null && wrapperClass.isAssignableFrom(tile.getClass()))
		{
			if (((TileTurbine) tile).getDirection() == self.getDirection())
			{
				return (TileTurbine) tile;
			}
		}

		return null;
	}
}
