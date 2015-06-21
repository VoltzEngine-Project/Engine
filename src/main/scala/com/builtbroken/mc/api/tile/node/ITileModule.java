package com.builtbroken.mc.api.tile.node;

import com.builtbroken.mc.api.tile.ITileModuleProvider;

/** Applied to an object that acts as a peace of a larger machine. Allowing for the machine to delegate handling to
 * this module. An example of its use is handling energy/wire connections for the tile. */
public interface ITileModule
{
	/** Called when the machine has loaded into the world */
    void onJoinWorld();

    /** Parent block has changed, use this time to rebuild data based on the world */
    void onParentChange();

	/** Called when the module is removed from the machine, or the machine is removed from the world */
    void onLeaveWorld();

    /** The object that houses this node */
    ITileModuleProvider getParent();

}
