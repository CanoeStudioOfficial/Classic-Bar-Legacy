package tfar.classicbar.overlays.vanillaoverlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import tfar.classicbar.ModUtils;
import tfar.classicbar.classicbar.Tags;
import tfar.classicbar.compat.FoodHelper;

import static tfar.classicbar.config.ModConfig.mods;

@SideOnly(Side.CLIENT)
public class TooltipRender {
	private static ResourceLocation modIcons = new ResourceLocation(Tags.MOD_ID, "textures/icons.png");
	public static final int TOOLTIP_REAL_HEIGHT_OFFSET_BOTTOM = 3;
	public static final int TOOLTIP_REAL_HEIGHT_OFFSET_TOP = -3;
	public static final int TOOLTIP_REAL_WIDTH_OFFSET_RIGHT = 3;

	public static void init() {
		MinecraftForge.EVENT_BUS.register(new TooltipRender());
	}

	@SubscribeEvent
	public void onRenderTooltip(RenderTooltipEvent.PostText event) {
		ItemStack hoveredStack = event.getStack();
		if (hoveredStack == null || hoveredStack.isEmpty())
			return;

		boolean shouldShowTooltip = (ModUtils.isShiftKeyDown()) || mods.alwaysShowTooltip;
		if (!shouldShowTooltip)
			return;

		Minecraft mc = Minecraft.getMinecraft();
		GuiScreen gui = mc.currentScreen;

		if (gui == null)
			return;

		if (!FoodHelper.isFood(hoveredStack))
			return;

		EntityPlayer player = mc.player;
		ScaledResolution scale = new ScaledResolution(mc);
		int toolTipY = event.getY();
		int toolTipX = event.getX();
		int toolTipW = event.getWidth();
		int toolTipH = event.getHeight();

		FoodHelper.BasicFoodValues defaultFoodValues = FoodHelper.getDefaultFoodValues(hoveredStack);
		FoodHelper.BasicFoodValues modifiedFoodValues = FoodHelper.getModifiedFoodValues(hoveredStack, player);

		defaultFoodValues = FoodHelper.getFoodValuesForDisplay(defaultFoodValues, player);
		modifiedFoodValues = FoodHelper.getFoodValuesForDisplay(modifiedFoodValues, player);

		if (defaultFoodValues.equals(modifiedFoodValues) && defaultFoodValues.hunger == 0)
			return;

		int biggestHunger = Math.max(defaultFoodValues.hunger, modifiedFoodValues.hunger);
		float biggestSaturationIncrement = Math.max(defaultFoodValues.getSaturationIncrement(), modifiedFoodValues.getSaturationIncrement());

		String hungerText = String.format("%d", biggestHunger);
		String saturationText = String.format("%.1f", biggestSaturationIncrement);

		int toolTipBottomY = toolTipY + toolTipH + 1 + TOOLTIP_REAL_HEIGHT_OFFSET_BOTTOM;
		int toolTipRightX = toolTipX + toolTipW + 1 + TOOLTIP_REAL_WIDTH_OFFSET_RIGHT;

		boolean shouldDrawBelow = toolTipBottomY + 20 < scale.getScaledHeight() - 3;

		int rightX = toolTipRightX - 2;
		int leftX = rightX - (Math.max(9 + mc.fontRenderer.getStringWidth(hungerText), 9 + mc.fontRenderer.getStringWidth(saturationText))) - 4;
		int topY = (shouldDrawBelow ? toolTipBottomY : toolTipY - 20 + TOOLTIP_REAL_HEIGHT_OFFSET_TOP);
		int bottomY = topY + 22;

		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		// bg
		Gui.drawRect(leftX - 1, topY, rightX + 1, bottomY, 0xF0100010);
		Gui.drawRect(leftX, (shouldDrawBelow ? bottomY : topY - 1), rightX, (shouldDrawBelow ? bottomY + 1 : topY), 0xF0100010);
		Gui.drawRect(leftX, topY, rightX, bottomY, 0x66FFFFFF);

		// drawRect disables blending and modifies color, so reset them
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		int x = rightX - 2;
		int startX = x;
		int y = bottomY - 18;

		mc.getTextureManager().bindTexture(Gui.ICONS);

		x -= 9;
		y -= 2;
		gui.drawTexturedModalRect(x - mc.fontRenderer.getStringWidth(hungerText) - 1, y, 16, 27, 9, 9);
		gui.drawTexturedModalRect(x - mc.fontRenderer.getStringWidth(hungerText) - 1, y, 61, 27, 9, 9);

		GlStateManager.pushMatrix();
		mc.fontRenderer.drawStringWithShadow(hungerText, x - mc.fontRenderer.getStringWidth(hungerText) + 10, y, 0xFFDDDDDD);
		GlStateManager.popMatrix();

        y += 10;
		x = startX;

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(modIcons);

		x -= 9;
		gui.drawTexturedModalRect(x - mc.fontRenderer.getStringWidth(saturationText) - 1, y, 0, 0, 9, 9);

		GlStateManager.pushMatrix();
		mc.fontRenderer.drawStringWithShadow(saturationText, x - mc.fontRenderer.getStringWidth(saturationText) + 10, y, 0xFFDDDDDD);
		GlStateManager.popMatrix();

        GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		// reset to drawHoveringText state
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
	}
}