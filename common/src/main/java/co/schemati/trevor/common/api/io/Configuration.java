package co.schemati.trevor.common.api.io;

import com.google.common.base.Preconditions;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Configuration {

    private final File folder;
    private final File file;
    private final ConfigurationLoader<?> loader;
    private ConfigurationNode node;

    public Configuration(File folder, File file, ConfigurationLoader<?> loader,
                         ConfigurationNode node) {
        this.folder = folder;
        this.file = file;
        this.loader = loader;
        this.node = node;
    }

    public boolean reload() {
        ConfigurationNode fallback = node;
        try {

            this.node = loader.load();

            return true;
        } catch (IOException exception) {
            exception.printStackTrace();

            node = fallback;

            return false;
        }
    }

    public boolean save() {
        try {
            loader.save(node);

            return true;
        } catch (IOException exception) {
            exception.printStackTrace();

            return false;
        }
    }

    public ConfigurationNode getNode(Object... values) {
        return node.getNode(values);
    }

    public void setNode(ConfigurationNode node) {
        setNode(node, false);
    }

    public void setNode(ConfigurationNode node, boolean save) {
        this.node = node;

        if (save) {
            save();
        }
    }

    public File getFolder() {
        return folder;
    }

    public File getFile() {
        return file;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder of(Path path) {
        return of(path.toFile());
    }

    public static Builder of(Path path, String name) {
        return of(path.resolve(name));
    }

    public static Builder of(File file) {
        return of(file.getParentFile(), file.getName());
    }

    public static Builder of(File parent, String name) {
        return new Builder().folder(parent).name(name);
    }

    public static class Builder {

        private File folder;
        private String name;

        public Builder folder(File folder) {
            this.folder = folder;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Configuration build() {
            Preconditions.checkNotNull(folder);
            Preconditions.checkNotNull(name);

            try {
                File file = Files.getOrCreate(folder, name);
                ConfigurationLoader<?> loader =
                        YAMLConfigurationLoader.builder().setDefaultOptions(
                                ConfigurationOptions.defaults().withShouldCopyDefaults(true)
                        ).setFile(file).build();

                ConfigurationNode node = loader.load();

                return new Configuration(folder, file, loader, node);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            return null;
        }
    }
}