package net.greenjab.fixedminecraft.mixin.mobs;

import net.greenjab.fixedminecraft.util.mobs.ArmorTrimmer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Belongs to Feature: Mobs have a chance to spawn with randomly trimmed armor
    @ModifyArg(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"), index = 1)
    public ItemStack trimArmor(ItemStack stack) {
        return ArmorTrimmer.trimAtChanceIfTrimable(stack, this.random, this.getWorld().getRegistryManager());
    }


    // Feature: Mobs can spawn wearing armor of different materials
    @ModifyArg(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getEquipmentForSlot(Lnet/minecraft/entity/EquipmentSlot;I)Lnet/minecraft/item/Item;"), index = 1)
    private int randomizeEquipmentLevel(int equipmentLevel) {
        equipmentLevel = 1;
        for (int i=0; i<3; i++) {
            if (random.nextFloat() < 0.095F) {
                ++equipmentLevel;
            }
        }
        return equipmentLevel;
    }
}
