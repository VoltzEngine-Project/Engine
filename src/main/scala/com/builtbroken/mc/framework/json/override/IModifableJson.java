package com.builtbroken.mc.framework.json.override;

import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;

/**
 * Applied to processors that support modifying data after content has been loaded.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2017.
 */
public interface IModifableJson<D extends IJsonGenObject>
{
    void addData(String key, JsonElement data, D generatedObject);

    void removeData(String key, D generatedObject);

    void replaceData(String key, JsonElement data, D generatedObject);
}
