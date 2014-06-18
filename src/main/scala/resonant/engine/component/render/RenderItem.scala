package resonant.engine.component.render

import net.minecraft.item.ItemStack
import resonant.engine.component.IComponent
import resonant.lib.render.RenderUtility

/**
 * @author Calclavia
 */
class RenderItem extends IComponent
{
  /**
   * Does an item rendering operation
   */
  def renderItem(itemStack: ItemStack)
  {
    glTranslated(0.5, 0.5, 0.5)
    RenderUtility.renderNormalBlockAsItem(block, metadata, renderer)
  }
}
