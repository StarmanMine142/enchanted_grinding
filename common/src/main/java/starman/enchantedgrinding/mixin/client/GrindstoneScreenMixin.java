package starman.enchantedgrinding.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import starman.enchantedgrinding.config.EGConfigManager;
import starman.enchantedgrinding.util.EnchantedGrindingCost;

@Mixin(GrindstoneScreen.class)
public class GrindstoneScreenMixin {

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void enchantedGrinding$extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (!EGConfigManager.get().modEnabled || !EGConfigManager.get().xpConsume) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        GrindstoneScreen screen = (GrindstoneScreen)(Object) this;
        GrindstoneMenu menu = screen.getMenu();

        ItemStack result = menu.getSlot(2).getItem();
        if (!result.is(Items.ENCHANTED_BOOK)) return;

        ItemStack slotA = menu.getSlot(0).getItem();
        ItemStack slotB = menu.getSlot(1).getItem();

        ItemStack enchantedItem = null;
        if (slotA.is(Items.BOOK) && isEnchantedNonBook(slotB)) enchantedItem = slotB;
        else if (slotB.is(Items.BOOK) && isEnchantedNonBook(slotA)) enchantedItem = slotA;
        if (enchantedItem == null) return;

        int cost = EnchantedGrindingCost.getLevelCost(enchantedItem);
        if (cost <= 0) return;

        Component text = Component.translatable("container.repair.cost", cost);
        int color = 0xFF80FF20;
        if (!player.hasInfiniteMaterials() && player.experienceLevel < cost) {
            color = 0xFFFF6060;
        }

        int x = screen.width / 2 - 17;
        int y = screen.height / 2 - 10;

        int width = Minecraft.getInstance().font.width(text);
        graphics.fill(x - 2, y - 2, x + width + 2, y + 10, 1325400064);
        graphics.text(Minecraft.getInstance().font, text, x, y, color, true);
    }

    @Unique
    private static boolean isEnchantedNonBook(ItemStack s) {
        return !s.isEmpty()
                && !s.is(Items.BOOK)
                && !s.is(Items.ENCHANTED_BOOK)
                && !EnchantmentHelper.getEnchantmentsForCrafting(s).isEmpty();
    }
}