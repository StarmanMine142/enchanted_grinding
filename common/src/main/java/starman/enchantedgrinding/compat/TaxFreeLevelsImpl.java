package starman.enchantedgrinding.compat;

import io.github.fourmisain.taxfreelevels.TaxFreeLevels;
import net.minecraft.world.entity.player.Player;

final class TaxFreeLevelsImpl {
    private TaxFreeLevelsImpl() {}

    static void apply(Player player, int levelCost) {
        TaxFreeLevels.applyFlattenedXpCost(player, levelCost);
    }
}