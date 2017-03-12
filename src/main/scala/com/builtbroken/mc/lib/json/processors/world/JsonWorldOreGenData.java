package com.builtbroken.mc.lib.json.processors.world;

import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.core.registry.implement.IPostInit;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.json.imp.IJsonProcessor;
import com.builtbroken.mc.lib.json.processors.JsonGenData;
import com.builtbroken.mc.lib.world.generator.OreGenReplaceStone;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public class JsonWorldOreGenData extends JsonGenData implements IPostInit
{
    Object block;

    String oreName;

    int min_y;
    int max_y;

    int branchSize;
    int chunkLimit;

    public JsonWorldOreGenData(IJsonProcessor processor, Object block, String oreName, int min, int max, int branch, int chunk)
    {
        super(processor);
        this.block = block;
        this.oreName = oreName;
        this.min_y = min;
        this.max_y = max;
        this.branchSize = branch;
        this.chunkLimit = chunk;
    }

    @Override
    public void onPostInit()
    {
        ItemStack stack = toStack(block);
        if (stack != null)
        {
            if (Engine.instance.getConfig().getBoolean("" + LanguageUtility.capitalizeFirst(oreName) + "_Ore", "WorldGen", true, "Enables generation of the ore in the world"))
            {
                GameRegistry.registerWorldGenerator(new OreGenReplaceStone(oreName, stack, min_y, max_y, chunkLimit, branchSize, "pickaxe", 1), 1);
            }
        }
    }
}
