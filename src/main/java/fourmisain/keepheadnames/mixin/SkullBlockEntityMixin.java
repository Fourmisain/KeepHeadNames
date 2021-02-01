package fourmisain.keepheadnames.mixin;

import fourmisain.keepheadnames.KeepHeadNames;
import fourmisain.keepheadnames.NameSettable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Add Nameable interface to SkullBlockEntity and de/serialize the custom name */
@Mixin(SkullBlockEntity.class)
@Implements({
    @Interface(iface = Nameable.class, prefix = "nameable$"),
    @Interface(iface = NameSettable.class, prefix = "namesettable$")
})
public abstract class SkullBlockEntityMixin implements Nameable, NameSettable {

    @Unique
    Text customName;

    public Text nameable$getName() {
        if (customName != null)
            return customName;

        // Currently, this will never be returned, but this might change in the future (e.g. when MC-174496 is fixed)
        return new LiteralText("Keep Head Names conflict");
    }

    @Nullable
    public Text nameable$getCustomName() {
        return customName;
    }

    public void namesettable$setCustomName(Text customName) {
        this.customName = customName;
    }

    /** Serializes the stored display name */
    @Inject(at = @At("RETURN"), method = "toTag")
    public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (customName != null) {
            tag.putString("CustomName", Text.Serializer.toJson(customName));
        }
    }

    /** Deserializes the stored display name */
    @Inject(at = @At("HEAD"), method = "fromTag")
    public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("CustomName", 8)) {
            customName = Text.Serializer.fromJson(tag.getString("CustomName"));
        }
    }
}
