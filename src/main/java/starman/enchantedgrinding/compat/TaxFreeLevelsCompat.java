package starman.enchantedgrinding.compat;

import io.github.fourmisain.taxfreelevels.TaxFreeLevels;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.player.Player;

public final class TaxFreeLevelsCompat {

    private static final boolean LOADED = FabricLoader.getInstance().isModLoaded("taxfreelevels");

    private TaxFreeLevelsCompat() {}

    public static boolean isActive() {
        return LOADED;
    }

    public static void applyFlattenedCost(Player player, int levelCost) {
        if (!LOADED) {
            player.giveExperienceLevels(-levelCost);
            return;
        }
        TaxFreeLevels.applyFlattenedXpCost(player, levelCost);
    }
}