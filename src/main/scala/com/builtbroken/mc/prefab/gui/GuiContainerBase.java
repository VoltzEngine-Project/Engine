package com.builtbroken.mc.prefab.gui;

import com.builtbroken.jlib.data.Colors;
import com.builtbroken.jlib.data.science.units.UnitDisplay;
import com.builtbroken.jlib.data.science.units.UnitDisplay.Unit;
import com.builtbroken.mc.client.SharedAssets;
import com.builtbroken.mc.core.Engine;
import com.builtbroken.mc.lib.helper.LanguageUtility;
import com.builtbroken.mc.lib.render.RenderUtility;
import com.builtbroken.mc.lib.transform.region.Rectangle;
import com.builtbroken.mc.lib.transform.vector.Point;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class GuiContainerBase extends GuiContainer
{
    protected static int energyType = 0;
    public ResourceLocation baseTexture;

    public String tooltip = "";

    protected HashMap<Rectangle, String> tooltips = new HashMap();
    protected ArrayList<GuiTextField> fields = new ArrayList();

    protected int meterHeight = 49;
    protected int meterWidth = 14;

    protected int containerWidth;
    protected int containerHeight;
    private float lastChangeFrameTime;

    public GuiContainerBase(Container container)
    {
        super(container);
        this.baseTexture = SharedAssets.GUI_MC_BASE;
    }

    public GuiContainerBase()
    {
        this(new ContainerDummy());
        this.baseTexture = SharedAssets.GUI_MC_BASE;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        this.buttonList.clear();
        this.fields.clear();
    }

    /**
     * Adds a button to the GUI
     *
     * @param button
     * @param <E>
     * @return
     */
    protected <E extends GuiButton> E addButton(E button)
    {
        buttonList.add(button);
        return button;
    }

    protected void drawString(String str, int x, int y, int color)
    {
        fontRendererObj.drawString(str, x, y, color);
    }

    protected void drawString(String str, int x, int y)
    {
        drawString(str, x, y, 4210752);
    }

    protected void drawString(String str, int x, int y, Color color)
    {
        drawString(str, x, y, Colors.getIntFromColor(color));
    }

    protected void drawStringCentered(String str, int x, int y)
    {
        drawStringCentered(str, x, y, 4210752);
    }

    protected void drawStringCentered(String str, int x, int y, Color color)
    {
        drawStringCentered(str, x, y, Colors.getIntFromColor(color));
    }

    protected void drawStringCentered(String str, int x, int y, int color)
    {
        drawString(str, x - (fontRendererObj.getStringWidth(str) / 2), y, color);
    }

    protected GuiTextField newField(int x, int y, int w, String msg)
    {
        return this.newField(x, y, w, 20, msg);
    }

    protected GuiTextField newField(int x, int y, int w, int h, String msg)
    {
        GuiTextField x_field = new GuiTextField(this.fontRendererObj, x, y, w, h);
        x_field.setText("" + msg);
        x_field.setMaxStringLength(15);
        x_field.setTextColor(16777215);
        fields.add(x_field);
        return x_field;
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        for (Entry<Rectangle, String> entry : this.tooltips.entrySet())
        {
            if (entry.getKey().isWithin(new Point(mouseX - this.guiLeft, mouseY - this.guiTop)))
            {
                this.tooltip = entry.getValue();
                break;
            }
        }

        if (this.tooltip != null && this.tooltip != "")
        {
            this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, LanguageUtility.splitStringPerWordIntoArray(this.tooltip, 5));
        }

        this.tooltip = "";
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        if (fields != null && fields.size() > 0)
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            for (GuiTextField field : fields)
            {
                field.drawTextBox();
            }
        }
    }

    @Override
    protected void keyTyped(char c, int id)
    {
        boolean f = false;
        for (GuiTextField field : fields)
        {
            field.textboxKeyTyped(c, id);
            if (field.isFocused())
            {
                return;
            }
        }
        if (!f)
        {
            super.keyTyped(c, id);
        }
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        for (GuiTextField field : fields)
        {
            field.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
    {
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;

        this.mc.renderEngine.bindTexture(this.baseTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
    }

    //TODO update and docs
    protected void drawBulb(int x, int y, boolean isOn)
    {
        this.mc.renderEngine.bindTexture(this.baseTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        if (isOn)
        {
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 161, 0, 6, 6);

        }
        else
        {
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 161, 4, 6, 6);
        }
    }

    //TODO update and docs
    protected void drawSlot(int x, int y, ItemStack itemStack)
    {
        this.mc.renderEngine.bindTexture(this.baseTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);

        if (itemStack != null)
        {
            this.drawItemStack(itemStack, this.containerWidth + x, this.containerHeight + y);
        }
    }

    //TODO update and docs
    protected void drawItemStack(ItemStack itemStack, int x, int y)
    {
        x += 1;
        y += 1;
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);

        // drawTexturedModelRectFromIcon
        // GL11.glEnable(GL11.GL_BLEND);
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, this.mc.renderEngine, itemStack, x, y);
        // GL11.glDisable(GL11.GL_BLEND);
    }

    //TODO update and docs
    protected void drawTextWithTooltip(String textName, String format, int x, int y, int mouseX, int mouseY)
    {
        this.drawTextWithTooltip(textName, format, x, y, mouseX, mouseY, 4210752);
    }

    //TODO update and docs
    protected void drawTextWithTooltip(String textName, String format, int x, int y, int mouseX, int mouseY, int color)
    {
        String name = LanguageUtility.getLocal("gui." + textName + ".name");
        String text = format.replaceAll("%1", name);
        fontRendererObj.drawString(text, x, y, color);

        String tooltip = LanguageUtility.getLocal("gui." + textName + ".tooltip");

        if (tooltip != null && tooltip != "")
        {
            if (new Rectangle(x, y, (int) (text.length() * 4.8), 12).isWithin(new Point(mouseX, mouseY)))
            {
                this.tooltip = tooltip;
            }
        }
    }

    //TODO update and docs
    protected void drawTextWithTooltip(String textName, int x, int y, int mouseX, int mouseY)
    {
        this.drawTextWithTooltip(textName, "%1", x, y, mouseX, mouseY);
    }

    //TODO update and docs
    protected void drawSlot(Slot slot)
    {
        drawSlot(slot.xDisplayPosition - 1, slot.yDisplayPosition - 1); //TODO get slot type from slot
        if (Engine.runningAsDev)
        {
            this.drawStringCentered("" + slot.getSlotIndex(), guiLeft + slot.xDisplayPosition + 9, guiTop + slot.yDisplayPosition + 9, Color.YELLOW);
            this.drawStringCentered("" + slot.slotNumber, guiLeft + slot.xDisplayPosition + 9, guiTop + slot.yDisplayPosition + 1, Color.RED);
        }
    }

    //TODO update and docs
    protected void drawSlot(int x, int y)
    {
        this.drawSlot(x, y, GuiSlotType.NONE);
    }

    //TODO update and docs
    protected void drawSlot(int x, int y, GuiSlotType type)
    {
        this.drawSlot(x, y, type, 1, 1, 1);
    }

    //TODO update and docs
    protected void drawSlot(int x, int y, GuiSlotType type, float r, float g, float b)
    {
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);
        GL11.glColor4f(r, g, b, 1.0F);

        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);

        if (type != GuiSlotType.NONE)
        {
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 18 * type.ordinal(), 18, 18);
        }
    }

    /**
     * Draws a large green fill bar, with background, for
     * use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawLargeBar(int x, int y, int w, float percent, Color color)
    {
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);

        int width = Math.round(percent * 138);
        //Draws bar background
        setColor(null);
        drawRectWithScaledWidth(containerWidth + x, containerHeight + y, 54, 33, 140, 15, w);
        //draws the percent fill bar
        setColor(color);
        drawRectWithScaledWidth(containerWidth + x + 1, containerHeight + y + 1, 55, 65, width, 13, w);
    }

    /**
     * Draws a smaller green fill bar than {@link #drawLargeBar(int, int, int, float, Color)},
     * with background, for use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawSmallBar(int x, int y, int w, float percent, Color color)
    {
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);

        final int width = Math.round(percent * 105);
        //Draws bar background
        setColor(null);
        drawRectWithScaledWidth(containerWidth + x, containerHeight + y, 54, 0, 107, 11, w);

        //draws the percent fill bar
        setColor(color);
        drawRectWithScaledWidth(containerWidth + x + 1, containerHeight + y + 1, 55, 24, width, 9, w);
    }

    /**
     * Draws a smaller green fill bar than {@link #drawSmallBar(int, int, int, float, Color)},
     * with background, for use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawMicroBar(int x, int y, float percent, Color color)
    {
        drawMicroBar(x, y, -1, percent, color);
    }

    /**
     * Draws a smaller green fill bar than {@link #drawSmallBar(int, int, int, float, Color)},
     * with background, for use in rendering gauges or progress bars
     *
     * @param x       - render pos
     * @param y       - render pos
     * @param w       - width of the bar, min 6
     * @param percent - 0f to 1f on how full the bar should render
     * @param color   - color of the bar, null uses default
     */
    public void drawMicroBar(int x, int y, int w, float percent, Color color)
    {
        //Local constants
        final int backgroundWidth = 56;
        final int fillBarWidth = 54;

        //Test texture to correct resource
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);


        //Render background bar
        setColor(null);
        drawRectWithScaledWidth(containerWidth + x, containerHeight + y, 54, 79, backgroundWidth, 7, w);


        //Render foreground bar
        final int width = Math.round(percent * fillBarWidth);
        setColor(color);
        drawRectWithScaledWidth(containerWidth + x + 1, containerHeight + y + 1, 55, 87, width, 5, w - 2);
    }

    /**
     * Draws a rectangle with an increased or decreased width value
     * <p>
     * This works by duplicating the middle (3, width - 3) of the rectangle
     *
     * @param x        - render pos
     * @param y        - render pos
     * @param u        - x pos of the texture in it's texture sheet
     * @param v        - y pos of the texture in it's texture sheet
     * @param width    - width of the texture
     * @param height   - height of the texture
     * @param newWidth - new width to render the rectangle, minimal size of 6
     */
    protected void drawRectWithScaledWidth(int x, int y, int u, int v, int width, int height, int newWidth)
    {
        if(width > 0)
        {
            //If both widths are the same redirect to original call
            if (newWidth <= 0 || width == newWidth)
            {
                drawTexturedModalRect(x, y, u, v, width, height);
            }

            //Size of the middle section of the image
            final int midWidth = width - 6;

            //Start cap of image rect
            drawTexturedModalRect(x, y, u, v, 3, height);
            x += 3;

            //only render middle if it is larger than 6
            if (newWidth > 6)
            {
                //Loop over number of sections that need to be rendered
                int loops = newWidth / width;
                while (loops > 0)
                {
                    drawTexturedModalRect(x, y, u + 3, v, midWidth, height);
                    x += midWidth;
                    loops -= 1;
                }

                //Check if there is a remainder that still needs rendered
                loops = newWidth % width;
                if (loops != 0)
                {
                    drawTexturedModalRect(x, y, u + 3, v, loops, height);
                    x += loops;
                }
            }

            //End cap of image rect
            drawTexturedModalRect(x, y, u + width - 3, v, 3, height);
        }
    }

    /**
     * Sets the render color for the GUI render
     *
     * @param color - color, null will force default
     */
    protected void setColor(Color color)
    {
        if (color == null)
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            GL11.glColor3f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        }
    }

    //TODO update and docs
    protected void drawElectricity(int x, int y, float scale)
    {
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        /** Draw background progress bar/ */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11);

        if (scale > 0)
        {
            /** Draw white color actual progress. */
            this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 22, (int) (scale * 107), 11);
        }
    }

    //TODO update and docs
    protected void drawMeter(int x, int y, float scale, float r, float g, float b)
    {
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        /** Draw the background meter. */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, this.meterWidth, this.meterHeight);

        /** Draw liquid/gas inside */
        GL11.glColor4f(r, g, b, 1.0F);
        int actualScale = (int) ((this.meterHeight - 1) * scale);
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y + (this.meterHeight - 1 - actualScale), 40, 49, this.meterHeight - 1, actualScale);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        /** Draw measurement lines */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 49 * 2, this.meterWidth, this.meterHeight);
    }

    //TODO update and docs
    protected void drawMeter(int x, int y, float scale, FluidStack liquidStack)
    {
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        /** Draw the background meter. */
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, meterWidth, meterHeight);

        /** Draw liquid/gas inside */
        if (liquidStack != null)
        {
            this.displayGauge(this.containerWidth + x, this.containerHeight + y, -10, 1, 12, (int) ((meterHeight - 1) * scale), liquidStack);
        }

        /** Draw measurement lines */
        this.mc.renderEngine.bindTexture(SharedAssets.GUI_COMPONENTS);
        this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 49 * 2, meterWidth, meterHeight);
    }

    protected void drawSlot(int x, int y, EnumGuiIconSheet type, float r, float g, float b)
    {
        GL11.glColor4f(r, g, b, 1.0F);
        EnumGuiIconSheet.NONE.draw(this, x + containerWidth, y + containerHeight);
        if (type != EnumGuiIconSheet.NONE)
        {
            type.draw(this, x + containerWidth, y + containerHeight);
        }
    }

    protected void drawSlot(int x, int y, EnumGuiIconSheet type)
    {
        this.drawSlot(x, y, type, 1, 1, 1);
    }

    //TODO update and docs
    public void renderUniversalDisplay(int x, int y, double energy, int mouseX, int mouseY, Unit unit)
    {
        renderUniversalDisplay(x, y, energy, mouseX, mouseY, unit, false);
    }

    //TODO update and docs
    public void renderUniversalDisplay(int x, int y, double energy, double maxEnergy, int mouseX, int mouseY, Unit unit, boolean symbol)
    {
        String displaySuffix = "";

        if (unit == Unit.WATT)
        {
            displaySuffix = "/s";
        }

        String display = new UnitDisplay(unit, energy).symbol(symbol) + "/" + new UnitDisplay(unit, maxEnergy).symbol(symbol);

		/*
        // Check different energy system types.
		if (unit == Unit.WATT || unit == Unit.JOULES)
		{
			switch (energyType)
			{
				case 3:
					display = UnitDisplay.roundDecimals(energy * ModuleThermalExpansion$.MODULE$.ratio) + " RF" + displaySuffix + "/" + UnitDisplay.roundDecimals(maxEnergy * ModuleThermalExpansion$.MODULE$.ratio) + " RF" + displaySuffix;
					break;
			}
		}*/

        //TODO: Check if this rect works.
        if (new Rectangle(x, y, x + display.length() * 5, y + 9).isWithin(new Point(mouseX, mouseY)))
        {
            if (Mouse.isButtonDown(0) && this.lastChangeFrameTime <= 0)
            {
                energyType = (energyType + 1) % 4;
                this.lastChangeFrameTime = 60;
            }
            else
            {
                this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop + 10, "Click to change unit.");
            }
        }

        this.lastChangeFrameTime--;

        fontRendererObj.drawString(display, x, y, 4210752);
    }

    //TODO update and docs
    public void renderUniversalDisplay(int x, int y, double energy, int mouseX, int mouseY, Unit unit, boolean small)
    {
        String displaySuffix = "";

        if (unit == Unit.WATT)
        {
            displaySuffix = "/s";
        }

        String display = new UnitDisplay(unit, energy).symbol(small).toString();
/*
        // Check different energy system types.
		if (unit == Unit.WATT || unit == Unit.JOULES)
		{
			switch (energyType)
			{
				case 3:
					display = UnitDisplay.roundDecimals(energy * ModuleThermalExpansion$.MODULE$.ratio) + " RF" + displaySuffix;
					break;
			}
		}
*/
        if (new Rectangle(x, y, x + display.length() * 5, y + 9).isWithin(new Point(mouseX, mouseY)))
        {
            if (Mouse.isButtonDown(0) && this.lastChangeFrameTime <= 0)
            {
                energyType = (energyType + 1) % 4;
                this.lastChangeFrameTime = 60;
            }
            else
            {
                this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop + 10, "Click to change unit.");
            }
        }

        this.lastChangeFrameTime--;

        fontRendererObj.drawString(display, x, y, 4210752);
    }

    //TODO update and docs
    public void drawTooltip(int x, int y, String... toolTips)
    {
        if (!GuiScreen.isShiftKeyDown())
        {
            if (toolTips != null)
            {
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                int var5 = 0;
                int var6;
                int var7;

                for (var6 = 0; var6 < toolTips.length; ++var6)
                {
                    var7 = fontRendererObj.getStringWidth(toolTips[var6]);

                    if (var7 > var5)
                    {
                        var5 = var7;
                    }
                }

                var6 = x + 12;
                var7 = y - 12;

                int var9 = 8;

                if (toolTips.length > 1)
                {
                    var9 += 2 + (toolTips.length - 1) * 10;
                }

                if (this.guiTop + var7 + var9 + 6 > this.height)
                {
                    var7 = this.height - var9 - this.guiTop - 6;
                }

                this.zLevel = 300;
                int var10 = -267386864;
                this.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
                this.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
                this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
                this.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
                this.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
                int var11 = 1347420415;
                int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
                this.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
                this.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
                this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
                this.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

                for (int var13 = 0; var13 < toolTips.length; ++var13)
                {
                    String var14 = toolTips[var13];

                    fontRendererObj.drawStringWithShadow(var14, var6, var7, -1);
                    var7 += 10;
                }

                this.zLevel = 0;

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            }
        }
    }

    /**
     * Based on BuildCraft
     */
    protected void displayGauge(int j, int k, int line, int col, int width, int squaled, FluidStack liquid)
    {
        squaled -= 1;

        if (liquid == null)
        {
            return;
        }

        int start = 0;

        IIcon liquidIcon = null;
        Fluid fluid = liquid.getFluid();

        if (fluid != null && fluid.getStillIcon() != null)
        {
            liquidIcon = fluid.getStillIcon();
        }

        RenderUtility.setSpriteTexture(fluid.getSpriteNumber());

        if (liquidIcon != null)
        {
            while (true)
            {
                int x;

                if (squaled > 16)
                {
                    x = 16;
                    squaled -= 16;
                }
                else
                {
                    x = squaled;
                    squaled = 0;
                }

                this.drawTexturedModelRectFromIcon(j + col, k + line + 58 - x - start, liquidIcon, width, 16 - (16 - x));
                start = start + 16;

                if (x == 0 || squaled == 0)
                {
                    break;
                }
            }
        }
    }
}
