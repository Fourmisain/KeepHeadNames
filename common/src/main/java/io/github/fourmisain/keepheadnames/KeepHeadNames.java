package io.github.fourmisain.keepheadnames;

import io.github.fourmisain.keepheadnames.util.CopyLoreLootFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class KeepHeadNames {
	public static final String MOD_ID = "keepheadnames";

	public static LootFunctionType COPY_LORE = new LootFunctionType(new CopyLoreLootFunction.Serializer());

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Nullable
	public static NbtList getLore(ItemStack itemStack) {
		NbtCompound displayTag = itemStack.getSubNbt("display");

		if (displayTag != null && displayTag.contains("Lore", NbtElement.LIST_TYPE)) {
			return displayTag.getList("Lore", NbtElement.STRING_TYPE);
		}

		return null;
	}

	public static void setLore(ItemStack itemStack, @Nullable NbtList lore) {
		if (lore != null) {
			NbtCompound displayTag = itemStack.getOrCreateSubNbt("display");
			displayTag.put("Lore", lore);
		} else {
			NbtCompound displayTag = itemStack.getSubNbt("display");

			if (displayTag != null) {
				displayTag.remove("Lore");

				if (displayTag.isEmpty()) {
					itemStack.removeSubNbt("display");
				}
			}
		}
	}

	public static void init() {
		Registry.register(Registries.LOOT_FUNCTION_TYPE, id("copy_lore"), COPY_LORE);
	}
}