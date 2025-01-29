package tfar.classicbar.compat;

import com.charles445.simpledifficulty.api.config.JsonConfig;
import com.charles445.simpledifficulty.api.config.json.JsonConsumableThirst;
import com.charles445.simpledifficulty.api.thirst.ThirstEnum;
import com.charles445.simpledifficulty.item.ItemDrinkBase;
import com.charles445.simpledifficulty.item.ItemJuice;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import toughasnails.api.item.ItemDrink;
import toughasnails.api.thirst.WaterType;
import toughasnails.config.json.DrinkData;
import toughasnails.init.ModConfig;
import toughasnails.item.ItemCanteen;
import toughasnails.item.ItemFruitJuice;

import java.util.List;

public class DrinkHelper {
    public static boolean isDrink(ItemStack stack) {
        return (Loader.isModLoaded("toughasnails") || Loader.isModLoaded("simpledifficulty")) && getDrinkData(stack) != null;
    }

    public static class DrinkValue {
        public final int thirst;
        public final float hydration;

        public DrinkValue(int thirst, float hydration) {
            this.thirst = thirst;
            this.hydration = hydration;
        }
    }

    private static DrinkValue getDrinkValue(ItemStack stack) {
        DrinkValue drinkValue = null;
        if (Loader.isModLoaded("touoghasnails")) {
            String registryName = stack.getItem().getRegistryName().toString();
            if (ModConfig.drinkData.containsKey(registryName)) {
                for (DrinkData drinkData : ModConfig.drinkData.get(registryName)) {
                    if (drinkData.getPredicate().apply(stack)) {
                        drinkValue =  new DrinkValue(drinkData.getThirstRestored(), drinkData.getHydrationRestored());
                        break;
                    }
                }
            }
        } else if (Loader.isModLoaded("simpledifficulty")) {
            List<JsonConsumableThirst> consumableList = JsonConfig.consumableThirst.get(stack.getItem().getRegistryName().toString());
            if (consumableList != null) {
                for(JsonConsumableThirst jct : consumableList) {
                    if (jct != null && jct.matches(stack)) {
                        drinkValue = new DrinkValue(jct.amount, jct.saturation);
                        break;
                    }
                }
            }
        }
        return drinkValue;
    }

    private static DrinkValue getJuiceValue(ItemStack stack) {
        DrinkValue drinkValue = null;
        if (Loader.isModLoaded("toughasnails")) {
            switch (stack.getTranslationKey()) {
                case "item.purified_water_bottle":
                    drinkValue = new DrinkValue(WaterType.PURIFIED.getThirst(), WaterType.PURIFIED.getHydration());
                    break;
                case "item.juice_apple":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.APPLE.getThirst(), ItemFruitJuice.JuiceType.APPLE.getHydration());
                    break;
                case "item.juice_beetroot":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.BEETROOT.getThirst(), ItemFruitJuice.JuiceType.BEETROOT.getHydration());
                    break;
                case "item.juice_cactus":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.CACTUS.getThirst(), ItemFruitJuice.JuiceType.CACTUS.getHydration());
                    break;
                case "item.juice_carrot":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.CARROT.getThirst(), ItemFruitJuice.JuiceType.CARROT.getHydration());
                    break;
                case "item.juice_chorus_fruit":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.CHORUS_FRUIT.getThirst(), ItemFruitJuice.JuiceType.CHORUS_FRUIT.getHydration());
                    break;
                case "item.juice_glistering_melon":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.GLISTERING_MELON.getThirst(), ItemFruitJuice.JuiceType.GLISTERING_MELON.getHydration());
                    break;
                case "item.juice_golden_apple":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.GOLDEN_APPLE.getThirst(), ItemFruitJuice.JuiceType.GOLDEN_APPLE.getHydration());
                    break;
                case "item.juice_golden_carrot":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.GOLDEN_CARROT.getThirst(), ItemFruitJuice.JuiceType.GOLDEN_CARROT.getHydration());
                    break;
                case "item.juice_melon":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.MELON.getThirst(), ItemFruitJuice.JuiceType.MELON.getHydration());
                    break;
                case "item.juice_pumpkin":
                    drinkValue = new DrinkValue(ItemFruitJuice.JuiceType.PUMPKIN.getThirst(), ItemFruitJuice.JuiceType.PUMPKIN.getHydration());
                    break;
            }
        } else if (Loader.isModLoaded("simpledifficulty")) {
            switch (stack.getTranslationKey()) {
                case "item.simpledifficulty:purified_water_bottle":
                    drinkValue = new DrinkValue(ThirstEnum.PURIFIED.getThirst(), ThirstEnum.PURIFIED.getSaturation());
                    break;
                case "item.simpledifficulty:juice_apple":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.APPLE.getThirstLevel(), ItemJuice.JuiceEnum.APPLE.getSaturation());
                    break;
                case "item.simpledifficulty:juice_beetroot":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.BEETROOT.getThirstLevel(), ItemJuice.JuiceEnum.BEETROOT.getSaturation());
                    break;
                case "item.simpledifficulty:juice_cactus":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.CACTUS.getThirstLevel(), ItemJuice.JuiceEnum.CACTUS.getSaturation());
                    break;
                case "item.simpledifficulty:juice_carrot":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.CARROT.getThirstLevel(), ItemJuice.JuiceEnum.CARROT.getSaturation());
                    break;
                case "item.simpledifficulty:juice_chorus_fruit":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.CHORUS_FRUIT.getThirstLevel(), ItemJuice.JuiceEnum.CHORUS_FRUIT.getSaturation());
                    break;
                case "item.simpledifficulty:juice_glistering_melon":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.GOLDEN_MELON.getThirstLevel(), ItemJuice.JuiceEnum.GOLDEN_MELON.getSaturation());
                    break;
                case "item.simpledifficulty:juice_golden_apple":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.GOLDEN_APPLE.getThirstLevel(), ItemJuice.JuiceEnum.GOLDEN_APPLE.getSaturation());
                    break;
                case "item.simpledifficulty:juice_golden_carrot":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.GOLDEN_CARROT.getThirstLevel(), ItemJuice.JuiceEnum.GOLDEN_CARROT.getSaturation());
                    break;
                case "item.simpledifficulty:juice_melon":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.MELON.getThirstLevel(), ItemJuice.JuiceEnum.MELON.getSaturation());
                    break;
                case "item.simpledifficulty:juice_pumpkin":
                    drinkValue = new DrinkValue(ItemJuice.JuiceEnum.PUMPKIN.getThirstLevel(), ItemJuice.JuiceEnum.PUMPKIN.getSaturation());
                    break;
                case "item.simpledifficulty:salt_water_bottle":
                    drinkValue = new DrinkValue(ThirstEnum.SALT.getThirst(), ThirstEnum.SALT.getSaturation());
            }
        }
        return drinkValue;
    }

    public static DrinkValue getDrinkData(ItemStack stack) {
        DrinkValue drinkValue = null;
        if (Loader.isModLoaded("toughasnails")) {
            if (stack.getItem().equals(Items.POTIONITEM)) {
                if (!stack.hasEffect()) {
                    drinkValue = new DrinkValue(WaterType.NORMAL.getThirst(), WaterType.NORMAL.getHydration());
                } else {
                    drinkValue = new DrinkValue(4, 0.3f);
                }
            } else if (stack.getItem() instanceof ItemCanteen && stack.getItemDamage() !=0) {
                drinkValue = new DrinkValue(WaterType.NORMAL.getThirst(), WaterType.NORMAL.getHydration());
            } else if (stack.getItem() instanceof ItemDrink) {
                drinkValue = getJuiceValue(stack);
            } else {
                drinkValue = getDrinkValue(stack);
            }
        } else if (Loader.isModLoaded("simpledifficulty")) {
            if (stack.getItem().equals(Items.POTIONITEM)) {
                if (!stack.hasEffect()) {
                    drinkValue = new DrinkValue(ThirstEnum.NORMAL.getThirst(), ThirstEnum.NORMAL.getSaturation());
                } else {
                    drinkValue = new DrinkValue(ThirstEnum.POTION.getThirst(), ThirstEnum.POTION.getSaturation());
                }
            } else if (stack.getItem() instanceof com.charles445.simpledifficulty.item.ItemCanteen && stack.getTagCompound().getInteger("Doses") != 0) {
                int type = stack.getTagCompound().getInteger("CanteenType");
                if (type == 0) {
                    drinkValue = new DrinkValue(ThirstEnum.NORMAL.getThirst(), ThirstEnum.NORMAL.getSaturation());
                } else if (type == 3) {
                    drinkValue = new DrinkValue(ThirstEnum.PURIFIED.getThirst(), ThirstEnum.PURIFIED.getSaturation());
                }
            } else if (stack.getItem() instanceof ItemDrinkBase) {
                drinkValue = getJuiceValue(stack);
            } else {
                drinkValue = getDrinkValue(stack);
            }
        }
        return drinkValue;
    }
}