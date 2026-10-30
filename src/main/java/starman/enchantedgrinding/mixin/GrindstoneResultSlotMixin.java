package starman.enchantedgrinding.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import starman.enchantedgrinding.compat.TaxFreeLevelsCompat;
import starman.enchantedgrinding.config.EGConfigManager;
import starman.enchantedgrinding.util.EnchantedGrindingCost;

@Mixin(targets = "net.minecraft.world.inventory.GrindstoneMenu$4")
public class GrindstoneResultSlotMixin {

    @WrapMethod(method = "onTake")
    private void eg$wrapOnTake(Player player, ItemStack stack, Operation<Void> original) {
        if (EGConfigManager.get().modEnabled
                && stack.is(Items.ENCHANTED_BOOK)
                && player.containerMenu instanceof GrindstoneMenu menu) {

            ItemStack a = menu.getSlot(0).getItem();
            ItemStack b = menu.getSlot(1).getItem();

            ItemStack enchantedItem = null;
            int enchSlot = -1;
            int bookSlot = -1;

            if (a.is(Items.BOOK) && isEnchantedNonBook(b)) {
                enchantedItem = b; enchSlot = 1; bookSlot = 0;
            } else if (b.is(Items.BOOK) && isEnchantedNonBook(a)) {
                enchantedItem = a; enchSlot = 0; bookSlot = 1;
            }

            if (enchantedItem != null) {
                if (EGConfigManager.get().xpConsume) {
                    int cost = EnchantedGrindingCost.getLevelCost(enchantedItem);
                    if (cost > 0) TaxFreeLevelsCompat.applyFlattenedCost(player, cost);
                }

                ItemStack restored = enchantedItem.copy();
                if (EGConfigManager.get().ignoreCurses) {
                    EnchantmentHelper.updateEnchantments(restored,
                            m -> m.removeIf(h -> !h.is(EnchantmentTags.CURSE)));
                } else {
                    EnchantmentHelper.updateEnchantments(restored,
                            m -> m.removeIf(h -> true));
                }

                ItemStack books = menu.getSlot(bookSlot).getItem();
                ItemStack leftBooks = books.getCount() > 1
                        ? books.copyWithCount(books.getCount() - 1)
                        : ItemStack.EMPTY;

                menu.getSlot(enchSlot).set(restored);
                menu.getSlot(bookSlot).set(leftBooks);

                player.level().levelEvent(1042, player.blockPosition(), 0);

                return;
            }
        }

        original.call(player, stack);
    }

    @Unique
    private static boolean isEnchantedNonBook(ItemStack s) {
        return !s.isEmpty()
                && !s.is(Items.BOOK)
                && !s.is(Items.ENCHANTED_BOOK)
                && !EnchantmentHelper.getEnchantmentsForCrafting(s).isEmpty();
    }
}