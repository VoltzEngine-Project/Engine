package universalelectricity.prefab;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiBase extends GuiScreen
{
	/**
	 * The X size of the inventory window in pixels.
	 */
	protected int xSize = 176;

	/**
	 * The Y size of the inventory window in pixels.
	 */
	protected int ySize = 166;

	/**
	 * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiLeft;

	/**
	 * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiTop;

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui()
	{
		super.initGui();
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		this.drawDefaultBackground();
		int var4 = this.guiLeft;
		int var5 = this.guiTop;
		this.drawBackgroundLayer(par1, par2, par3);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) var4, (float) var5, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		short var7 = 240;
		short var8 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var7 / 1.0F, (float) var8 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.drawForegroundLayer(par1, par2, par3);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Draws the foreground layer for the GUI
	 */
	protected abstract void drawForegroundLayer(int var2, int var3, float var1);

	/**
	 * Draws the background layer for the GUI
	 */

	protected abstract void drawBackgroundLayer(int var2, int var3, float var1);

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char x, int y)
	{
		if (y == 1 || y == this.mc.gameSettings.keyBindInventory.keyCode)
		{
			this.mc.thePlayer.closeScreen();
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead)
		{
			this.mc.thePlayer.closeScreen();
		}
	}
}