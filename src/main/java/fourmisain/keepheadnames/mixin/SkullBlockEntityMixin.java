package fourmisain.keepheadnames.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** De/Serializes the stored display name */
@Mixin(SkullBlockEntity.class)
public abstract class SkullBlockEntityMixin {

    @Unique
    Text name;

    @Inject(at = @At("RETURN"), method = "toTag")
    public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (name != null) {
            CompoundTag displayTag = new CompoundTag();
            displayTag.putString("Name", Text.Serializer.toJson(name));
            tag.put("display", displayTag);
        }
    }

    @Inject(at = @At("HEAD"), method = "fromTag")
    public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("display", 10)) {
            CompoundTag display = tag.getCompound("display");

            if (display.contains("Name", 8)) {
                name = Text.Serializer.fromJson(display.getString("Name"));
            }
        }
    }
}
