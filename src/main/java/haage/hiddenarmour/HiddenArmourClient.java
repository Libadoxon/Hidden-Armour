package haage.hiddenarmour;

import net.fabricmc.api.ClientModInitializer;
import haage.hiddenarmour.config.HiddenArmourConfig;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import org.lwjgl.glfw.GLFW;

// import our new screen
import haage.hiddenarmour.client.HiddenArmourScreen;

public class HiddenArmourClient implements ClientModInitializer {
    private static KeyBinding toggleArmorKey;
    private static KeyBinding openGuiKey;

    @Override
    public void onInitializeClient() {
        // ─── STEP 2: Load existing config (or create defaults) ─────────
        HiddenArmourConfig.get();
        // ───────────────────────────────────────────────────────────────

        // 1) Register “J” for toggling armor
        toggleArmorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hiddenarmour.toggle",    // translation key
                GLFW.GLFW_KEY_J,              // default key: J
                "category.hiddenarmour"       // translation key for the category
        ));

        // 2) Register “U” for opening our settings GUI
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hiddenarmour.openGui",   // translation key
                GLFW.GLFW_KEY_U,              // default key: U
                "category.hiddenarmour"       // same category
        ));

        // 3) Every tick, check if either was pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Toggle armour hide
            while (toggleArmorKey.wasPressed()) {
                boolean newState = !HiddenArmourConfig.get().hideArmour;
                HiddenArmourConfig.get().hideArmour = newState;
                HiddenArmourConfig.save();
                client.player.sendMessage(
                        Text.literal("Armor hidden: " + newState),
                        true
                );
            }

            // Open the settings screen
            while (openGuiKey.wasPressed()) {
                // Use MinecraftClient.getInstance() in case `client` is shadowed
                MinecraftClient.getInstance().setScreen(new HiddenArmourScreen());
            }
        });
    }
}
