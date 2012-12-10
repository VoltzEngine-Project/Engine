package basiccomponents;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;

import basiccomponents.tile.TileEntityCopperWire;

public class RenderCopperWire extends TileEntitySpecialRenderer
{
	private ModelCopperWire model;

	public RenderCopperWire()
	{
		model = new ModelCopperWire();
	}

	public void renderAModelAt(TileEntityCopperWire tileEntity, double d, double d1, double d2, float f)
	{
		// Texture file
		bindTextureByName(BasicComponents.FILE_PATH + "CopperWire.png");
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 1.5F, (float) d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);

		if (tileEntity.visuallyConnected[0])
		{
			model.renderBottom();
		}

		if (tileEntity.visuallyConnected[1])
		{
			model.renderTop();
		}

		if (tileEntity.visuallyConnected[2])
		{
			model.renderBack();
		}

		if (tileEntity.visuallyConnected[3])
		{
			model.renderFront();
		}

		if (tileEntity.visuallyConnected[4])
		{
			model.renderLeft();
		}

		if (tileEntity.visuallyConnected[5])
		{
			model.renderRight();
		}

		model.renderMiddle();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderAModelAt((TileEntityCopperWire) tileEntity, var2, var4, var6, var8);
	}
}