package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
                    + "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;"
                    + "I"
                    + "Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;"
                    + "FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderElytra(
            MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, BipedEntityRenderState bipedEntityRenderState, float f, float g,
            CallbackInfo ci
    ) {
        if (HiddenArmourConfig.get().hideArmour
                && HiddenArmourConfig.get().includeElytra
                && bipedEntityRenderState instanceof PlayerEntityRenderState) {
            ci.cancel();
        }
    }
}
