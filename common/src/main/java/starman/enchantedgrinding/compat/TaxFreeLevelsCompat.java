package starman.enchantedgrinding.compat;

import net.minecraft.world.entity.player.Player;

public final class TaxFreeLevelsCompat {
    private static final String MOD_ID = "taxfreelevels";

    private TaxFreeLevelsCompat() {}

    public static boolean isActive() {
        return EGPlatform.isModLoaded(MOD_ID);
    }

    public static void applyFlattenedCost(Player player, int levelCost) {
        if (!EGPlatform.isModLoaded(MOD_ID)) {
            player.giveExperienceLevels(-levelCost);
            return;
        }
        TaxFreeLevelsImpl.apply(player, levelCost);
    }
}