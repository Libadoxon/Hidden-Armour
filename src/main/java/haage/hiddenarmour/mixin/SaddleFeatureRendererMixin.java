package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaddleFeatureRenderer.class)
public class SaddleFeatureRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;"
                    + "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;"
                    + "ILnet/minecraft/client/render/entity/state/LivingEntityRenderState;"
                    + "FF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderSaddleArmor(
            MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, LivingEntityRenderState livingEntityRenderState, float f, float g,
            CallbackInfo ci
    ) {
        if (HiddenArmourConfig.get().hideHorseArmor) {
            ci.cancel();
        }
    }
}
