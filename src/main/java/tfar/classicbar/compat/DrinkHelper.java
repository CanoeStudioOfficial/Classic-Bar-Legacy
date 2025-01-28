package tfar.classicbar.compat;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import toughasnails.api.item.ItemDrink;
import toughasnails.api.thirst.WaterType;
import toughasnails.config.json.DrinkData;
import toughasnails.init.ModConfig;
import toughasnails.item.ItemCanteen;
import toughasnails.item.ItemFruitJuice;

public class DrinkHelper {
    public static boolean isDrink(ItemStack stack) {
        return Loader.isModLoaded("toughasnails") && (stack.getItem().equals(Items.POTIONITEM) || stack.getItem() instanceof ItemDrink || getDrinkData(stack) != null);
    }

    private static DrinkData getDrinkData(ItemStack stack) {
        DrinkData data = null;
        String registryName = stack.getItem().getRegistryName().toString();
        if (ModConfig.drinkData.containsKey(registryName)) {
            for (DrinkData drinkData : ModConfig.drinkData.get(registryName)) {
                if (drinkData.getPredicate().apply(stack)) {
                    data = drinkData;
                    break;
                }
            }
        }
        return data;
    }

    private static int getThirst(ItemStack stack) {
        int thirst = 0;
        switch (stack.getTranslationKey()) {
            case "item.purified_water_bottle":
                thirst = WaterType.PURIFIED.getThirst();
                break;
            case "item.juice_apple":
                thirst = ItemFruitJuice.JuiceType.APPLE.getThirst();
                break;
            case "item.juice_beetroot":
                thirst = ItemFruitJuice.JuiceType.BEETROOT.getThirst();
                break;
            case "item.juice_cactus":
                thirst = ItemFruitJuice.JuiceType.CACTUS.getThirst();
                break;
            case "item.juice_carrot":
                thirst = ItemFruitJuice.JuiceType.CARROT.getThirst();
                break;
            case "item.juice_chorus_fruit":
                thirst = ItemFruitJuice.JuiceType.CHORUS_FRUIT.getThirst();
                break;
            case "item.juice_glistering_melon":
                thirst = ItemFruitJuice.JuiceType.GLISTERING_MELON.getThirst();
                break;
            case "item.juice_golden_apple":
                thirst = ItemFruitJuice.JuiceType.GOLDEN_APPLE.getThirst();
                break;
            case "item.juice_golden_carrot":
                thirst = ItemFruitJuice.JuiceType.GOLDEN_CARROT.getThirst();
                break;
            case "item.juice_melon":
                thirst = ItemFruitJuice.JuiceType.MELON.getThirst();
                break;
            case "item.juice_pumpkin":
                thirst = ItemFruitJuice.JuiceType.PUMPKIN.getThirst();
                break;
        }
        return thirst;
    }

    private static float getHydration(ItemStack stack) {
        float hydration = 0;
        switch (stack.getTranslationKey()) {
            case "item.purified_water_bottle":
                hydration = WaterType.PURIFIED.getHydration();
                break;
            case "item.juice_apple":
                hydration = ItemFruitJuice.JuiceType.APPLE.getHydration();
                break;
            case "item.juice_beetroot":
                hydration = ItemFruitJuice.JuiceType.BEETROOT.getHydration();
                break;
            case "item.juice_cactus":
                hydration = ItemFruitJuice.JuiceType.CACTUS.getHydration();
                break;
            case "item.juice_carrot":
                hydration = ItemFruitJuice.JuiceType.CARROT.getHydration();
                break;
            case "item.juice_chorus_fruit":
                hydration = ItemFruitJuice.JuiceType.CHORUS_FRUIT.getHydration();
                break;
            case "item.juice_glistering_melon":
                hydration = ItemFruitJuice.JuiceType.GLISTERING_MELON.getHydration();
                break;
            case "item.juice_golden_apple":
                hydration = ItemFruitJuice.JuiceType.GOLDEN_APPLE.getHydration();
                break;
            case "item.juice_golden_carrot":
                hydration = ItemFruitJuice.JuiceType.GOLDEN_CARROT.getHydration();
                break;
            case "item.juice_melon":
                hydration = ItemFruitJuice.JuiceType.MELON.getHydration();
                break;
            case "item.juice_pumpkin":
                hydration = ItemFruitJuice.JuiceType.PUMPKIN.getHydration();
                break;
        }
        return hydration;
    }

    public static int getDrinkThirst(ItemStack stack) {
        int thirst;
        if (stack.getItem().equals(Items.POTIONITEM)) {
            ItemStack piston = stack;
            if (!piston.hasEffect()) {
                thirst = WaterType.NORMAL.getThirst();
            } else {
                thirst = 4;
            }
        } else if (stack.getItem() instanceof ItemDrink) {
            if (stack.getItem() instanceof ItemCanteen) {
                thirst = WaterType.NORMAL.getThirst();
            } else {
                thirst = getThirst(stack);
            }
        } else {
            thirst = getDrinkData(stack).getThirstRestored();
        }
        return thirst;
    }

    public static float getDrinkHydration(ItemStack stack) {
        float hydration;
        if (stack.getItem().equals(Items.POTIONITEM)) {
            if (!stack.hasEffect()) {
                hydration = WaterType.NORMAL.getHydration();
            } else {
                hydration = 0.3f;
            }
        } else if (stack.getItem() instanceof ItemDrink) {
            if (stack.getItem() instanceof ItemCanteen) {
                hydration = WaterType.NORMAL.getHydration();
            } else {
                hydration = getHydration(stack);
            }
        } else {
            hydration = getDrinkData(stack).getHydrationRestored();
        }
        return hydration;
    }
}
