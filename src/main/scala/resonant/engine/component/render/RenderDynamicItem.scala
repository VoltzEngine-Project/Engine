package resonant.engine.component.render

import net.minecraft.item.ItemStack
import universalelectricity.core.transform.vector.Vector3

/**
 * A quick way to redirect an item render into the dynamic render. Useful for blocks with models that need to have the same item model being rendered.
 * @author Calclavia
 */
class RenderDynamicItem(dynamic : RenderDynamic) extends RenderItem
{
  def renderItem(itemStack: ItemStack)
  {
    dynamic.renderDynamic(new Vector3(), true, 0)
  }
}
