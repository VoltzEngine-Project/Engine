package com.builtbroken.mc.prefab.json;

import com.builtbroken.mc.lib.helper.MaterialDict;
import net.minecraft.block.Block;

/**
 * Block generated through a json based file format... Used to reduce dependency on code
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class BlockJson extends Block
{
    protected BlockJson(String name, String mat)
    {
        super(MaterialDict.get(mat));
        setBlockName(name);
    }
}
