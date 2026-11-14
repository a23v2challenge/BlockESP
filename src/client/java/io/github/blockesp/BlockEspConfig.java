package io.github.blockesp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class BlockEspConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockEspClient.MOD_ID);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("blockesp.json");
    private static final BlockEspConfig INSTANCE = new BlockEspConfig();

    private boolean enabled = true;
    private final Map<String, Boolean> ores = new HashMap<>();
    private int revision;

    private BlockEspConfig() {
        enableAllInternal();
    }

    public static BlockEspConfig get() {
        return INSTANCE;
    }

    public static void load() {
        if (!Files.exists(PATH)) {
            INSTANCE.save();
            return;
        }
        try {
            JsonObject root = GSON.fromJson(Files.readString(PATH, StandardCharsets.UTF_8), JsonObject.class);
            if (root != null && root.has("enabled")) {
                INSTANCE.enabled = root.get("enabled").getAsBoolean();
            }
            if (root != null && root.has("ores") && root.get("ores").isJsonObject()) {
                for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject("ores").entrySet()) {
                    if (entry.getValue().isJsonPrimitive()) {
                        INSTANCE.ores.put(entry.getKey(), entry.getValue().getAsBoolean());
                    }
                }
            }
            INSTANCE.revision++;
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Could not read BlockESP configuration; using defaults.", exception);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSelected(OreType ore) {
        return ores.getOrDefault(ore.configKey(), true);
    }

    public int revision() {
        return revision;
    }

    public boolean toggleEnabled() {
        enabled = !enabled;
        changed();
        return enabled;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        changed();
    }

    public void toggleOre(OreType ore) {
        ores.put(ore.configKey(), !isSelected(ore));
        changed();
    }

    public void enableAll() {
        enableAllInternal();
        changed();
    }

    private void enableAllInternal() {
        for (OreType ore : OreType.values()) {
            ores.put(ore.configKey(), true);
        }
    }

    private void changed() {
        revision++;
        save();
    }

    private void save() {
        JsonObject root = new JsonObject();
        root.addProperty("enabled", enabled);
        JsonObject oreValues = new JsonObject();
        for (OreType ore : OreType.values()) {
            oreValues.addProperty(ore.configKey(), isSelected(ore));
        }
        root.add("ores", oreValues);
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(root), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            LOGGER.warn("Could not save BlockESP configuration.", exception);
        }
    }
}
