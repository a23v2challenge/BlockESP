package io.github.blockesp;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class BlockEspConfigScreen extends Screen {
    private final Screen parent;

    public BlockEspConfigScreen(Screen parent) {
        super(Text.translatable("blockesp.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        BlockEspConfig config = BlockEspConfig.get();
        int buttonWidth = (width - 36) / 2;
        int leftX = 12;
        int rightX = leftX + buttonWidth + 12;

        addDrawableChild(ButtonWidget.builder(enabledLabel(), button -> {
            config.setEnabled(!config.isEnabled());
            button.setMessage(enabledLabel());
        }).dimensions(leftX, 18, width - 24, 20).build());

        OreType[] ores = OreType.values();
        for (int index = 0; index < ores.length; index++) {
            OreType ore = ores[index];
            int x = (index & 1) == 0 ? leftX : rightX;
            int y = 40 + (index / 2) * 16;
            addDrawableChild(ButtonWidget.builder(oreLabel(ore), button -> {
                config.toggleOre(ore);
                button.setMessage(oreLabel(ore));
            }).dimensions(x, y, buttonWidth, 15).build());
        }

        addDrawableChild(ButtonWidget.builder(Text.translatable("blockesp.config.reset"), button -> {
            config.enableAll();
            clearAndInit();
        }).dimensions(leftX, height - 22, buttonWidth, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("blockesp.config.done"), button -> close())
                .dimensions(rightX, height - 22, buttonWidth, 20).build());
    }

    @Override
    public void close() {
        if (client != null) {
            client.setScreen(parent);
        }
    }

    private Text enabledLabel() {
        return Text.translatable("blockesp.config.enabled").append(": ")
                .append(BlockEspConfig.get().isEnabled() ? Text.literal("ON") : Text.literal("OFF"));
    }

    private Text oreLabel(OreType ore) {
        return Text.literal(ore.displayName() + ": " + (BlockEspConfig.get().isSelected(ore) ? "ON" : "OFF"));
    }
}
