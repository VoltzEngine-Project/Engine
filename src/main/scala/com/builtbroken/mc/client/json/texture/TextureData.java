package com.builtbroken.mc.client.json.texture;

import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.debug.IJsonDebugDisplay;
import com.builtbroken.mc.framework.json.imp.IJsonProcessor;
import com.builtbroken.mc.framework.json.processors.JsonGenData;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/23/2016.
 */
public class TextureData extends JsonGenData implements IJsonDebugDisplay
{
    public final String domain;
    public final String key;
    public final String name;
    public final Type type;

    private ResourceLocation cachedLocation;

    @Deprecated //Move to cache outside of this class
    private IIcon icon;

    public TextureData(IJsonProcessor processor, String key, String domain, String name, Type type)
    {
        super(processor);
        this.key = key;
        this.domain = domain;
        this.name = name;
        this.type = type;
    }

    public ResourceLocation getLocation()
    {
        if (cachedLocation == null)
        {
            cachedLocation = new ResourceLocation(domain, "textures/" + type.path + "/" + name + ".png");
        }
        return cachedLocation;
    }

    public IIcon getIcon()
    {
        return icon;
    }

    public void register(IIconRegister register)
    {
        icon = register.registerIcon(domain + ":" + name);
    }

    @Override
    public void register()
    {
        ClientDataHandler.INSTANCE.addTexture(key, this);
    }

    @Override
    public String getContentID()
    {
        return key;
    }

    @Override
    public String getUniqueID()
    {
        return key;
    }

    @Override
    public String getDisplayName()
    {
        return getContentID();
    }

    @Override
    public void addDebugLines(List<String> lines)
    {
        lines.add("Location = " + getLocation());
        if (type == Type.BLOCK || type == Type.ITEM)
        {
            lines.add("Icon = " + getIcon());
        }
        lines.add("Type = " + type.name().toLowerCase());
        lines.add("Domain = " + domain);
        lines.add("Path = " + name);
    }

    public enum Type
    {
        BLOCK("blocks"),
        ITEM("items"),
        MODEL("models"),
        EFFECT("fx"),
        OVERLAY("overlay"),
        GUI("gui");

        public final String path;

        Type(String path)
        {
            this.path = path;
        }

        public static Type get(String name)
        {
            for (Type type : values())
            {
                if (type.path.equalsIgnoreCase(name) || type.name().equalsIgnoreCase(name))
                {
                    return type;
                }
            }
            return null;
        }
    }
}
