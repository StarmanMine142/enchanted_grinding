package starman.enchantedgrinding.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class EGConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path configPath;
    private static EGConfig config = new EGConfig();

    private EGConfigManager() {}

    public static void init(Path configDir, String fileName) {
        configPath = configDir.resolve(fileName + ".json");
        load();
    }

    public static EGConfig get() {
        if (config == null) {
            config = new EGConfig();
        }
        return config;
    }

    public static void load() {
        if (configPath == null) {
            throw new IllegalStateException("EGConfigManager.init() must be called before load()");
        }
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                EGConfig loaded = GSON.fromJson(reader, EGConfig.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        if (configPath == null) {
            return;
        }
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}