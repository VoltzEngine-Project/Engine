package com.builtbroken.mc.codegen.templates.item;

import com.builtbroken.mc.codegen.processor.ItemWrappedTemplate;
import com.builtbroken.mc.framework.item.ItemBase;
import com.builtbroken.mc.framework.item.logic.ItemNode;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 5/9/2017.
 */
@ItemWrappedTemplate(annotationName = "Empty")
public class ItemTemplate extends ItemBase
{
    public ItemTemplate(ItemNode node)
    {
        super(node);
    }
}
