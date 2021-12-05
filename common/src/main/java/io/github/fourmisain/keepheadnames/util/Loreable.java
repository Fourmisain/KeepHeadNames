package io.github.fourmisain.keepheadnames.util;

import net.minecraft.nbt.NbtList;

public interface Loreable {
	void setLore(NbtList lore);
	NbtList getLore();
}
