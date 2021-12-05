package io.github.fourmisain.keepheadnames;

import io.github.fourmisain.keepheadnames.KeepHeadNames;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod(KeepHeadNames.MOD_ID)
public class KeepHeadNamesForge {
	public KeepHeadNamesForge() {
		// this mod is server-only
		ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

		FMLJavaModLoadingContext.get().getModEventBus().addListener(KeepHeadNamesForge::commonSetup);
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(KeepHeadNames::init);
	}
}
