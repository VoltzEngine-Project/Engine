package resonant.core.content.debug

import universalelectricity.api.core.grid.electric.EnergyStorageHandler
import net.minecraft.block.material.Material
import resonant.lib.content.prefab.java.TileElectricStorage

/**
 * @since 31/05/14
 * @author tgame14
 */
class TileInfiniteEnergy(mat: Material) extends TileElectricStorage(mat)
{
	energy = new EnergyStorageHandler(Double.MaxValue)
	energy.setMaxExtract(Double.MaxValue)
	ioMap = 728

	override def updateEntity()
	{
		super.updateEntity()
		energy.setEnergy(Double.MaxValue)
	}

}
