package me.gemini.smoothlagfix;

import net.fabricmc.loader.api.FabricLoader;
import java.io.*;
import java.util.Properties;

public class ModConfig {
    public int clearIntervalTicks = 6000; // 5 minutes
    public boolean enableBrainThrottling = true;
    public boolean enableEntityCulling = true;
    public double cullingRange = 64.0;

    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "smoothlagfix.properties");

    public void load() {
        Properties props = new Properties();
        if (CONFIG_FILE.exists()) {
            try (InputStream is = new FileInputStream(CONFIG_FILE)) {
                props.load(is);
                clearIntervalTicks = Integer.parseInt(props.getProperty("clearIntervalTicks", "6000"));
                enableBrainThrottling = Boolean.parseBoolean(props.getProperty("enableBrainThrottling", "true"));
                enableEntityCulling = Boolean.parseBoolean(props.getProperty("enableEntityCulling", "true"));
                cullingRange = Double.parseDouble(props.getProperty("cullingRange", "64.0"));
            } catch (IOException | NumberFormatException e) {
                SmoothLagFix.LOGGER.error("Failed to load config, using defaults", e);
            }
        } else {
            save();
        }
    }

    public void save() {
        Properties props = new Properties();
        props.setProperty("clearIntervalTicks", String.valueOf(clearIntervalTicks));
        props.setProperty("enableBrainThrottling", String.valueOf(enableBrainThrottling));
        props.setProperty("enableEntityCulling", String.valueOf(enableEntityCulling));
        props.setProperty("cullingRange", String.valueOf(cullingRange));

        try (OutputStream os = new FileOutputStream(CONFIG_FILE)) {
            props.store(os, "SmoothLagFix Configuration");
        } catch (IOException e) {
            SmoothLagFix.LOGGER.error("Failed to save config", e);
        }
    }
}
