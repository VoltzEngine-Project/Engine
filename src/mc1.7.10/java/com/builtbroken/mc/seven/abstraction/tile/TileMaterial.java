package com.builtbroken.mc.seven.abstraction.tile;

import com.builtbroken.mc.api.abstraction.tile.ITileMaterial;
import net.minecraft.block.material.Material;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/12/2017.
 */
public class TileMaterial implements ITileMaterial
{
    private final Material material;
    private final String name;

    public TileMaterial(Material material, String name)
    {
        this.material = material;
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Material unwrap()
    {
        return material;
    }
}
