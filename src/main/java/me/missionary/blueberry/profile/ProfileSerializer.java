package me.missionary.blueberry.profile;

import net.minecraft.util.com.google.gson.*;

import java.lang.reflect.Type;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Missionary (missionarymc@gmail.com) on 9/1/2017.
 */
public class ProfileSerializer implements JsonSerializer<Profile>, JsonDeserializer<Profile> {

    @Override
    public Profile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        Profile profile = new Profile(UUID.fromString(object.get("uniqueId").getAsString()));
        profile.setKills(object.get("kills").getAsInt());
        profile.setDeaths(object.get("deaths").getAsInt());
        profile.setTimeZone(TimeZone.getTimeZone(object.get("timeZone").getAsString()));
        return profile;
    }

    @Override
    public JsonElement serialize(Profile profile, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("uniqueId", profile.getUuid().toString());
        object.addProperty("kills", profile.getKills());
        object.addProperty("deaths", profile.getDeaths());
        object.addProperty("timeZone", profile.getTimeZone().getID());
        return object;
    }
}
