package starman.enchantedgrinding.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import starman.enchantedgrinding.config.EGConfigManager;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {

	@Inject(method = "computeResult", at = @At("HEAD"), cancellable = true)
	private void enchantedGrinding$computeResult(ItemStack a, ItemStack b, CallbackInfoReturnable<ItemStack> cir) {
		if (!EGConfigManager.get().modEnabled) return;

		ItemEnchantments ea = EnchantmentHelper.getEnchantmentsForCrafting(a);
		ItemEnchantments eb = EnchantmentHelper.getEnchantmentsForCrafting(b);

		ItemStack enchantedItem;
		ItemEnchantments enchantments;

		if (a.is(Items.BOOK) && !eb.isEmpty() && !b.is(Items.ENCHANTED_BOOK)) {
			enchantedItem = b;
			enchantments = eb;
		} else if (b.is(Items.BOOK) && !ea.isEmpty() && !a.is(Items.ENCHANTED_BOOK)) {
			enchantedItem = a;
			enchantments = ea;
		} else {
			return;
		}

		if (enchantedItem.getCount() != 1) return;

		boolean skipCurses = EGConfigManager.get().ignoreCurses;

		ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
		EnchantmentHelper.updateEnchantments(result, mutable -> {
			for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
				Holder<Enchantment> holder = entry.getKey();
				if (skipCurses && holder.is(EnchantmentTags.CURSE)) continue;
				mutable.upgrade(holder, entry.getIntValue());
			}
		});

		if (EnchantmentHelper.getEnchantmentsForCrafting(result).isEmpty()) return;

		cir.setReturnValue(result);
	}
}