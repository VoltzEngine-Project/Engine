package com.builtbroken.mc.lib.world.map.radiation;

import com.builtbroken.mc.lib.world.map.data.ChunkMapManager;

/**
 * Handles updating radiation data for the game world
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/20/2017.
 */
public class RadiationDataManager extends ChunkMapManager<RadiationMap>
{
    public RadiationDataManager()
    {
        super("radiation", "radiation");
    }

    @Override
    protected RadiationMap createNewMap(int dim)
    {
        return null;
    }
}
