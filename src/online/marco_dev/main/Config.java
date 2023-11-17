package online.marco_dev.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private final Properties configFile;

    public Config() {
        createConfig();
        this.configFile = loadConfigFile();
    }

    private void createConfig() {
        File folder = new File("config");
        File configFile = new File("config", "config.properties");

        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("config" + " folder created successfully.");
            } else {
                throw new RuntimeException("Failed to create " + "config" + " folder.");
            }
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                System.out.println("config.properties" + " file created successfully.");
            } catch (IOException e) {
                throw new RuntimeException("Failed to create " + "config.properties" + " file.", e);
            }
        }
    }

    private Properties loadConfigFile() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./config/config.properties")) {
            properties.load(fis);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file not found", e);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config file", e);
        }
        return properties;
    }

    public String getProperty(String key) {
        return this.configFile.getProperty(key);
    }
}
