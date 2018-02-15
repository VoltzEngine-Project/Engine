package com.builtbroken.mc.framework.guide.parts;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Single chapter in the guide book
 * <p>
 * Normal a chapter would be focused on a single item or concept. However, it doesn't matter so long as the content
 * helps the user.
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class Chapter extends JsonGenData
{
    /** Unique id of the chapter, prefixed in front of section and pages (e.g. chapter.section.page ) */
    @JsonProcessorData("id")
    public String id;
    /** Display name of the chapter */
    @JsonProcessorData("name")
    public String name;

    /** List of sections in order of display */
    protected LinkedList<String> sections = new LinkedList();
    /** Map of section ids to section objects */
    protected HashMap<String, Section> id_to_section = new HashMap();

    public Chapter(IJsonProcessor processor)
    {
        super(processor);
    }
}
