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
        if (Loader.isModLoaded("toughasnails")) {
            String registryName = stack.getItem().getRegistryName().toString();
            if (ModConfig.drinkData.containsKey(registryName)) {
                for (DrinkData drinkData : ModConfig.drinkData.get(registryName)) {
                    if (drinkData.getPredicate().apply(stack)) {
                        drinkValue = new DrinkValue(drinkData.getThirstRestored(), drinkData.getHydrationRestored());
                        break;
                    }
                }
            }
        }
        if (drinkValue == null && Loader.isModLoaded("simpledifficulty")) {
            List<JsonConsumableThirst> consumableList = JsonConfig.consumableThirst.get(stack.getItem().getRegistryName().toString());
            if (consumableList != null) {
                for (JsonConsumableThirst jct : consumableList) {
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
            String registryName = stack.getItem().getRegistryName().toString();
            if (ModConfig.drinkData.containsKey(registryName)) {
                for (DrinkData drinkData : ModConfig.drinkData.get(registryName)) {
                    if (drinkData.getPredicate().apply(stack)) {
                        drinkValue = new DrinkValue(drinkData.getThirstRestored(), drinkData.getHydrationRestored());
                        break;
                    }
                }
            }
        }
        if (drinkValue == null && Loader.isModLoaded("simpledifficulty")) {
            List<JsonConsumableThirst> consumableList = JsonConfig.consumableThirst.get(stack.getItem().getRegistryName().toString());
            if (consumableList != null) {
                for (JsonConsumableThirst jct : consumableList) {
                    if (jct != null && jct.matches(stack)) {
                        drinkValue = new DrinkValue(jct.amount, jct.saturation);
                        break;
                    }
                }
            }
        }
        return drinkValue;
    }

    public static DrinkValue getDrinkData(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        
        DrinkValue drinkValue = null;
        if (Loader.isModLoaded("toughasnails")) {
            if (stack.getItem().equals(Items.POTIONITEM)) {
                if (!stack.hasEffect()) {
                    drinkValue = new DrinkValue(WaterType.NORMAL.getThirst(), WaterType.NORMAL.getHydration());
                } else {
                    drinkValue = new DrinkValue(4, 0.3f);
                }
            } else if (stack.getItem() instanceof ItemCanteen && stack.getItemDamage() != 0) {
                drinkValue = new DrinkValue(WaterType.NORMAL.getThirst(), WaterType.NORMAL.getHydration());
            } else if (stack.getItem() instanceof ItemDrink) {
                drinkValue = getJuiceValue(stack);
            } else {
                drinkValue = getDrinkValue(stack);
            }
        }
        
        if (drinkValue == null && Loader.isModLoaded("simpledifficulty")) {
            if (stack.getItem().equals(Items.POTIONITEM)) {
                if (!stack.hasEffect()) {
                    drinkValue = new DrinkValue(ThirstEnum.NORMAL.getThirst(), ThirstEnum.NORMAL.getSaturation());
                } else {
                    drinkValue = new DrinkValue(ThirstEnum.POTION.getThirst(), ThirstEnum.POTION.getSaturation());
                }
            } else if (stack.getItem() instanceof com.charles445.simpledifficulty.item.ItemCanteen) {
                if (stack.hasTagCompound()) {
                    int doses = stack.getTagCompound().getInteger("Doses");
                    if (doses != 0) {
                        int type = stack.getTagCompound().getInteger("CanteenType");
                        if (type == 0) {
                            drinkValue = new DrinkValue(ThirstEnum.NORMAL.getThirst(), ThirstEnum.NORMAL.getSaturation());
                        } else if (type == 1) {
                            drinkValue = new DrinkValue(ThirstEnum.SALT.getThirst(), ThirstEnum.SALT.getSaturation());
                        } else if (type == 3 || type == 4) {
                            drinkValue = new DrinkValue(ThirstEnum.PURIFIED.getThirst(), ThirstEnum.PURIFIED.getSaturation());
                        }
                    }
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