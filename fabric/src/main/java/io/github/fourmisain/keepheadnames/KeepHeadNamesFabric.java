package io.github.fourmisain.keepheadnames;

import net.fabricmc.api.ModInitializer;

public class KeepHeadNamesFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		KeepHeadNames.init();
	}
}