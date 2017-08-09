package com.builtbroken.mc.lib.json.imp;

/**
 * Applied to items or blocks to convert a meta entry used in a recipe or registry value to the actual meta. This
 * is used to allow states to be used rather than hardcoded integers. As well to fix issues with runtime generated
 * meta values.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 8/9/2017.
 */
public interface IJSONMetaConvert
{
    /**
     * Converts the string to its meta value
     *
     * @param value - string, ignore case as input will be lowercase to prevent issues
     * @return 0-15 for blocks, 0-31999 for items, -1 is an error state
     */
    int getMetaForValue(String value);
}
