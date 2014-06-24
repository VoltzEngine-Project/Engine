package resonant.engine.content.debug

import net.minecraft.block.material.Material
import resonant.lib.content.prefab.java.TileElectricStorage
import universalelectricity.api.core.grid.electric.EnergyStorage

/**
 * @since 31/05/14
 * @author tgame14
 */
class TileInfiniteEnergy(mat: Material) extends TileElectricStorage(mat)
{
  energy = new EnergyStorage(Double.MaxValue)
  energy.setMaxExtract(Double.MaxValue)
  ioMap = 728

  override def update()
  {
    super.updateEntity()
    energy.setEnergy(Double.MaxValue)
  }

}
