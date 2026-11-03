package starman.enchantedgrinding.config;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class EGConfigScreen extends Screen {
    private static final String PREFIX = "config.enchanted_grinding.";

    private final Screen parent;
    private final EGConfig config = EGConfigManager.get();

    private final boolean initialModEnabled = config.modEnabled;
    private final boolean initialXpConsume = config.xpConsume;
    private final boolean initialIgnoreCurses = config.ignoreCurses;

    private boolean hasUnsavedChanges = false;
    private boolean showWarning = false;

    protected EGConfigScreen(Screen parent) {
        super(Component.translatable(PREFIX + "title"));
        this.parent = parent;
    }

    public static Screen createScreen(Screen parent) {
        return new EGConfigScreen(parent);
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 2 - 60;

        toggleButton(x, y, "modEnabled", () -> config.modEnabled, v -> config.modEnabled = v);
        toggleButton(x, y + 24, "xpConsume", () -> config.xpConsume, v -> config.xpConsume = v);
        toggleButton(x, y + 48, "ignoreCurses", () -> config.ignoreCurses, v -> config.ignoreCurses = v);

        this.addRenderableWidget(Button.builder(
                        Component.translatable(PREFIX + "saveAndQuit"), b -> {
                            EGConfigManager.save();
                            this.hasUnsavedChanges = false;
                            this.showWarning = false;
                            this.minecraft.setScreen(parent);
                        })
                .pos((this.width - 200) / 2, this.height - 26).size(200, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        graphics.centeredText(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);

        if (this.showWarning) {
            graphics.centeredText(this.font,
                    Component.translatable(PREFIX + "pleaseSave").withStyle(ChatFormatting.RED),
                    this.width / 2, this.height - 26 - 14, 0xFFFF5555);
        }
    }

    private void toggleButton(int x, int y, String key, BooleanSupplier getter, Consumer<Boolean> setter) {
        Component status = getter.getAsBoolean()
                ? Component.translatable("options.on")
                : Component.translatable("options.off");

        Button button = Button.builder(
                        Component.translatable(PREFIX + key).append(": ").append(status),
                        b -> {
                            setter.accept(!getter.getAsBoolean());
                            this.markChanged();
                        })
                .pos(x, y).size(200, 20).build();

        button.setTooltip(Tooltip.create(Component.translatable(PREFIX + key + ".tooltip")));
        this.addRenderableWidget(button);
    }

    private void markChanged() {
        this.hasUnsavedChanges = true;
        this.showWarning = false;
        this.clearWidgets();
        this.init();
    }

    @Override
    public void onClose() {
        if (this.hasUnsavedChanges && !this.showWarning) {
            this.showWarning = true;
            return;
        }
        if (this.hasUnsavedChanges) {
            config.modEnabled = this.initialModEnabled;
            config.xpConsume = this.initialXpConsume;
            config.ignoreCurses = this.initialIgnoreCurses;
        }
        this.minecraft.setScreen(parent);
    }
}