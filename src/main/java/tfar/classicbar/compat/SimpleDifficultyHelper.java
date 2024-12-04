package tfar.classicbar.compat;

import net.minecraft.entity.player.EntityPlayer;
import com.charles445.simpledifficulty.api.thirst.IThirstCapability;
import com.charles445.simpledifficulty.api.SDCapabilities;

public class SimpleDifficultyHelper {
    public static IThirstCapability getHandler(EntityPlayer player) {
        return player.getCapability(SDCapabilities.THIRST, null);
    };
}
