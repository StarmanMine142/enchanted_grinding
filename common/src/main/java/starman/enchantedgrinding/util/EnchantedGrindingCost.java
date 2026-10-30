package starman.enchantedgrinding.util;

import starman.enchantedgrinding.config.EGConfigManager;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import it.unimi.dsi.fastutil.objects.Object2IntMap;

public class EnchantedGrindingCost {

    public static int getLevelCost(ItemStack enchantedItem) {
        if (enchantedItem.isEmpty()) return 0;

        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(enchantedItem);
        if (enchantments.isEmpty()) return 0;

        boolean skipCurses = EGConfigManager.get().ignoreCurses;
        int totalCost = 0;
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> holder = entry.getKey();
            if (skipCurses && holder.is(EnchantmentTags.CURSE)) continue;
            int level = entry.getIntValue();
            int anvilCost = holder.value().definition().anvilCost();
            totalCost += anvilCost * level;
        }
        return totalCost;
    }
}