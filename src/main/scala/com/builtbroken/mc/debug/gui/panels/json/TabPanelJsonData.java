package com.builtbroken.mc.debug.gui.panels.json;

import com.builtbroken.mc.debug.IJsonDebugDisplay;
import com.builtbroken.mc.debug.data.DebugData;
import com.builtbroken.mc.debug.data.DebugJsonData;
import com.builtbroken.mc.debug.data.IJsonDebugData;
import com.builtbroken.mc.debug.gui.panels.imp.PanelDataList;
import com.builtbroken.mc.framework.json.JsonContentLoader;
import com.builtbroken.mc.framework.json.imp.IJsonGenObject;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 9/15/2017.
 */
public class TabPanelJsonData extends PanelDataList<IJsonGenObject>
{
    public final String PROCESSOR_KEY;

    public TabPanelJsonData(String key)
    {
        super();
        PROCESSOR_KEY = key;
    }

    @Override
    protected IJsonDebugData getDataEntryFor(IJsonGenObject object)
    {
        if (object instanceof IJsonDebugDisplay)
        {
            return new DebugJsonData((IJsonDebugDisplay) object);
        }
        return new DebugData("MOD: " + object.getMod() + "  ID: " + object.getContentID());
    }

    @Override
    protected void buildData()
    {
        data.clear();
        List<IJsonGenObject> genObjects = JsonContentLoader.INSTANCE.generatedObjects.get(PROCESSOR_KEY);
        if (genObjects != null && !genObjects.isEmpty())
        {
            data.addAll(genObjects);
        }
    }
}
