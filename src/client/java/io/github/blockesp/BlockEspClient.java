package io.github.blockesp;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class BlockEspClient implements ClientModInitializer {
    public static final String MOD_ID = "blockesp";

    @Override
    public void onInitializeClient() {
        BlockEspConfig.load();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("Besp").executes(context -> toggle()));
            // Minecraft commands are case-sensitive; keep a lowercase alias for convenience.
            dispatcher.register(ClientCommandManager.literal("besp").executes(context -> toggle()));
        });
        ClientTickEvents.END_CLIENT_TICK.register(BlockEspScanner::tick);
        WorldRenderEvents.LAST.register(BlockEspRenderer::render);
    }

    private static int toggle() {
        boolean enabled = BlockEspConfig.get().toggleEnabled();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal("[BlockESP] " + (enabled ? "ON" : "OFF")), false);
        }
        return Command.SINGLE_SUCCESS;
    }
}
