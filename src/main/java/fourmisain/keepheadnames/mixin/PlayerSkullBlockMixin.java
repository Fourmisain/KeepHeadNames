package fourmisain.keepheadnames.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlayerSkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

import static fourmisain.keepheadnames.KeepHeadNames.LOGGER;

/** Stores the display name tag from the ItemStack inside the placed SkullBlockEntity */
@Mixin(PlayerSkullBlock.class)
public class PlayerSkullBlockMixin {

    @Inject(at = @At("TAIL"), method = "onPlaced")
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SkullBlockEntity) {
            SkullBlockEntity skullBlockEntity = (SkullBlockEntity)blockEntity;

            if (itemStack.hasTag()) {
                CompoundTag tag = itemStack.getTag();

                if (tag.contains("display", 10)) {
                    CompoundTag display = tag.getCompound("display");

                    if (display.contains("Name", 8)) {
                        Text name = Text.Serializer.fromJson(display.getString("Name"));

                        // TODO: is there no better way to access added Mixin fields?
                        try {
                            Field field = skullBlockEntity.getClass().getDeclaredField("name");
                            field.setAccessible(true);
                            field.set(skullBlockEntity, name);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            LOGGER.error("Couldn't set player head display name", e);
                        }
                    }
                }
            }
        }
    }
}
