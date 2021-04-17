package fourmisain.keepheadnames.util;

import net.minecraft.nbt.ListTag;

public interface Loreable {
	void setLore(ListTag lore);
	ListTag getLore();
}
