package fourmisain.keepheadnames.mixin;

import fourmisain.keepheadnames.util.Loreable;
import fourmisain.keepheadnames.util.NameSettable;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
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
    @Unique NbtList lore;

    public Text nameable$getName() {
        if (customName != null)
            return customName;

        return new LiteralText("Keep Head Names conflict");
    }

    @Nullable
    public Text nameable$getCustomName() {
        return customName;
    }

    public void namesettable$setCustomName(@Nullable Text customName) {
        this.customName = customName;
    }

    public NbtList loreable$getLore() {
        return lore;
    }

    public void loreable$setLore(@Nullable NbtList lore) {
        this.lore = lore;
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    public void writeNameAndLoreNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (customName != null) {
            nbt.putString("CustomName", Text.Serializer.toJson(customName));
        }

        if (lore != null) {
            nbt.put("Lore", lore);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    public void readNameAndLoreNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CustomName", NbtElement.STRING_TYPE)) {
            customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }

        if (nbt.contains("Lore", NbtElement.LIST_TYPE)) {
            lore = nbt.getList("Lore", NbtElement.STRING_TYPE);
        }
    }
}
