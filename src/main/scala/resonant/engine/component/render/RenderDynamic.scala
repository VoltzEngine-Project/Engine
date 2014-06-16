package resonant.engine.component.render

import resonant.engine.component.IComponent
import universalelectricity.core.transform.vector.Vector3

/**
 * @author Calclavia
 */
class RenderDynamic extends IComponent
{
  /**
   * Render the static, unmoving faces of this part into the world renderer. The Tesselator is
   * already drawing.
   */
  /**
   * Render the dynamic, changing faces of this part and other gfx as in a TESR. The Tesselator
   * will need to be started if it is to be used.
   */
  def renderDynamic(position: Vector3, isItem: Boolean, frame: Float)
  {

  }
}
