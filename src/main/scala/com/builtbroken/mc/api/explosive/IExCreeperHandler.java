package com.builtbroken.mc.api.explosive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/** Applied to an explosive handler to change how a creeper looks when it contains the explosive
 * Created by robert on 2/3/2015.
 */
public interface IExCreeperHandler
{
    /**
     * Gets the texture to use instead of the default creeper texture
     *
     * @param entity - entity that will use the texture for its renderer
     * @return Valid resource location, or null to use default texture
     */
    @SideOnly(Side.CLIENT)
    public ResourceLocation getCreeperTexture(Entity entity);

    /**
     * Gets the string key to use for the translation of the creeper name
     * @param entity - creeper
     * @return valid string for translating
     */
    public String getTranslationKey(Entity entity);
}
