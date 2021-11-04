package wtf.moneymod.client.api.management.impl;

import com.google.gson.*;
import scala.Int;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.misc.SettingUtils;
import wtf.moneymod.client.impl.utility.impl.render.JColor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager extends Thread implements Globals {

    private static final ConfigManager CONFIG_MANAGER;

    private static final File mainFolder = new File("moneymod");
    private static final String modulesFolder = mainFolder.getAbsolutePath() + "/modules";

    public static File getMainFolder() {return mainFolder;}

    public void load() {
        try {
            loadModules();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadModules() throws Exception {
        for (Module m : Main.getMain().getModuleManager()) {
            loadModule(m);
        }
    }

    private void loadModule(Module m) throws Exception {
        Path path = Paths.get(modulesFolder, m.getLabel() + ".json");
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        if (jsonObject.get("Enabled") != null && jsonObject.get("KeyBind") != null) {
            if (jsonObject.get("Enabled").getAsBoolean() && !m.isConfigException()) m.setToggled(true);
            m.setKey(jsonObject.get("KeyBind").getAsInt());
        }
        Option.getContainersForObject(m).forEach(s -> {
            JsonElement settingObject = jsonObject.get(s.getName());
            if (settingObject != null) {
                if (s.getValue().getClass().isEnum()) {
                    try {
                        ((Option<Enum>) s).setValue(SettingUtils.INSTANCE.getProperEnum(( Enum ) s.getValue(), settingObject.getAsString()));
                    } catch (Exception ignored) { }
                    return;
                }
                switch (s.getValue().getClass().getSimpleName()) {
                    case "Boolean":
                        ((Option<Boolean>) s).setValue(settingObject.getAsBoolean());
                        break;
                    case "Number":
                        if (settingObject.getAsDouble() < s.getMax() && settingObject.getAsDouble() > s.getMin())
                            ((Option<Number>) s).setValue(settingObject.getAsDouble());
                        break;
                    case "JColor":
                        JsonArray jsonElements = settingObject.getAsJsonArray();
                        ((Option<JColor>) s).setValue(new JColor(jsonElements.get(0).getAsInt(), jsonElements.get(1).getAsInt(), jsonElements.get(2).getAsInt(), jsonElements.get(3).getAsInt(), jsonElements.get(4).getAsBoolean()));
                        break;
                }
            }
        });
    }

    public String loadFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @Override public void run() {
        if (!mainFolder.exists() && !mainFolder.mkdirs()) System.out.println("Failed to create config folder");
        if (!new File(modulesFolder).exists() && !new File(modulesFolder).mkdirs())
            System.out.println("Failed to create modules folder");
        try {
            saveModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveModules() throws IOException {
        for (Module m : Main.getMain().getModuleManager()) {
            saveModule(m);
        }
    }

    private void saveModule(Module m) throws IOException {
        Path path = Paths.get(modulesFolder, m.getLabel() + ".json");
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("Enabled", new JsonPrimitive(m.isToggled()));
        jsonObject.add("KeyBind", new JsonPrimitive(m.getKey()));
        Option.getContainersForObject(m).forEach(s -> {
            if (s.getValue().getClass().isEnum()) {
                jsonObject.add(s.getName(), new JsonPrimitive(SettingUtils.INSTANCE.getProperName(( Enum ) s.getValue())));
                return;
            }
            switch (s.getValue().getClass().getSimpleName()) {
                case "Boolean":
                    jsonObject.add(s.getName(), new JsonPrimitive(( Boolean ) s.getValue()));
                    break;
                case "Double":
                    jsonObject.add(s.getName(), new JsonPrimitive(( Double ) s.getValue()));
                    break;
                case "Integer":
                    jsonObject.add(s.getName(), new JsonPrimitive(( Integer ) s.getValue()));
                    break;
                case "JColor":
                    JsonArray jsonColors = new JsonArray();
                    JColor color =  ((Option<JColor>) s).getValue();
                    jsonColors.add(color.getColor().getRed());
                    jsonColors.add(color.getColor().getGreen());
                    jsonColors.add(color.getColor().getBlue());
                    jsonColors.add(color.getColor().getAlpha());
                    jsonColors.add(color.isRainbow());
                    jsonObject.add(s.getName(), jsonColors);
            }
        });
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
    }

    private void createFile(Path path) {
        if (Files.exists(path)) new File(path.normalize().toString()).delete();
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManager getInstance() {
        return CONFIG_MANAGER;
    }

    static {
        CONFIG_MANAGER = new ConfigManager();
    }

}
