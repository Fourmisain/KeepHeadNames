package io.github.fourmisain.keepheadnames.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import io.github.fourmisain.keepheadnames.SkullNameFix;
import net.minecraft.datafixer.Schemas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Schemas.class)
public abstract class SchemasMixin {
	@ModifyArg(
		method = "build",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/datafixer/fix/RemoveEmptyItemInSuspiciousBlockFix;<init>(Lcom/mojang/datafixers/schemas/Schema;)V"
		)
	)
	private static Schema addSkullNameFix(Schema outputSchema, @Local(argsOnly = true) DataFixerBuilder builder) {
		builder.addFixer(new SkullNameFix(outputSchema));
		return outputSchema;
	}
}
