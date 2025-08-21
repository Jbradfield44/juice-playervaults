package net.miron.playervaults;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class PlayerVaultsMod implements ModInitializer {

	public static final String MOD_ID = "playervaults";

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(PVCommand.register());
			dispatcher.register(PVExportCommand.register());
		});

		System.out.println("PlayerVaults loaded! Vault size: " + ConfigManager.config.vaultSize);
	}
}
