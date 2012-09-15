package universalelectricity.basiccomponents;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.StatCollector;

import org.lwjgl.opengl.GL11;

import universalelectricity.electricity.ElectricInfo;
import universalelectricity.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.network.PacketManager;

public class GUIElectricFurnace extends GuiContainer
{
    private TileEntityElectricFurnace tileEntity;

    private int containerWidth;
    private int containerHeight;
    
    private long GUITicks = 0;

    public GUIElectricFurnace(InventoryPlayer par1InventoryPlayer, TileEntityElectricFurnace tileEntity)
    {
        super(new ContainerElectricFurnace(par1InventoryPlayer, tileEntity));
        this.tileEntity = tileEntity;
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
        this.fontRenderer.drawString("Electric Furnace", 60, 6, 4210752);
        this.fontRenderer.drawString("Smelting:", 10, 28, 4210752);
        this.fontRenderer.drawString("Battery:", 10, 53, 4210752);
        String displayText = "";

        if (this.tileEntity.isDisabled())
        {
            displayText = "Disabled!";
        }
        else if (this.tileEntity.smeltingTicks > 0)
        {
            displayText = "Smelting";
        }
        else
        {
            displayText = "Idle";
        }

        this.fontRenderer.drawString("Status: " + displayText, 82, 45, 4210752);
        this.fontRenderer.drawString("Voltage: " + ElectricInfo.getDisplayShort(this.tileEntity.getVoltage(), ElectricUnit.VOLTAGE), 82, 56, 4210752);
        this.fontRenderer.drawString("Require: " + ElectricInfo.getDisplayShort(this.tileEntity.WATTS_PER_TICK*20, ElectricUnit.WATT), 82, 68, 4210752);
        this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
    * Draw the background layer for the GuiContainer (everything behind the items)
    */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int var4 = this.mc.renderEngine.getTexture(BasicComponents.FILE_PATH + "ElectricFurnace.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        containerWidth = (this.width - this.xSize) / 2;
        containerHeight = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(containerWidth, containerHeight, 0, 0, this.xSize, this.ySize);

        if (this.tileEntity.smeltingTicks > 0)
        {
            int scale = (int)(((double)this.tileEntity.smeltingTicks / this.tileEntity.SMELTING_TIME_REQUIRED) * 23);
            this.drawTexturedModalRect(containerWidth + 77, containerHeight + 24, 176, 0, 23 - scale, 20);
        }
    }
}