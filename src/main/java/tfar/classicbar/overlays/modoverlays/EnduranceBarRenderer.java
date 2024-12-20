package tfar.classicbar.overlays.modoverlays;

import com.origins_eternity.ercore.content.capability.Capabilities;
import com.origins_eternity.ercore.content.capability.endurance.IEndurance;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import tfar.classicbar.Color;
import tfar.classicbar.overlays.IBarOverlay;

import static com.origins_eternity.ercore.content.gui.Overlay.gui;
import static tfar.classicbar.ColorUtils.hex2Color;
import static tfar.classicbar.ModUtils.*;
import static tfar.classicbar.config.ModConfig.*;

public class EnduranceBarRenderer implements IBarOverlay {
    public boolean side;

    @Override
    public IBarOverlay setSide(boolean side) {
        this.side = side;
        return this;
    }

    @Override
    public boolean rightHandSide() {
        return side;
    }

    @Override
    public boolean shouldRender(EntityPlayer player) {
        return true;
    }

    @Override
    public void renderBar(EntityPlayer player, int width, int height) {
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        int value = (int) endurance.getEndurance();
        float exhaustion = endurance.getExhaustion();
        int xStart = width / 2 + 10;
        int yStart = height - getSidedOffset();
        mc.profiler.startSection("endurance");
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        Color.reset();
        drawTexturedModalRect(xStart, yStart, 0, 0, 81, 9);
        float f = xStart+79-getWidth(value,endurance.getHealth());
        hex2Color(mods.enduranceBarColor).color2Gl();
        drawTexturedModalRect(f, yStart + 1, 1, 10, getWidth(value,endurance.getHealth()), 7);
        exhaustion = Math.min(exhaustion, 1);
        f = xStart - getWidth(exhaustion, 1) + 80;
        GlStateManager.color(1, 1, 1, .25f);
        drawTexturedModalRect(f, yStart + 1, 1, 28, getWidth(exhaustion, 1), 9);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        mc.profiler.endSection();
    }

    @Override
    public boolean shouldRenderText() {
        return numbers.showEnduranceNumbers;
    }

    @Override
    public void renderText(EntityPlayer player, int width, int height) {
        int xStart = width / 2 + 10;
        int yStart = height - getSidedOffset();
        IEndurance endurance = player.getCapability(Capabilities.ENDURANCE, null);
        int value = (int) endurance.getEndurance();
        int h1 = value;
        int c = Integer.decode(mods.enduranceBarColor);
        if (numbers.showPercent)h1 = value*5;
        drawStringOnHUD(h1 + "", xStart + 9 * ((general.displayIcons) ? 1 : 0) + rightTextOffset, yStart - 1, c);
    }

    @Override
    public void renderIcon(EntityPlayer player, int width, int height) {
        int xStart = width / 2 + 10;
        int yStart = height - getSidedOffset();
        mc.getTextureManager().bindTexture(gui);
        drawTexturedModalRect(xStart + 82, yStart, 0, 0, 8,9);
        drawTexturedModalRect(xStart + 82, yStart, 0, 9, 8, 9);
    }

    @Override
    public String name() {
        return "endurance";
    }
}