package universalelectricity.basiccomponents;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricInfo.ElectricUnit;


public class GUICoalGenerator extends GuiContainer
{
    private TileEntityCoalGenerator tileEntity;

    private int containerWidth;
    private int containerHeight;

    public GUICoalGenerator(InventoryPlayer par1InventoryPlayer, TileEntityCoalGenerator tileEntity)
    {
        super(new ContainerCoalGenerator(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
    }
    
    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString("Coal Generator", 55, 6, 4210752);
        this.fontRenderer.drawString("Generating", 90, 33, 4210752);
        String displayText = "";

        if (this.tileEntity.isDisabled())
        {
            displayText = "Disabled";
        }
        else if (tileEntity.connectedElectricUnit == null && !tileEntity.func_70314_l().isRemote)
        {
            displayText = "Not Connected";
        }
        else if (tileEntity.generateAmps * 20 <= 0)
        {
            displayText = "Not Generating";
        }
        else if (tileEntity.generateAmps * 20 < 20)
        {
            displayText = "Hull Heat: " + (int)(tileEntity.generateAmps * 100) + "%";
        }
        else
        {
            displayText = ElectricInfo.getDisplay(ElectricInfo.getWatts(tileEntity.generateAmps*20, tileEntity.getVoltage()), ElectricUnit.WATT);
        }

        this.fontRenderer.drawString(displayText, (int)(103 - displayText.length() * 1.25), 45, 4210752);
        this.fontRenderer.drawString("Voltage: " + (int)this.tileEntity.getVoltage(), 85, 60, 4210752);
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
