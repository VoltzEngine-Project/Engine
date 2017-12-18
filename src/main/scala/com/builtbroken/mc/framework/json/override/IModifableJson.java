package com.builtbroken.mc.framework.json.override;

import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Applied to processors that support modifying data after content has been loaded.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/14/2017.
 */
public interface IModifableJson<D extends IJsonGenObject>
{
    /**
     * Called to add data to the object
     *
     * @param key
     * @param data
     * @param generatedObject
     */
    void addData(String key, JsonElement data, D generatedObject);

    /**
     * Called to remove data from an object
     *
     * @param key
     * @param generatedObject
     */
    void removeData(String key, D generatedObject);

    /**
     * Called to replace or set data for an object
     *
     * @param key
     * @param data
     * @param generatedObject
     */
    void replaceData(String key, JsonElement data, D generatedObject);

    /**
     * Allows accessing the value of data stored in the object
     *
     * @param key
     * @param generatedObject
     * @return
     */
    Object getData(String key, D generatedObject);

    /**
     * Called to get possible modifications for the object
     * <p>
     * Primary use is to generate templates for use with the
     * override system. Only add data to the return that can
     * be used or it might break the system. Try to mirror
     * the original object as best as possible.
     * <p>
     * Make sure to include the minimal values needed
     * for override systems to load the file. This
     * should include:
     * processor ID
     * content ID
     * actions = [add, remove, replace]
     * data
     * <p>
     * See {@link JsonOverrideProcessor} and {@link JsonOverrideData} for
     * more details on internal logic.
     *
     * @param object
     * @return object containing modifications aviable
     */
    JsonObject getPossibleModificationsAsJson(IJsonGenObject object);
}
