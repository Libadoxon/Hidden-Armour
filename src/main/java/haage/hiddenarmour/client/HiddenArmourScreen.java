package haage.hiddenarmour.client;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;

public class HiddenArmourScreen extends Screen {
    private static final int PANEL_W       = 220;
    private static final int PANEL_H       = 170;
    private static final int INSET         = 10;
    private static final int SPACING       = 5;
    private static final int SEP_THICKNESS = 1;
    private static final int BUTTON_H      = 20;

    private final Screen parent; // parent screen for ModMenu integration

    private ButtonWidget armourBtn, elytraBtn, horseBtn, doneBtn;

    // Computed positions
    private int panelX, panelY;
    private int yTitle, ySep1, ySub1, yArmour, yElytra, ySep2, ySub2, yHorse, ySep3, yDone;

    /**
     * Constructor used by ModMenu, receives the parent Screen to return to.
     */
    public HiddenArmourScreen(Screen parent) {
        super(Text.translatable("screen.hiddenarmour.title"));
        this.parent = parent;
    }

    /**
     * No-arg constructor for legacy entrypoints, falls back to game screen.
     */
    public HiddenArmourScreen() {
        this(null);
    }

    @Override
    protected void init() {
        super.init();
        int cx = this.width / 2;
        int cy = this.height / 2;
        panelX = cx - PANEL_W / 2;
        panelY = cy - PANEL_H / 2;

        // Vertical layout calculations
        TextRenderer tr = this.textRenderer;
        yTitle  = panelY + 8;
        ySep1   = yTitle + tr.fontHeight + SPACING;
        ySub1   = ySep1 + SEP_THICKNESS + SPACING;
        yArmour = ySub1 + tr.fontHeight + SPACING;
        yElytra = yArmour + BUTTON_H + SPACING;
        ySep2   = yElytra + BUTTON_H + SPACING;
        ySub2   = ySep2 + SEP_THICKNESS + SPACING;
        yHorse  = ySub2 + tr.fontHeight + SPACING;
        ySep3   = yHorse + BUTTON_H + SPACING;
        yDone   = ySep3 + SEP_THICKNESS + SPACING;

        armourBtn = addDrawableChild(ButtonWidget.builder(getArmourText(), b -> toggleArmour(b))
                .dimensions(panelX + INSET, yArmour, PANEL_W - 2 * INSET, BUTTON_H)
                .build());

        elytraBtn = addDrawableChild(ButtonWidget.builder(getElytraText(), b -> toggleElytra(b))
                .dimensions(panelX + INSET, yElytra, PANEL_W - 2 * INSET, BUTTON_H)
                .build());

        horseBtn  = addDrawableChild(ButtonWidget.builder(getHorseArmorText(), b -> toggleHorse(b))
                .dimensions(panelX + INSET, yHorse, PANEL_W - 2 * INSET, BUTTON_H)
                .build());

        doneBtn   = addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b ->
                        MinecraftClient.getInstance().setScreen(parent != null ? parent : null))
                .dimensions(panelX + INSET, yDone, PANEL_W - 2 * INSET, BUTTON_H)
                .build());
    }

    private void toggleArmour(ButtonWidget b) {
        var cfg = HiddenArmourConfig.get();
        cfg.hideArmour = !cfg.hideArmour;
        HiddenArmourConfig.save();
        b.setMessage(getArmourText());
    }

    private void toggleElytra(ButtonWidget b) {
        var cfg = HiddenArmourConfig.get();
        cfg.includeElytra = !cfg.includeElytra;
        HiddenArmourConfig.save();
        b.setMessage(getElytraText());
    }

    private void toggleHorse(ButtonWidget b) {
        var cfg = HiddenArmourConfig.get();
        cfg.hideHorseArmor = !cfg.hideHorseArmor;
        HiddenArmourConfig.save();
        b.setMessage(getHorseArmorText());
    }

    private Text getArmourText() {
        return splitAndColor("button.hiddenarmour.toggleArmour.", HiddenArmourConfig.get().hideArmour);
    }

    private Text getElytraText() {
        return splitAndColor("button.hiddenarmour.toggleElytra.", HiddenArmourConfig.get().includeElytra);
    }

    private Text getHorseArmorText() {
        return splitAndColor("button.hiddenarmour.toggleHorseArmor.", HiddenArmourConfig.get().hideHorseArmor);
    }

    /**
     * Splits the translation at ": ", colors suffix green/red only.
     */
    private MutableText splitAndColor(String baseKey, boolean on) {
        String key = baseKey + (on ? "enabled" : "disabled");
        String full = Text.translatable(key).getString();
        String[] parts = full.split(": ", 2);
        String prefix = parts[0] + ": ";
        String suffix = parts.length > 1 ? parts[1] : "";
        return Text.literal(prefix)
                .append(Text.literal(suffix).formatted(on ? Formatting.GREEN : Formatting.RED));
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // no-op: disable blur behind the UI
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Draw panel background and borders
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + PANEL_H, 0xFF111111);
        int border = 0xFFFFFFFF;
        ctx.fill(panelX - 1, panelY - 1, panelX + PANEL_W + 1, panelY, border);
        ctx.fill(panelX - 1, panelY + PANEL_H, panelX + PANEL_W + 1, panelY + PANEL_H + 1, border);
        ctx.fill(panelX - 1, panelY, panelX, panelY + PANEL_H, border);
        ctx.fill(panelX + PANEL_W, panelY, panelX + PANEL_W + 1, panelY + PANEL_H, border);

        // Title and separators
        ctx.drawCenteredTextWithShadow(textRenderer, title.getString(), panelX + PANEL_W / 2, yTitle, 0xFFFFFFFF);
        ctx.fill(panelX + INSET, ySep1, panelX + PANEL_W - INSET, ySep1 + SEP_THICKNESS, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("Player Render"), panelX + PANEL_W / 2, ySub1, 0xFFFFFFFF);

        super.render(ctx, mouseX, mouseY, delta);

        ctx.fill(panelX + INSET, ySep2, panelX + PANEL_W - INSET, ySep2 + SEP_THICKNESS, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("Horse Render"), panelX + PANEL_W / 2, ySub2, 0xFFFFFFFF);
        ctx.fill(panelX + INSET, ySep3, panelX + PANEL_W - INSET, ySep3 + SEP_THICKNESS, 0xFFFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
