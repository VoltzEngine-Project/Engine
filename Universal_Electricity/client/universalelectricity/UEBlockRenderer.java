package net.minecraft.src.universalelectricity;

import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.forge.ITextureProvider;

import org.lwjgl.opengl.GL11;

/**
 * Use this class if you want your block to constantly update it's texture (e.g rotatable blocks)
 * @author Henry
 *
 */
public class UEBlockRenderer extends TileEntitySpecialRenderer
{
    private UERenderBlocks renderBlocks = new UERenderBlocks();
    
	@Override
	public void renderTileEntityAt(TileEntity blockToBeRendered, double par2, double par4, double par6, float par8)
	{
		if(blockToBeRendered instanceof ITextureProvider)
		{
			if(((ITextureProvider)blockToBeRendered).getTextureFile() != null)
			{
				this.bindTextureByName(((ITextureProvider)blockToBeRendered).getTextureFile());
				GL11.glPushMatrix();
		        GL11.glTranslatef((float)par2+0.5F, (float)par4+0.5F, (float)par6+0.5F);
		        GL11.glPushMatrix();
		        renderBlocks.renderBlockWithMetadata(blockToBeRendered.worldObj, blockToBeRendered.getBlockType(), blockToBeRendered.xCoord, blockToBeRendered.yCoord, blockToBeRendered.zCoord);
		        GL11.glPopMatrix();
		        GL11.glPopMatrix();
			}
		}
	}
}
