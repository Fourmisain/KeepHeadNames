package io.github.fourmisain.keepheadnames;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KeepHeadNames.MOD_ID)
public class KeepHeadNamesForge {
	public KeepHeadNamesForge() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(KeepHeadNamesForge::commonSetup);
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(KeepHeadNames::init);
	}
}
