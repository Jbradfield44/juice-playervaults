package net.miron.playervaults;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class PlayerVaultsMod implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(PVCommand.register());
			dispatcher.register(PVExportCommand.register()); // 👈 add this line
		});
		System.out.println("PlayerVaults loaded!"+ ConfigManager.config.vaultSize);

	}

}
