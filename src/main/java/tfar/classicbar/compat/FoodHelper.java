package tfar.classicbar.compat;

import betterwithmods.api.FeatureEnabledEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;

public class FoodHelper {
    private static String HC_HUNGER_FEATURE_KEY = "hchunger";
    public static boolean HC_HUNGER_ENABLED = false;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new FoodHelper());
    }

    @SubscribeEvent
    public void bwmFeatureEnabled(FeatureEnabledEvent event) {
        if (event.getFeature().equals(HC_HUNGER_FEATURE_KEY))
           HC_HUNGER_ENABLED = event.isEnabled();
    }

    public static class BasicFoodValues {
        public final int hunger;
        public final float saturationModifier;

        public BasicFoodValues(int hunger, float saturationModifier) {
            this.hunger = hunger;
            this.saturationModifier = saturationModifier;
        }

        public float getSaturationIncrement() {
            return hunger * saturationModifier * 2f;
        }
    }
    
    public static class BWMFoodValues extends BasicFoodValues {
        public BWMFoodValues(int hunger, float saturationModifier) {
            super(hunger, saturationModifier);
        }

        // This is actually quite variable, as BWM multiples the returned value by the
        // amount above the max hunger level that eating the food gets you to
        // but this seems like the best way to display that, I guess
        public float getSaturationIncrement() {
            return Math.min(20, saturationModifier / 3f);
        }
    }

    public static boolean isFood(ItemStack itemStack) {
        if (Loader.isModLoaded("AppleCore") || Loader.isModLoaded("applecore")) {
            return AppleCoreAPI.accessor.isFood(itemStack);
        } else {
            return itemStack.getItem() instanceof ItemFood;
        }
    }

    public static BasicFoodValues getDefaultFoodValues(ItemStack itemStack) {
        if (Loader.isModLoaded("AppleCore") || Loader.isModLoaded("applecore")) {
            FoodValues foodValues = AppleCoreAPI.accessor.getFoodValues(itemStack);
            return new BasicFoodValues(foodValues.hunger, foodValues.saturationModifier);
        } else {
            ItemFood itemFood = (ItemFood) itemStack.getItem();
            int hunger = itemFood.getHealAmount(itemStack);
            float saturationModifier = itemFood.getSaturationModifier(itemStack);
            return new BasicFoodValues(hunger, saturationModifier);
        }
    }

    public static BasicFoodValues getModifiedFoodValues(ItemStack itemStack, EntityPlayer player) {
        if (Loader.isModLoaded("AppleCore") || Loader.isModLoaded("applecore")) {
            FoodValues foodValues = AppleCoreAPI.accessor.getFoodValuesForPlayer(itemStack, player);
            return new BasicFoodValues(foodValues.hunger, foodValues.saturationModifier);
        } else {
            return getDefaultFoodValues(itemStack);
        }
    }

    public static BasicFoodValues getFoodValuesForDisplay(BasicFoodValues values, EntityPlayer player) {
        BasicFoodValues valuesForDisplay = values;
        if (Loader.isModLoaded("AppleCore") || Loader.isModLoaded("applecore")) {
            int maxHunger = AppleCoreAPI.accessor.getMaxHunger(player);
            if (maxHunger == 20)
                return values;
            float scale = 20f / maxHunger;
            valuesForDisplay = new BasicFoodValues(MathHelper.ceil((double) values.hunger * scale), values.saturationModifier);
        }
        if (HC_HUNGER_ENABLED) {
            valuesForDisplay =  new BWMFoodValues(values.hunger, values.saturationModifier);
        }
        return valuesForDisplay;
    }
}