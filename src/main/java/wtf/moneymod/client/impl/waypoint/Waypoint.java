package wtf.moneymod.client.impl.waypoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.impl.utility.impl.misc.JsonSerializable;

public class Waypoint implements JsonSerializable<Waypoint> {

    private BlockPos pos;
    private String name, server;
    private int dimension;

    public Waypoint(String name, BlockPos blockPos, String server, int dimension) {
        this.name = name;
        this.pos = blockPos;
        this.server = server;
        this.dimension = dimension;
    }

    public String getName() { return name; }

    public BlockPos getPos() { return pos; }

    public int getDimension() { return dimension; }

    public String getServer() { return server; }

    @Override public Waypoint fromJson(JsonObject json) throws IllegalArgumentException {
        String name = json.get("name").getAsString();
        String server = json.get("server").getAsString();
        JsonArray array = json.get("coords").getAsJsonArray();
        if (array.size() < 3) throw new IllegalArgumentException();
        BlockPos pos = new BlockPos(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
        int dimension = json.get("dimension").getAsInt();
        if (name == null || server == null) throw new IllegalArgumentException();
        return new Waypoint(name, pos, server, dimension);
    }

    @Override public JsonObject toJson(Waypoint object) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", object.getName());
        jsonObject.addProperty("server", object.getServer());
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(object.getPos().getX());
        jsonArray.add(object.getPos().getY());
        jsonArray.add(object.getPos().getZ());
        jsonObject.add("coords", jsonArray);
        jsonObject.addProperty("dimension", object.dimension);
        return jsonObject;
    }

}
