package thingswithworth.org.transittimes.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Alex on 8/28/2015.
 */
public class RouteTypeDeserializer implements JsonDeserializer<Route.RouteType>
{

    @Override
    public Route.RouteType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        int key = json.getAsInt();
        return Route.RouteType.fromKey(key);
    }
}
