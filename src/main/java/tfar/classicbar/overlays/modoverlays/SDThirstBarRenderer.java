package tfar.classicbar.overlays.modoverlays;

import com.charles445.simpledifficulty.api.SDCapabilities;
import com.charles445.simpledifficulty.api.SDPotions;
import com.charles445.simpledifficulty.api.config.QuickConfig;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import tfar.classicbar.Color;
import tfar.classicbar.overlays.IBarOverlay;

import static com.charles445.simpledifficulty.client.gui.ThirstGui.THIRSTHUD;
import static tfar.classicbar.ColorUtils.hex2Color;
import static tfar.classicbar.ModUtils.*;
import static tfar.classicbar.config.ModConfig.*;

public class SDThirstBarRenderer implements IBarOverlay {
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
        return QuickConfig.isThirstEnabled();
    }

    @Override
    public void renderBar(EntityPlayer player, int width, int height) {

        IThirstCapability thirstStats = player.getCapability(SDCapabilities.THIRST, null);
        int thirst = thirstStats.getThirstLevel();
        float hydration = thirstStats.getThirstSaturation();
        float thirstExhaustion = thirstStats.getThirstExhaustion();

        //Push to avoid lasting changes

        int xStart = width / 2 + 10;
        int yStart = height - getSidedOffset();

        mc.profiler.startSection("thirst");

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        boolean dehydration = player.isPotionActive(SDPotions.thirsty);

        //Bar background
        Color.reset();
        drawTexturedModalRect(xStart, yStart, 0, 0, 81, 9);

        //draw portion of bar based on thirst amount

        float f = xStart+79-getWidth(thirst,20);
        hex2Color((dehydration) ? mods.deHydrationBarColor : mods.thirstBarColor).color2Gl();
        drawTexturedModalRect(f, yStart + 1, 1, 10, getWidth(thirst,20), 7);



        //draw hydration if present
        if (hydration>0){
            f = xStart + 79 - getWidth(hydration, 20);
            hex2Color((dehydration) ? mods.deHydrationSecondaryBarColor : mods.hydrationBarColor).color2Gl();
            drawTexturedModalRect(f, yStart + 1, 1, 10, getWidth(hydration,20), 7);
        }

        if (true/*general.overlayorder.hunger.showExhaustionOverlay*/) {
            thirstExhaustion = Math.min(thirstExhaustion,4);
            f = xStart - getWidth(thirstExhaustion, 4) + 80;
            //draw exhaustion
            GlStateManager.color(1, 1, 1, .25f);
            drawTexturedModalRect(f, yStart + 1, 1, 28, getWidth(thirstExhaustion, 4), 9);
        }
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        mc.profiler.endSection();
    }

    @Override
    public boolean shouldRenderText() {
        return numbers.showThirstNumbers;
    }

    @Override
    public void renderText(EntityPlayer player, int width, int height) {
        int xStart = width / 2 + 10;
        int yStart = height - getSidedOffset();
        boolean dehydration = player.isPotionActive(SDPotions.thirsty);
        IThirstCapability thirstStats = player.getCapability(SDCapabilities.THIRST, null);
        int thirst = thirstStats.getThirstLevel();
        int h1 = thirst;
        int c = Integer.decode((dehydration) ? mods.deHydrationBarColor : mods.thirstBarColor);
        if (numbers.showPercent)h1 = thirst*5;
        drawStringOnHUD(h1 + "", xStart + 9 * ((general.displayIcons) ? 1 : 0) + rightTextOffset, yStart - 1, c);
    }

    @Override
    public void renderIcon(EntityPlayer player, int width, int height) {
        int xStart = width / 2 + 10;
        int yStart = height - getSidedOffset();
        //Draw thirst icon
        int backgroundOffset = 0;
        int iconIndex = 0;
        if(player.isPotionActive(SDPotions.thirsty)) {
            iconIndex += 4;
            backgroundOffset += 117;
        }
        mc.getTextureManager().bindTexture(THIRSTHUD);

        drawTexturedModalRect(xStart + 82, yStart, backgroundOffset, 9, 9,9);
        drawTexturedModalRect(xStart + 82, yStart, (iconIndex + 4) * 9, 9, 9, 9);
    }

    @Override
    public String name() {
        return "thirst";
    }

}
