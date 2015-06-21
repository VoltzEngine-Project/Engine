package com.builtbroken.mc.api.tile;

import com.builtbroken.mc.api.tile.node.ITileModule;
import net.minecraftforge.common.util.ForgeDirection;

/** Applied to a TileEntity to provide access, from outside the tile, to the TileModules contained in the tile.
 *
 */
public interface ITileModuleProvider
{
	/**
	 * @param nodeType - The type of node we are looking for.
	 * @param from     - The direction.
	 * @return Returns the node object.
	 */
	<N extends ITileModule> N getModule(Class<? extends N> nodeType, ForgeDirection from);
}
