package wtf.moneymod.client.api.management.impl;

import com.google.gson.*;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.Option;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.impl.utility.impl.misc.SettingUtils;
import wtf.moneymod.client.impl.utility.impl.render.JColor;
import wtf.moneymod.client.impl.waypoint.Waypoint;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ConfigManagement extends Thread implements Globals {

    private static final ConfigManagement CONFIG_MANAGER;

    private static final File mainFolder = new File("moneymod");
    private static final String modulesFolder = mainFolder.getAbsolutePath() + "/modules";
    private static final String FRIEND = "Friends.json";
    private static final String WAYPOINTS = "Waypoints.json";

    public static File getMainFolder() {return mainFolder;}

    public void load() {
        try {
            loadFriends();
        } catch (Exception e) { }
        try {
            loadWaypoints();
        } catch (Exception ignored) { }
        loadModules();
    }

    private void loadModules() {
        for (Module m : Main.getMain().getModuleManager()) {
            loadModule(m);
        }
    }

    private void loadModule(Module m) {
        try {
            Path path = Paths.get(modulesFolder, m.getLabel() + ".json");
            if (!path.toFile().exists()) return;
            String rawJson = loadFile(path.toFile());
            JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
            if (jsonObject.get("Enabled") != null && jsonObject.get("KeyBind") != null) {
                if (jsonObject.get("Enabled").getAsBoolean() && !m.isConfigException()) m.setToggled(true);
                m.setKey(jsonObject.get("KeyBind").getAsInt());
            }
            if (jsonObject.get("Drawn") != null) {
                m.drawn = jsonObject.get("Drawn").getAsBoolean();
            }
            if(jsonObject.get("Hold") != null) {
                m.setHold(jsonObject.get("Hold").getAsBoolean());
            }
            Option.getContainersForObject(m).forEach(s -> {
                JsonElement settingObject = jsonObject.get(s.getName());
                if (settingObject != null) {
                    if (s.getValue().getClass().isEnum()) {
                        try {
                            (( Option<Enum> ) s).setValue(SettingUtils.INSTANCE.getProperEnum(( Enum ) s.getValue(), settingObject.getAsString()));
                        } catch (Exception ignored) { }
                        return;
                    }
                    switch (s.getValue().getClass().getSimpleName()) {
                        case "Boolean":
                            (( Option<Boolean> ) s).setValue(settingObject.getAsBoolean());
                            break;
                        case "Integer":
                            (( Option<Number> ) s).setValue(settingObject.getAsInt());
                            break;
                        case "Double":
                            (( Option<Number> ) s).setValue(settingObject.getAsDouble());
                            break;
                        case "Float":
                            (( Option<Number> ) s).setValue(settingObject.getAsFloat());
                            break;
                        case "JColor":
                            JsonArray jsonElements = settingObject.getAsJsonArray();
                            (( Option<JColor> ) s).setValue(new JColor(jsonElements.get(0).getAsInt(), jsonElements.get(1).getAsInt(), jsonElements.get(2).getAsInt(), jsonElements.get(3).getAsInt(), jsonElements.get(4).getAsBoolean()));
                            break;
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void loadFriends() throws IOException {
        Path path = Paths.get(mainFolder.getAbsolutePath(), FRIEND);
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        if (jsonObject.get("Friends") != null) {
            JsonArray friendObject = jsonObject.get("Friends").getAsJsonArray();
            friendObject.forEach(object -> FriendManagement.getInstance().add(object.getAsString()));
        }
    }

    private void loadWaypoints() throws IOException {
        Path path = Paths.get(mainFolder.getAbsolutePath(), WAYPOINTS);
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        JsonArray waypoints = null;
        if ((waypoints = jsonObject.get("waypoints").getAsJsonArray()) != null) {
            for (JsonElement element : waypoints) {
                JsonObject waypointJson = element.getAsJsonObject();
                if (waypointJson.size() != 4) continue;
                try {
                    Waypoint waypoint = new Waypoint().fromJson(waypointJson);
                    WaypointManagement.getInstance().add(waypoint);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveWaypoints() {
        Path path = Paths.get(mainFolder.getAbsolutePath(), WAYPOINTS);
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (Waypoint waypoint : WaypointManagement.getInstance()) {
            jsonArray.add(waypoint.toJson(waypoint));
        }
        jsonObject.add("waypoints", jsonArray);
        try {
            Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
        } catch (Exception ignored) {}
    }

    private void saveFriends() throws IOException {
        Path path = Paths.get(mainFolder.getAbsolutePath(), FRIEND);
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        JsonArray friends = new JsonArray();
        FriendManagement.getInstance().forEach(friends::add);
        jsonObject.add("Friends", friends);
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
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
        saveModules();
        saveWaypoints();
        try {
            saveFriends();
        } catch (Exception e) { }
    }

    private void saveModules() {
        for (Module m : Main.getMain().getModuleManager()) {
            saveModule(m);
        }
    }

    private void saveModule(Module m) {
        try {
            Path path = Paths.get(modulesFolder, m.getLabel() + ".json");
            createFile(path);
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("Enabled", new JsonPrimitive(m.isToggled()));
            jsonObject.add("KeyBind", new JsonPrimitive(m.getKey()));
            jsonObject.add("Drawn", new JsonPrimitive(m.drawn));
            jsonObject.add("Hold", new JsonPrimitive(m.isHold()));
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
                    case "Float":
                        jsonObject.add(s.getName(), new JsonPrimitive(( Float ) s.getValue()));
                        break;
                    case "JColor":
                        JsonArray jsonColors = new JsonArray();
                        JColor color = (( Option<JColor> ) s).getValue();
                        jsonColors.add(color.getColor().getRed());
                        jsonColors.add(color.getColor().getGreen());
                        jsonColors.add(color.getColor().getBlue());
                        jsonColors.add(color.getColor().getAlpha());
                        jsonColors.add(color.isRainbow());
                        jsonObject.add(s.getName(), jsonColors);
                }
            });
            Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFile(Path path) {
        if (Files.exists(path)) new File(path.normalize().toString()).delete();
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ConfigManagement getInstance() {
        return CONFIG_MANAGER;
    }

    static {
        CONFIG_MANAGER = new ConfigManagement();
    }

}
