package universalelectricity.basiccomponents;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.network.PacketManager;


public class GUIBatteryBox extends GuiContainer
{
    private TileEntityBatteryBox tileEntity;

    private int containerWidth;
    private int containerHeight;
    
    private long GUITicks = 0;

    public GUIBatteryBox(InventoryPlayer par1InventoryPlayer, TileEntityBatteryBox batteryBox)
    {
        super(new ContainerBatteryBox(par1InventoryPlayer, batteryBox));
        this.tileEntity = batteryBox;
    }
    
    public void initGui()
    {
    	super.initGui();
    	PacketManager.sendTileEntityPacketToServer(this.tileEntity, "BasicComponents", (int)-1, true);
    }
    
    @Override
    public void onGuiClosed()
    {
    	super.onGuiClosed();
    	PacketManager.sendTileEntityPacketToServer(this.tileEntity, "BasicComponents", (int)-1, false);
    }
    
    public void updateScreen()
    {
    	super.updateScreen();
    	
    	if(GUITicks % 100 == 0)
    	{
    		PacketManager.sendTileEntityPacketToServer(this.tileEntity, "BasicComponents", (int)-1, true);
    	}
    	GUITicks ++;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer()
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), 65, 6, 4210752);
        String displayText = ElectricInfo.getDisplay(tileEntity.getWattHours(), ElectricUnit.WATT_HOUR, 3, true);
        String displayText2 = ElectricInfo.getDisplaySimple(tileEntity.getMaxWattHours(), ElectricUnit.WATT_HOUR, 0);

        if (this.tileEntity.isDisabled())
        {
            displayText2 = "Disabled";
        }

        this.fontRenderer.drawString(displayText + " of", 99 - displayText.length(), 30, 4210752);
        this.fontRenderer.drawString(displayText2, 80, 40, 4210752);
        this.fontRenderer.drawString("Voltage: " + (int)this.tileEntity.getVoltage(), 90, 60, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture(BasicComponents.FILE_PATH + "BatteryBox.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        
        containerWidth = (this.width - this.xSize) / 2;
        containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);
        int scale = (int)(((double)this.tileEntity.getWattHours() / this.tileEntity.getMaxWattHours()) * 72);
        this.drawTexturedModalRect(containerWidth + 87, containerHeight + 51, 176, 0, scale, 20);
    }
}
