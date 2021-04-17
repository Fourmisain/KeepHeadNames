package fourmisain.keepheadnames.util;

import net.minecraft.text.Text;

/** The Nameable interface doesn't have a setter, so we use our own */
public interface NameSettable {
    void setCustomName(Text customName);
}
