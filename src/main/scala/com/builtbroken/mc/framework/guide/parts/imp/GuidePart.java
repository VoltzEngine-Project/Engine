package com.builtbroken.mc.framework.guide.parts.imp;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.util.ArrayList;
import java.util.List;

/**
 * Prefab for objects that are part of the guide book
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/16/2018.
 */
public abstract class GuidePart extends JsonGenData
{
    /** Unique id, lowercase */
    @JsonProcessorData("id")
    public String id;
    /** Unlocalized name for translation */
    @JsonProcessorData("unlocalized")
    public String unlocalized;

    /** Previous list of ids used for some level of backwards compat */
    @JsonProcessorData(value = "previous_ids", type = "list.array", args = "string")
    public List<String> previous_names = new ArrayList();

    public GuidePart(IJsonProcessor processor)
    {
        super(processor);
    }

    public <D extends GuidePart> D init(String id, String name)
    {
        this.id = id.toLowerCase();
        this.unlocalized = name;
        return (D) this;
    }
}
