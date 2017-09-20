package com.builtbroken.mc.framework.json.event;

import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;
import com.builtbroken.mc.framework.json.imp.JsonLoadPhase;

/**
 * Called when a new entry is created
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/20/2017.
 */
public class JsonEntryCreationEvent extends JsonContentLoaderEvent
{
    public final String mod;
    public final String id;
    public final String contentID;
    public final IJsonGenObject object;

    /**
     * @param loader    - loader handling the content
     * @param loadPhase - may not reflect the load phase
     * @param mod       - might be null
     * @param contentID        - might be null
     * @param object    - object created
     */
    public JsonEntryCreationEvent(JsonContentLoader loader, JsonLoadPhase loadPhase, String mod, String id, String contentID, IJsonGenObject object)
    {
        super(loader, loadPhase);
        this.mod = mod;
        this.id = id;
        this.contentID = contentID;
        this.object = object;
    }
}
