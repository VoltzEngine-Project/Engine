package resonant.content.loader

import net.minecraft.item.Item
import resonant.content.spatial.block.SpatialBlock

/**
 * Automatic content registration for all fields extending this trait.
 * @author anti344, Calclavia
 */
trait ContentLoader
{
  self =>

  lazy val manager = new ModManager()

  def preInit() =
  {
    self.getClass.getDeclaredFields
      .map(m => {m.setAccessible(true); m.get(self) })
      .foreach
      {
        case item: Item =>
          manager.newItem(item)
        case block: SpatialBlock =>
          manager.newBlock(block)
        case _ =>
      }
  }
}