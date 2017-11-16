package com.builtbroken.mc.client.json.imp;

import com.builtbroken.mc.client.json.models.ModelCustomData;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;

/**
 * Applied to render states that support model rendering
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 4/4/2017.
 */
public interface IModelState extends IRenderState
{
    /** Amount to scale in each axis */
    Pos getScale();

    /** Amount to move in each axis */
    Pos getOffset();

    /** Amount to rotate in each axis */
    EulerAngle getRotation();

    /** Model to use */
    ModelCustomData getModel();

    /**
     * Gets the parts to render
     *
     * @return
     */
    String[] getPartsToRender();

    /** Texture to use */
    default TextureData getTexture()
    {
        return getTextureData(0);
    }

    /**
     * Called to render the model centered on a position
     *
     * @param subRender - is the render part of a larger render
     * @return true if rendered
     */
    default boolean render(boolean subRender)
    {
        return render(subRender, 0, 0, 0);
    }

    /**
     * Called to render the model centered on a position.
     * <p>
     * Rotation changes are used to dynamically modify the render.
     *
     * @param subRender - is the render part of a larger render
     * @param yaw       - rotation to add to default render
     * @param pitch     - rotation to add to default render
     * @param roll      - rotation to add to default render
     * @return true if rendered
     */
    boolean render(boolean subRender, float yaw, float pitch, float roll);
}
