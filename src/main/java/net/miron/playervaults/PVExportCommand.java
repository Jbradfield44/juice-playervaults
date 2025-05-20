package net.miron.playervaults;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import static net.minecraft.commands.Commands.literal;

public class PVExportCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return literal("pvexport")
                .requires(PermissionUtils::canExportVault)
                .then(net.minecraft.commands.Commands.argument("target", EntityArgument.player())
                        .then(net.minecraft.commands.Commands.argument("number", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    ServerPlayer target = EntityArgument.getPlayer(context, "target");
                                    ServerPlayer player = context.getSource().getPlayerOrException();

                                    int number = IntegerArgumentType.getInteger(context, "number");
                                    int size = ConfigManager.config.vaultSize;
                                    UUID uuid = target.getUUID();

                                    RegistryAccess registryAccess = target.server.registryAccess();
                                    NonNullList<ItemStack> items = VaultStorage.loadVault(uuid, number, size, registryAccess);

                                    File folder = new File("pvexports");
                                    folder.mkdirs();

                                    File file = new File(folder, target.getName().getString() + "_pv" + number + ".json");
                                    try (FileWriter writer = new FileWriter(file)) {
                                        writer.write("[\n");
                                        for (int i = 0; i < items.size(); i++) {
                                            ItemStack stack = items.get(i);
                                            if (!stack.isEmpty()) {
                                                writer.write("  {\n");
                                                writer.write("    \"slot\": " + i + ",\n");
                                                writer.write("    \"item\": \"" + stack.getItem().toString() + "\",\n");
                                                writer.write("    \"count\": " + stack.getCount() + "\n");
                                                writer.write("  },\n");
                                            }
                                        }
                                        writer.write("  {}]\n");
                                        context.getSource().sendSuccess(() ->
                                                Component.literal("Exported vault #" + number + " of " + target.getName().getString() + " to /pvexports"), false);
                                    } catch (IOException e) {
                                        context.getSource().sendFailure(Component.literal("Failed to export vault."));
                                        e.printStackTrace();
                                    }

                                    return 1;
                                })
                        )
                );
    }
}
