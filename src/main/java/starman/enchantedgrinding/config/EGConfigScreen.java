package starman.enchantedgrinding.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EGConfigScreen {
    public static Screen createScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.enchanted_grinding.title"));

        EGConfig config = EGConfigManager.get();

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("config.enchanted_grinding.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("config.enchanted_grinding.modEnabled"),
                        config.modEnabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.enchanted_grinding.modEnabled.tooltip"))
                .setSaveConsumer(newValue -> config.modEnabled = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("config.enchanted_grinding.xpConsume"),
                        config.xpConsume)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.enchanted_grinding.xpConsume.tooltip"))
                .setSaveConsumer(newValue -> config.xpConsume = newValue)
                .build());


        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("config.enchanted_grinding.ignoreCurses"),
                        config.ignoreCurses)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("config.enchanted_grinding.ignoreCurses.tooltip"))
                .setSaveConsumer(newValue -> config.ignoreCurses = newValue)
                .build());

        builder.setSavingRunnable(EGConfigManager::save);

        return builder.build();
    }
}