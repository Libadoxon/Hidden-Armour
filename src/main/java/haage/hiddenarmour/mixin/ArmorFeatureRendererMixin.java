package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    // Will be true only while rendering a player and hideArmour==true
    @Unique private boolean hiddenArmour_applyToggles = false;

    // Hook the public render(...) that fires once per-entity
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
                    + "Lnet/minecraft/client/render/VertexConsumerProvider;"
                    + "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
            at     = @At("HEAD")
    )
    private void beforeRender(
            MatrixStack matrices,
            VertexConsumerProvider vcp,
            int light,
            BipedEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci
    ) {
        // record whether this is a player AND the master‐toggle is on
        hiddenArmour_applyToggles =
                HiddenArmourConfig.get().hideArmour
                        && state instanceof PlayerEntityRenderState;
    }

    // Clear it when the feature is done
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
                    + "Lnet/minecraft/client/render/VertexConsumerProvider;"
                    + "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
            at     = @At("TAIL")
    )
    private void afterRender(
            MatrixStack matrices,
            VertexConsumerProvider vcp,
            int light,
            BipedEntityRenderState state,
            float limbAngle,
            float limbDistance,
            CallbackInfo ci
    ) {
        hiddenArmour_applyToggles = false;
    }

    // Hook the private renderArmor(...) that fires once per slot
    @Inject(
            method = "renderArmor",
            at     = @At("HEAD"),
            cancellable = true
    )
    private void onRenderArmor(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
            BipedEntityModel<?> model,
            CallbackInfo ci
    ) {
        // If we’re *not* in a player+hideArmour render, do nothing
        if (!hiddenArmour_applyToggles) return;

        var cfg = HiddenArmourConfig.get();
        // Cancel each slot based on your emoji‐toggles:
        switch (slot) {
            case HEAD:
                if (!cfg.isShowHelmet())     ci.cancel();
                break;
            case CHEST:
                if (!cfg.isShowChestplate()) ci.cancel();
                break;
            case LEGS:
                if (!cfg.isShowLeggings())   ci.cancel();
                break;
            case FEET:
                if (!cfg.isShowBoots())      ci.cancel();
                break;
            default:
                // no other slots
        }
    }
}
