package com.builtbroken.mc.client.json.imp;

import com.builtbroken.mc.client.json.models.ModelData;
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
    ModelData getModel();

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
     * <p>
     * Position and rotation is preset so no need to worry about. All
     * this render call should do is render the model in the state given.
     * The state should be static with no change. If a dynamic state is
     * needs used a dynamic render system.
     *
     * @return true if rendered
     */
    boolean render(boolean subRender);
}
