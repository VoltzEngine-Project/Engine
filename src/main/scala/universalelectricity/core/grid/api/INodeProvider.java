package universalelectricity.core.grid.api;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * @param <N> - Node type.
 * @author Calclavia
 */
public interface INodeProvider
{
	/**
	 * @param nodeType - The type of node we are looking for.
	 * @param from     - The direction.
	 * @return Returns the node object.
	 */
	public <N extends INode> N getNode(Class<? super N> nodeType, ForgeDirection from);
}
