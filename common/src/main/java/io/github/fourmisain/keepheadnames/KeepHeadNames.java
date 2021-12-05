package io.github.fourmisain.keepheadnames;

import io.github.fourmisain.keepheadnames.util.CopyLoreLootFunction;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class KeepHeadNames {
	public static final String MOD_ID = "keepheadnames";

	public static LootFunctionType COPY_LORE;

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Nullable
	public static NbtList getLore(ItemStack itemStack) {
		NbtCompound displayTag = itemStack.getSubTag("display");

		if (displayTag != null && displayTag.contains("Lore", 9)) {
			return displayTag.getList("Lore", 8);
		}

		return null;
	}

	public static void setLore(ItemStack itemStack, @Nullable NbtList lore) {
		if (lore != null) {
			NbtCompound displayTag = itemStack.getOrCreateSubTag("display");
			displayTag.put("Lore", lore);
		} else {
			NbtCompound displayTag = itemStack.getSubTag("display");

			if (displayTag != null) {
				displayTag.remove("Lore");

				if (displayTag.isEmpty()) {
					itemStack.removeSubTag("display");
				}
			}
		}
	}

	public static void init() {
		COPY_LORE = Registry.register(Registry.LOOT_FUNCTION_TYPE, id("copy_lore"), new LootFunctionType(new CopyLoreLootFunction.Serializer()));
	}
}