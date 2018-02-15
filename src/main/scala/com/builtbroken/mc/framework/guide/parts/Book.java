package com.builtbroken.mc.framework.guide.parts;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.loading.JsonProcessorData;
import com.builtbroken.mc.framework.json.processors.JsonGenData;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/15/2018.
 */
public class Book extends JsonGenData
{
    /** Unique id of the book, prefixed in front of everything (e.g. book.chapter.section.page ) */
    @JsonProcessorData("id")
    public String id;
    /** Display name of the chapter */
    @JsonProcessorData("name")
    public String name;

    /** List of sections in order of display */
    protected LinkedList<String> chapters = new LinkedList();
    /** Map of section ids to chapter objects */
    protected HashMap<String, Chapter> id_to_chapter = new HashMap();

    public Book(IJsonProcessor processor)
    {
        super(processor);
    }

    public Book add(Chapter chapter)
    {
        chapters.add(chapter.id);
        id_to_chapter.put(chapter.id, chapter);
        return this;
    }

    /** List of sections in order of display */
    public LinkedList<String> getChapters()
    {
        return chapters;
    }

    /** Map of section ids to chapter objects */
    public HashMap<String, Chapter> getId_to_chapter()
    {
        return id_to_chapter;
    }
}
