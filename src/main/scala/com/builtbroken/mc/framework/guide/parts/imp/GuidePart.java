package com.builtbroken.mc.framework.guide.parts.imp;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/16/2018.
 */
public abstract class GuidePart extends JsonGenData
{
    @JsonProcessorData("id")
    public String id;
    @JsonProcessorData("name")
    public String name;
    @JsonProcessorData(value = "previous_names", type = "list.array", args = "string")
    public List<String> previous_names = new ArrayList();

    public GuidePart(IJsonProcessor processor)
    {
        super(processor);
    }

    public <D extends GuidePart> D init(String id, String name)
    {
        this.id = id;
        this.name = name;
        return (D) this;
    }
}
