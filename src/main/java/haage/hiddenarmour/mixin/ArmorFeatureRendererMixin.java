package haage.hiddenarmour.mixin;

import haage.hiddenarmour.config.HiddenArmourConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private void onRenderArmor(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            ItemStack stack,
            EquipmentSlot slot,
            int light,
            BipedEntityModel<?> model,
            CallbackInfo ci
    ) {
        // Cancel armor rendering if hideArmour flag is set
        if (HiddenArmourConfig.get().hideArmour) {
            ci.cancel();
        }
    }
}