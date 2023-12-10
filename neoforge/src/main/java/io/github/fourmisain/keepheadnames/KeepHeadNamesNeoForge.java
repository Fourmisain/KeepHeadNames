package io.github.fourmisain.keepheadnames;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(KeepHeadNames.MOD_ID)
public class KeepHeadNamesNeoForge {
	public KeepHeadNamesNeoForge() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(RegisterEvent.class, event -> {
			KeepHeadNames.init();
		});
	}
}
