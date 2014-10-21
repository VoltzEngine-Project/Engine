package resonant.engine.content.debug;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import resonant.engine.ResonantEngine;
import resonant.lib.gui.GuiContainerBase;
import resonant.lib.network.discriminator.PacketTile;
import resonant.lib.schematic.Schematic;
import resonant.lib.schematic.SchematicRegistry;
import resonant.lib.utility.LanguageUtility;

public class GuiCreativeBuilder extends GuiContainerBase
{
	private GuiTextField textFieldSize;
    private TileCreativeBuilder builder;

	public GuiCreativeBuilder(TileCreativeBuilder builder)
	{
		super();
		this.builder = builder;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.textFieldSize = new GuiTextField(fontRendererObj, 45, 58, 50, 12);
        this.textFieldSize.setText("10");
		this.buttonList.add(new GuiButton(0, this.width / 2 - 80, this.height / 2 - 10, 58, 20, "Build"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 - 35, 120, 20, "Mode"));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		fontRendererObj.drawString("Creative Builder", 60, 6, 4210752);
		fontRendererObj.drawString("This is a creative only cheat", 9, 20, 4210752);
		fontRendererObj.drawString("which allows you to auto build", 9, 30, 4210752);
		fontRendererObj.drawString("structures for testing.", 9, 40, 4210752);

		fontRendererObj.drawString("Size: ", 9, 60, 4210752);
		this.textFieldSize.drawTextBox();

        //Button Name
        Schematic sch = SchematicRegistry.INSTANCE.getByID(builder.schematicID());
        String name = sch != null ? LanguageUtility.getLocal(sch.getName()) : "None";
		((GuiButton) this.buttonList.get(1)).displayString = name;


        fontRendererObj.drawString("Mode: ", 9, 80, 4210752);
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		this.textFieldSize.textboxKeyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int x, int y, int par3)
	{
		super.mouseClicked(x, y, par3);
		this.textFieldSize.mouseClicked(x - containerWidth, y - containerHeight, par3);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		super.actionPerformed(par1GuiButton);

		if (par1GuiButton.id == 0)
		{
			try
			{
                builder.size_$eq(Integer.parseInt(this.textFieldSize.getText()));
			}
			catch (NumberFormatException e)
			{
                textFieldSize.setText("");
			}

			if (builder.size() > 0)
			{
                ResonantEngine.instance.packetHandler.sendToServer(new PacketTile(builder, 0, builder.schematicID(), builder.size()));
                this.mc.thePlayer.closeScreen();
			}
		}
		else if (par1GuiButton.id == 1)
		{
            builder.schematicID_$eq((builder.schematicID() + 1) % (SchematicRegistry.INSTANCE.size()));
		}
	}

}