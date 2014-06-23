package resonant.content.component.render

import net.minecraft.item.{ItemBlock, ItemStack}
import resonant.content.component.IComponent
import resonant.lib.render.RenderUtility
import org.lwjgl.opengl.GL11._

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
    RenderUtility.renderNormalBlockAsItem(itemStack.getItem().asInstanceOf[ItemBlock].field_150939_a, itemStack.getItemDamage(), RenderUtility.renderBlocks)
  }
}
