package net.greenjab.fixedminecraft.mixin.beacon;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BeaconBlockEntity;
//import net.minecraft.entity.effect.SaturationStatusEffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;

@Mixin(StatusEffectInstance.class)
public class StatusEffectInstanceMixin {

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;canApplyUpdateEffect(II)Z"))
    private boolean injected(StatusEffect instance, int duration, int amplifier) {
        StatusEffectInstance SEI = (StatusEffectInstance)(Object)this;
        if (instance.toString().contains("Saturation")) {
            int i = 100 >> amplifier;
            if (i > 0) {
                return duration % i == 0;
            } else {
                return true;
            }
        } else {
            return SEI.getEffectType().canApplyUpdateEffect(duration, amplifier);
        }
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;applyUpdateEffect(Lnet/minecraft/entity/LivingEntity;I)V"))
    private void injected2(StatusEffect instance, LivingEntity entity, int amplifier) {
        StatusEffectInstance SEI = (StatusEffectInstance)(Object)this;
        boolean b = instance.toString().contains("Saturation");
        if (b) {
            if (!entity.getWorld().isClient && entity instanceof PlayerEntity playerEntity) {
                playerEntity.getHungerManager().add(+ 1, 0.0F);
            }
        } else {
            SEI.getEffectType().applyUpdateEffect(entity,amplifier);
        }
    }
}
