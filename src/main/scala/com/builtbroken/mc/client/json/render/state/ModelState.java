package com.builtbroken.mc.client.json.render.state;

import com.builtbroken.jlib.helpers.MathHelper;
import com.builtbroken.mc.client.SharedAssets;
import com.builtbroken.mc.client.json.ClientDataHandler;
import com.builtbroken.mc.client.json.imp.IModelState;
import com.builtbroken.mc.client.json.models.ModelData;
import com.builtbroken.mc.client.json.texture.TextureData;
import com.builtbroken.mc.imp.transform.rotation.EulerAngle;
import com.builtbroken.mc.imp.transform.vector.Pos;
import cpw.mods.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

/**
 * Render/Texture/Animation states used for rendering models in the game
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 11/22/2016.
 */
public class ModelState extends TextureState implements IModelState
{
    public String modelID;
    public String[] parts;
    public Pos offset;
    public Pos scale;
    public EulerAngle rotation;
    public String[] rotationOrder = new String[] { "roll", "pitch", "yaw"};

    public boolean renderParent = false;
    public boolean renderOnlyParts = true;

    public ModelState(String ID)
    {
        super(ID);
    }

    public ModelState(String ID, String modelID, Pos offset, Pos scale, EulerAngle rotation)
    {
        this(ID);
        this.modelID = modelID;
        this.offset = offset;
        this.scale = scale;
        this.rotation = rotation;
    }

    @Override
    public boolean render(boolean subRender, float yaw, float pitch, float roll)
    {
        TextureData textureData = getTexture();
        ModelData data = getModel();
        if (data != null && data.getModel() != null)
        {
            //Starts rendering by storing previous matrix
            GL11.glPushMatrix();

            if (!subRender)
            {
                //TODO handle parent additions, in which parent and child data are combined
                //Scales object by value
                if (scale != null)
                {
                    GL11.glScaled(scale.x(), scale.y(), scale.z());
                }
                else if (parentState instanceof IModelState && ((IModelState) parentState).getScale() != null)
                {
                    GL11.glScaled(((IModelState) parentState).getScale().x(), ((IModelState) parentState).getScale().y(), ((IModelState) parentState).getScale().z());
                }

                //Apply rotation, using render defined order
                if(rotationOrder != null)
                {
                    for (String r : rotationOrder)
                    {
                        if (r != null)
                        {
                            if (r.equalsIgnoreCase("roll"))
                            {
                                doRoll(roll);
                            }
                            else if (r.equalsIgnoreCase("pitch"))
                            {
                                doPitch(pitch);
                            }
                            else if (r.equalsIgnoreCase("yaw"))
                            {
                                doYaw(yaw);
                            }
                        }
                    }
                }

                //Moves the object
                if (offset != null)
                {
                    GL11.glTranslated(offset.x(), offset.y(), offset.z());
                }
                else if (parentState instanceof IModelState && ((IModelState) parentState).getOffset() != null)
                {
                    GL11.glTranslated(((IModelState) parentState).getOffset().x(), ((IModelState) parentState).getOffset().y(), ((IModelState) parentState).getOffset().z());
                }
            }

            //Render parent
            GL11.glPushMatrix();
            if (parentState instanceof IModelState)
            {
                if (renderParent)
                {
                    ((IModelState) parentState).render(true);
                }
                else if (parts == null && parentState instanceof ModelState && ((ModelState) parentState).renderParent)
                {
                    if (((ModelState) parentState).parentState instanceof IModelState)
                    {
                        ((IModelState) ((ModelState) parentState).parentState).render(true);
                    }
                }
            }
            GL11.glPopMatrix();

            //Binds texture
            if (textureData != null)
            {
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(textureData.getLocation());
            }
            else
            {
                //Backup texture bind, if no texture
                FMLClientHandler.instance().getClient().renderEngine.bindTexture(SharedAssets.GREY_TEXTURE);
            }

            //Render model
            data.render(renderOnlyParts, getPartsToRender());

            //Ends render by restoring previous matrix(rotation, position, etc)
            GL11.glPopMatrix();
            return true;
        }
        return false;
    }

    private void doRoll(float extra)
    {
        float roll = extra;
        if (rotation != null)
        {
            roll += rotation.roll();
        }
        else if (parentState instanceof IModelState && ((IModelState) parentState).getRotation() != null)
        {
            roll += ((IModelState) parentState).getRotation().roll();
        }

        GL11.glRotated(MathHelper.clampAngleTo360(roll), 0, 0, 1);
    }

    private void doPitch(float extra)
    {
        float pitch = extra;
        if (rotation != null)
        {
            pitch += rotation.pitch();
        }
        else if (parentState instanceof IModelState && ((IModelState) parentState).getRotation() != null)
        {
            pitch += ((IModelState) parentState).getRotation().pitch();
        }

        GL11.glRotated(MathHelper.clampAngleTo360(pitch), 1, 0, 0);
    }

    private void doYaw(float extra)
    {
        float yaw = extra;
        if (rotation != null)
        {
           yaw += rotation.yaw();
        }
        else if (parentState instanceof IModelState && ((IModelState) parentState).getRotation() != null)
        {
            yaw += ((IModelState) parentState).getRotation().yaw();
        }

        GL11.glRotated(MathHelper.clampAngleTo360(yaw), 0, 1, 0);
    }

    @Override
    public Pos getScale()
    {
        if (scale == null)
        {
            return parentState instanceof IModelState ? ((IModelState) parentState).getScale() : null;
        }
        else if (parentState instanceof IModelState)
        {
            //TODO add to parent rotation, or null out rotation
            //TODO setup logic via configs to allow users to decide how rotation is used
        }
        return scale;
    }

    @Override
    public Pos getOffset()
    {
        if (offset == null)
        {
            return parentState instanceof IModelState ? ((IModelState) parentState).getOffset() : null;
        }
        else if (parentState instanceof IModelState)
        {
            //TODO add to parent rotation, or null out rotation
            //TODO setup logic via configs to allow users to decide how rotation is used
        }
        return offset;
    }

    @Override
    public EulerAngle getRotation()
    {
        if (rotation == null)
        {
            return parentState instanceof IModelState ? ((IModelState) parentState).getRotation() : null;
        }
        else if (parentState instanceof IModelState)
        {
            //TODO add to parent rotation, or null out rotation
            //TODO setup logic via configs to allow users to decide how rotation is used
        }
        return rotation;
    }

    @Override
    public ModelData getModel()
    {
        if (parentState instanceof IModelState)
        {
            return ((IModelState) parentState).getModel();
        }
        return ClientDataHandler.INSTANCE.getModel(modelID);
    }

    @Override
    public String[] getPartsToRender()
    {
        if (parentState instanceof IModelState && (parts == null || parts.length == 0))
        {
            return ((IModelState) parentState).getPartsToRender();
        }
        return parts;
    }

    @Override
    public TextureData getTexture()
    {
        TextureData textureData = ClientDataHandler.INSTANCE.getTexture(textureID);
        if (textureData == null && parentState instanceof IModelState)
        {
            return ((IModelState) parentState).getTexture();
        }
        return textureData;
    }
}
