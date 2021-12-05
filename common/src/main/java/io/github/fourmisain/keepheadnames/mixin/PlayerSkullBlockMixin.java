package io.github.fourmisain.keepheadnames.mixin;

import io.github.fourmisain.keepheadnames.util.NameSettable;
import io.github.fourmisain.keepheadnames.util.Loreable;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlayerSkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static io.github.fourmisain.keepheadnames.KeepHeadNames.getLore;

/** Stores the display name and lore tag from the ItemStack inside the placed SkullBlockEntity */
@Mixin(PlayerSkullBlock.class)
public abstract class PlayerSkullBlockMixin {
	@Inject(method = "onPlaced", at = @At("TAIL"))
	public void copyNameAndLoreToSkullBlock(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
		BlockEntity blockEntity = Objects.requireNonNull(world.getBlockEntity(pos));

		if (itemStack.hasCustomName()) {
			((NameSettable) blockEntity).setCustomName(itemStack.getName());
		}
		((Loreable) blockEntity).setLore(getLore(itemStack));
	}
}
