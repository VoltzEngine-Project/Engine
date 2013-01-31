package basiccomponents.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import basiccomponents.common.BasicComponents;
import basiccomponents.common.container.ContainerCoalGenerator;
import basiccomponents.common.tileentity.TileEntityCoalGenerator;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCoalGenerator extends GuiContainer
{
	private TileEntityCoalGenerator tileEntity;

	private int containerWidth;
	private int containerHeight;

	public GuiCoalGenerator(InventoryPlayer par1InventoryPlayer, TileEntityCoalGenerator tileEntity)
	{
		super(new ContainerCoalGenerator(par1InventoryPlayer, tileEntity));
		this.tileEntity = tileEntity;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 55, 6, 4210752);
		this.fontRenderer.drawString("Generating", 90, 33, 4210752);
		String displayText = "";

		if (this.tileEntity.isDisabled())
		{
			displayText = "Disabled";
		}
		else if (this.tileEntity.generateWatts <= 0)
		{
			displayText = "Not Generating";
		}
		else if (this.tileEntity.generateWatts < this.tileEntity.MIN_GENERATE_WATTS)
		{
			displayText = "Hull Heat: " + (int) (this.tileEntity.generateWatts / this.tileEntity.MIN_GENERATE_WATTS * 100) + "%";
		}
		else
		{
			displayText = ElectricInfo.getDisplay(tileEntity.generateWatts, ElectricUnit.WATT);
		}

		this.fontRenderer.drawString(displayText, (int) (100 - displayText.length() * 1.25), 45, 4210752);
		this.fontRenderer.drawString("Voltage: " + (int) this.tileEntity.getVoltage(), 85, 60, 4210752);
		this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		int var4 = this.mc.renderEngine.getTexture(BasicComponents.FILE_PATH + "CoalGenerator.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(var4);
		containerWidth = (this.width - this.xSize) / 2;
		containerHeight = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
	}
}
