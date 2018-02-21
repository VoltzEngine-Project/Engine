package com.builtbroken.mc.framework.guide.parts;

import com.builtbroken.mc.framework.guide.GuideEntry;
import com.builtbroken.mc.framework.guide.parts.comp.PageComponent;
import com.builtbroken.mc.framework.guide.parts.imp.GuidePart;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;

import java.util.LinkedList;
import java.util.List;

/**
 * Collection of words to display on the guide GUI. Do not think in terms of pages in a book. As formatting
 * may change how the page displays. This may cause what fit on one page to show on two or more pages. Especially
 * when translations are applied.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class Page extends GuidePart<Section>
{
    @JsonProcessorData("id")
    public String id;
    /** Display name of the chapter */
    @JsonProcessorData("name")
    public String name;

    //TODO find a way to store by language.. as we are not dumping a ton of entries into the localization system
    List<PageComponent> components = new LinkedList();

    public Page(IJsonProcessor processor)
    {
        super(processor);
    }

    @Override
    public GuideEntry getGuideEntry()
    {
        return parent != null ? parent.getGuideEntry().getWithPage(id) : null;
    }
}
