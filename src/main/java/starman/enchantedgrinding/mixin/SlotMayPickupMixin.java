package starman.enchantedgrinding.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import starman.enchantedgrinding.config.EGConfigManager;

@Mixin(Slot.class)
public abstract class SlotMayPickupMixin {

    @Shadow public int index;
    @Shadow @Final public Container container;
    @Shadow public abstract ItemStack getItem();

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void enchantedGrinding$mayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (!EGConfigManager.get().modEnabled || !EGConfigManager.get().xpConsume) return;
        if (this.index != 2) return;
        if (!(this.container instanceof ResultContainer)) return;

        ItemStack item = this.getItem();
        if (!item.is(Items.ENCHANTED_BOOK)) return;

        CustomData data = item.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;

        int cost = data.copyTag().getInt("enchanted_grinding_cost");
        if (cost > 0 && player.experienceLevel < cost) {
            cir.setReturnValue(false);
        }
    }
}