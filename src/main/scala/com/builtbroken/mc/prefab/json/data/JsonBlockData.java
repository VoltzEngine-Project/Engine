package com.builtbroken.mc.prefab.json.data;

/**
 * Holds data waiting to be processed
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 6/24/2016.
 */
public class JsonBlockData<C extends Object>
{
    //TODO add call back to original mod with object generated
    private C object;

    protected void setObject(C object)
    {
        this.object = object;
    }

    public C getObject()
    {
        return object;
    }
}
