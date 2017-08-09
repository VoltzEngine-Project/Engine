package com.builtbroken.mc.framework.json.processors.recipe;

import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.item.ItemStack;

/**
 * Prefab for any recipe that has a single output
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 3/10/2017.
 */
public abstract class JsonRecipeData extends JsonGenData implements IJsonGenObject
{
    /** Output of the recipe */
    public Object output;

    public JsonRecipeData(IJsonProcessor processor, Object output)
    {
        super(processor);
        this.output = output;
    }

    /**
     * Output of the recipe
     * <p>
     * Cache as this may be re-calculated each call
     *
     * @return
     */
    public ItemStack getOutput()
    {
        return toStack(output);
    }


    @Override
    public void register()
    {

    }

    @Override
    public String toString()
    {
        return "JsonRecipeData[" + output + "]@" + hashCode();
    }
}
