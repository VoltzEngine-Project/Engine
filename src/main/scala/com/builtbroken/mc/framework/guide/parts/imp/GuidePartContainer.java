package com.builtbroken.mc.framework.guide.parts.imp;

import com.builtbroken.mc.framework.json.imp.IJsonProcessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 2/16/2018.
 */
public abstract class GuidePartContainer<P extends GuidePartContainer, E extends GuidePart> extends GuidePart<P> implements Iterable<E>
{
    /** List of pages in order of display */
    protected LinkedList<String> orderOfDisplay = new LinkedList();
    /** Map of ids to page objects */
    protected HashMap<String, E> map = new HashMap();

    public GuidePartContainer(IJsonProcessor processor)
    {
        super(processor);
    }

    /** Map of section ids to section objects */
    public HashMap<String, E> getPartMap()
    {
        return map;
    }

    /**
     * List of parts in order they appear.
     * <p>
     * Feel free to use this to reorder the parts.
     * However, keep in mind the order may be important.
     *
     * @return
     */
    public List<String> getPartIDs()
    {
        return orderOfDisplay;
    }

    public <D extends GuidePartContainer> D add(E section)
    {
        this.orderOfDisplay.add(section.id.toLowerCase());
        this.map.put(section.id.toLowerCase(), section);
        return (D) this;
    }

    public E get(String page)
    {
        return page != null ? map.get(page.toLowerCase()) : null;
    }

    @Override
    public Iterator<E> iterator()
    {
        return new PartIterator();
    }

    protected class PartIterator implements java.util.Iterator<E> //TODO replace with less lazy version
    {
        Iterator<String> it;

        public PartIterator()
        {
            it = getPartIDs().iterator();
        }

        @Override
        public boolean hasNext()
        {
            return it.hasNext();
        }

        @Override
        public E next()
        {
            return GuidePartContainer.this.get(it.next());
        }
    }
}
