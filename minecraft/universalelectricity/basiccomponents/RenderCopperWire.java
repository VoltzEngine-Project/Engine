package universalelectricity.basiccomponents;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

import universalelectricity.basiccomponents.BasicComponents;
import universalelectricity.basiccomponents.TileEntityCopperWire;


public class RenderCopperWire extends TileEntitySpecialRenderer
{
    private ModelCopperWire model;

    public RenderCopperWire()
    {
        model = new ModelCopperWire();
    }

    public void renderAModelAt(TileEntityCopperWire tileEntity, double d, double d1, double d2, float f)
    {
        //Texture file
        bindTextureByName(BasicComponents.FILE_PATH + "CopperWire.png");
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);

        if (tileEntity.connectedBlocks[0] != null)
        {
            model.renderBottom();
        }

        if (tileEntity.connectedBlocks[1] != null)
        {
            model.renderTop();
        }

        if (tileEntity.connectedBlocks[2] != null)
        {
        	model.renderBack();
        }

        if (tileEntity.connectedBlocks[3] != null)
        {
        	model.renderFront();
        }

        if (tileEntity.connectedBlocks[4] != null)
        {
        	model.renderLeft();
        }

        if (tileEntity.connectedBlocks[5] != null)
        {
        	model.renderRight();
        }

        model.renderMiddle();
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderAModelAt((TileEntityCopperWire)tileEntity, var2, var4, var6, var8);
    }
}