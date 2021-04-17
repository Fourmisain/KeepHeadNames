package fourmisain.keepheadnames.mixin;

import fourmisain.keepheadnames.util.Loreable;
import fourmisain.keepheadnames.util.NameSettable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Add Nameable interface to SkullBlockEntity and de/serialize the custom name and lore tags */
@Mixin(SkullBlockEntity.class)
@Implements({
    @Interface(iface = Nameable.class, prefix = "nameable$"),
    @Interface(iface = NameSettable.class, prefix = "namesettable$", remap = Interface.Remap.NONE, unique = true),
    @Interface(iface = Loreable.class, prefix = "loreable$", remap = Interface.Remap.NONE, unique = true)
})
public abstract class SkullBlockEntityMixin implements Nameable {
    @Unique Text customName;
    @Unique ListTag lore;

    public Text nameable$getName() {
        if (customName != null)
            return customName;

        return new LiteralText("Keep Head Names conflict");
    }

    @Nullable
    public Text nameable$getCustomName() {
        return customName;
    }

    public void namesettable$setCustomName(Text customName) {
        this.customName = customName;
    }

    public ListTag loreable$getLore() {
        return lore;
    }

    public void loreable$setLore(ListTag lore) {
        this.lore = lore;
    }

    /** Serializes the stored display name and lore */
    @Inject(at = @At("RETURN"), method = "toTag")
    public void toTag(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (customName != null) {
            tag.putString("CustomName", Text.Serializer.toJson(customName));
        }

        if (lore != null) {
            tag.put("Lore", lore);
        }
    }

    /** Deserializes the stored display name and lore */
    @Inject(at = @At("HEAD"), method = "fromTag")
    public void fromTag(BlockState state, CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("CustomName", 8)) {
            customName = Text.Serializer.fromJson(tag.getString("CustomName"));
        }

        if (tag.contains("Lore", 9)) {
            lore = tag.getList("Lore", 8);
        }
    }
}
