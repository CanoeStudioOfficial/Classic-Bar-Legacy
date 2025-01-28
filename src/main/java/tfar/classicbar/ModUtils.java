package tfar.classicbar;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Keyboard;
import tfar.classicbar.classicbar.Tags;

public class ModUtils {
    public static final int rightTextOffset = 82;

    public static final int leftTextOffset = -5;

    public static ResourceLocation ICON_BAR = new ResourceLocation(Tags.MOD_ID, "textures/gui/health.png");
    public static final Minecraft mc = Minecraft.getMinecraft();
    private static final FontRenderer fontRenderer = mc.fontRenderer;

    public static void drawTexturedModalRect(float x, float y, int textureX, int textureY, int width, int height) {
        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    public static int getWidth(double d1, double d2) {
        int w = 78;
        double d3 = Math.max(w * d1 / d2, 0);
        return (int) Math.ceil(d3);
    }

    public static int getStringLength(String s) {
        return fontRenderer.getStringWidth(s);
    }

    public static void drawScaledBar(double absorb, double maxHealth, float x, float y, boolean left) {
        int i = getWidth(absorb, maxHealth);

        if (left) {
            drawTexturedModalRect(x, y, 0, 0, i + 1, 9);
            drawTexturedModalRect(x + i + 1, y, 79, 0, 2, 9);
        } else {
            drawTexturedModalRect(x + 2, y, 80 - i, 0, i + 1, 9);
            drawTexturedModalRect(x, y, 0, 0, 2, 9);
        }
    }

    public static void drawStringOnHUD(String string, int xOffset, int yOffset, int color) {
   /* double scale = numbers.numberScale;
    GlStateManager.pushMatrix();
    GlStateManager.scale(scale, scale, 1);
    xOffset /= scale;
    yOffset /= scale;
    int l = fontRenderer.getStringWidth(string);
    xOffset += (left) ? .4*l * (1 - scale) / scale : 0;
    GlStateManager.translate(16 * (1 - scale) / scale, 14 * (1 - scale) / scale, 0);*/

        xOffset += 2;
        yOffset += 2;

        fontRenderer.drawString(string, xOffset, yOffset, color, true);
    }

    public static float getExhaustion(EntityPlayer player) {
        return ObfuscationReflectionHelper.getPrivateValue(FoodStats.class, player.getFoodStats(), "field_75126_c");
    }

    public static void setExhaustion(EntityPlayer player, float exhaustion) {
        ObfuscationReflectionHelper.setPrivateValue(FoodStats.class, player.getFoodStats(), exhaustion, "field_75126_c");
    }

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    }
}