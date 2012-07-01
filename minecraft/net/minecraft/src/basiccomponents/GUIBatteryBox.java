package net.minecraft.src.basiccomponents;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;
import net.minecraft.src.universalelectricity.UniversalElectricity;

import org.lwjgl.opengl.GL11;

public class GUIBatteryBox extends GuiContainer
{
    private TileEntityBatteryBox tileEntity;

    private int containerWidth;
    private int containerHeight;

    public GUIBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityBatteryBox batteryBox)
    {
        super(new ContainerBatteryBox(par1InventoryPlayer, batteryBox));
        this.tileEntity = batteryBox;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("Battery Box", 65, 6, 4210752);
        this.fontRenderer.drawString("Electricity", 98, 30, 4210752);
        //String electricityStored = tileEntity.electricityStored+"";
        String displayText = UniversalElectricity.getWattDisplay(tileEntity.electricityStored);
        
        String displayText2 = UniversalElectricity.getWattDisplay(tileEntity.getElectricityCapacity());
        if(this.tileEntity.isDisabled()) displayText2 = "Disabled";
        
        this.fontRenderer.drawString(displayText, 110-displayText.length(), 40, 4210752);
        this.fontRenderer.drawString("Capacity: "+displayText2, 80, 60, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture(BasicComponents.filePath+"BatteryBox.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        containerWidth = (this.width - this.xSize) / 2;
        containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        
        int scale = (int)(((double)this.tileEntity.electricityStored/this.tileEntity.getElectricityCapacity())*72);
        this.drawTexturedModalRect(containerWidth + 87, containerHeight + 51, 176, 0, scale, 20);
    }
}
