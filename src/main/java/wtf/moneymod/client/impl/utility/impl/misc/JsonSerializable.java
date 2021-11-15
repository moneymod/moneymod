package wtf.moneymod.client.impl.utility.impl.misc;

import com.google.gson.JsonObject;

public interface JsonSerializable<T> {

    T fromJson(JsonObject json) throws IllegalArgumentException;

    JsonObject toJson(T object);

}
