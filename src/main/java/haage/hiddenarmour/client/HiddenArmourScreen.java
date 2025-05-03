package haage.hiddenarmour.client;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class HiddenArmourScreen extends Screen {
    private ButtonWidget armourBtn;
    private ButtonWidget elytraBtn;
    private ButtonWidget doneBtn;

    public HiddenArmourScreen() {
        super(Text.translatable("screen.hiddenarmour.title"));
    }

    @Override
    protected void init() {
        super.init(); // always call super

        int cx = this.width  / 2;
        int cy = this.height / 2;

        // Armour toggle
        armourBtn = addDrawableChild(ButtonWidget.builder(
                        getArmourText(),
                        btn -> {
                            var cfg = HiddenArmourConfig.get();
                            cfg.hideArmour = !cfg.hideArmour;
                            HiddenArmourConfig.save();
                            btn.setMessage(getArmourText());
                        })
                .dimensions(cx - 100, cy - 20, 200, 20)
                .build()
        );

        // Elytra toggle
        elytraBtn = addDrawableChild(ButtonWidget.builder(
                        getElytraText(),
                        btn -> {
                            var cfg = HiddenArmourConfig.get();
                            cfg.includeElytra = !cfg.includeElytra;
                            HiddenArmourConfig.save();
                            btn.setMessage(getElytraText());
                        })
                .dimensions(cx - 100, cy + 5, 200, 20)
                .build()
        );

        // Done
        doneBtn = addDrawableChild(ButtonWidget.builder(
                                Text.translatable("gui.done"),
                                btn -> MinecraftClient.getInstance().setScreen(null)
                        )
                        .dimensions(cx - 100, cy + 40, 200, 20)
                        .build()
        );
    }

    private Text getArmourText() {
        return Text.translatable(
                "button.hiddenarmour.toggleArmour." +
                        (HiddenArmourConfig.get().hideArmour ? "enabled" : "disabled")
        );
    }

    private Text getElytraText() {
        return Text.translatable(
                "button.hiddenarmour.toggleElytra." +
                        (HiddenArmourConfig.get().includeElytra ? "enabled" : "disabled")
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title.getString(),
                this.width  / 2,
                this.height / 2 - 50,
                0xFFFFFF
        );
        super.render(context, mouseX, mouseY, delta);
    }


    @Override
    public boolean shouldPause() {
        return false;
    }
}
