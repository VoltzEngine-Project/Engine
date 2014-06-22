package resonant.content.component.render

import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.world.IBlockAccess
import resonant.content.component.IComponent
import universalelectricity.core.transform.vector.Vector3

/**
 * @author Calclavia
 */
class RenderStatic extends IComponent
{
  /**
   * Render the static, unmoving faces of this part into the world renderer. The Tesselator is
   * already drawing.
   */
  def renderStatic(access: IBlockAccess, block: Block, renderer: RenderBlocks, position: Vector3)
  {
    renderer.renderStandardBlock(block, position.xi, position.yi, position.zi)
  }
}
