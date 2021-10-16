package com.norsecraft.common.util.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * THis class takes a nbt compound and serialize it to a json object
 */
public class NbtSerializer {

    protected Logger logger = LogManager.getLogger("Nbt Serializer");

    /**
     * Our own save method for saving an item with all his nbt tags
     *
     * @return the full json object with all the data from the item
     */
    public JsonObject serialize(NbtCompound nbt) {
        JsonObject main = new JsonObject();
        if (nbt == null) {
            logger.warn("Could not serialize nbt data. Nbt data is null");
            return main;
        }
        Set<String> keys = nbt.getKeys();
        if (keys.isEmpty()) {
            logger.warn("The nbt tag has no keys");
            return main;
        }
        for (String key : keys) {
            NbtElement element = nbt.get(key);
            JsonElement jsonElement = this.fetchNbtElement(element);
            if (jsonElement == null)
                continue;
            main.add(key, jsonElement);
        }
        return main;
    }

    /**
     * Fetches a {@link NbtElement} to a {@link JsonElement}
     *
     * @param element the nbt element to fetch
     * @return the fetched nbt element as a json element
     * null if the {@link NbtElement} is null or the element type was not found
     */
    @Nullable
    private JsonElement fetchNbtElement(NbtElement element) {
        if (element == null)
            return null;
        if (element.getNbtType() == NbtTypes.byId(10)) { //NbtCompound
            NbtCompound compound = (NbtCompound) element;
            return this.fetchNbtCompound(compound);
        } else if (element.getNbtType() == NbtTypes.byId(8)) { //String
            return new JsonPrimitive(element.asString());
        } else if (element.getNbtType() == NbtTypes.byId(99)) { //99 = every numeric nbt value except 5 and 6 (float and double)
            return new JsonPrimitive(((AbstractNbtNumber) element).numberValue());
        } else if (element.getNbtType() == NbtTypes.byId(5)) { //Float
            NbtFloat nbtFloat = (NbtFloat) element;
            return new JsonPrimitive(nbtFloat.floatValue());
        } else if (element.getNbtType() == NbtTypes.byId(6)) { //Double
            NbtDouble nbtDouble = (NbtDouble) element;
            return new JsonPrimitive(nbtDouble.doubleValue());
        } else if (element.getNbtType() == NbtTypes.byId(7)) { //ByteArray
            NbtByteArray byteArray = (NbtByteArray) element;
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("BYTE");
            for (byte b : byteArray.getByteArray()) {
                jsonArray.add(b);
            }
            return jsonArray;
        } else if (element.getNbtType() == NbtTypes.byId(9)) { //List
            NbtList list = (NbtList) element;
            return this.fetchNbtList(list);
        } else if (element.getNbtType() == NbtTypes.byId(11)) { //IntArray
            NbtIntArray intArray = (NbtIntArray) element;
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("INT");
            for (int i : intArray.getIntArray())
                jsonArray.add(i);
            return jsonArray;
        } else if (element.getNbtType() == NbtTypes.byId(12)) { //LongArray
            NbtLongArray longArray = (NbtLongArray) element;
            JsonArray jsonArray = new JsonArray();
            jsonArray.add("LONG");
            for (long l : longArray.getLongArray())
                jsonArray.add(l);
            return jsonArray;
        }
        logger.warn("Element with nbt typeId {} not found", element.getType());
        return null;
    }

    private JsonObject fetchNbtCompound(NbtCompound nbt) {
        JsonObject main = new JsonObject();
        for (String key : nbt.getKeys()) {
            NbtElement element = nbt.get(key);
            main.add(key, fetchNbtElement(element));
        }
        return main;
    }

    private JsonArray fetchNbtList(NbtList list) {
        JsonArray array = new JsonArray();
        array.add("LIST");
        for (NbtElement element : list) {
            if (element.getNbtType() == NbtTypes.byId(10)) { //NbtCompound
                JsonElement obj = this.fetchNbtElement(element);
                array.add(obj);
            } else if (element.getNbtType() == NbtTypes.byId(8)) { //String
                array.add(element.asString());
            } else if (element.getNbtType() == NbtTypes.byId(99)) { //99 = every numeric nbt value
                array.add(((AbstractNbtNumber) element).numberValue());
            } else if (element.getNbtType() == NbtTypes.byId(5)) { //Float
                NbtFloat nbtFloat = (NbtFloat) element;
                array.add(nbtFloat.floatValue());
            } else if (element.getNbtType() == NbtTypes.byId(6)) { //Double
                NbtDouble nbtDouble = (NbtDouble) element;
                array.add(nbtDouble.doubleValue());
            } else if (element.getNbtType() == NbtTypes.byId(7)) { //ByteArray
                NbtByteArray byteArray = (NbtByteArray) element;
                JsonArray jsonArray = new JsonArray();
                for (byte b : byteArray.getByteArray()) {
                    jsonArray.add(b);
                }
                array.add(jsonArray);
            } else if (element.getNbtType() == NbtTypes.byId(9)) { //List
                NbtList l = (NbtList) element;
                array.add(this.fetchNbtList(l));
            } else if (element.getNbtType() == NbtTypes.byId(11)) { //IntArray
                NbtIntArray intArray = (NbtIntArray) element;
                JsonArray jsonArray = new JsonArray();
                for (int i : intArray.getIntArray())
                    jsonArray.add(i);
                array.add(jsonArray);
            } else if (element.getNbtType() == NbtTypes.byId(12)) { //LongArray
                NbtLongArray longArray = (NbtLongArray) element;
                JsonArray jsonArray = new JsonArray();
                for (long l : longArray.getLongArray())
                    jsonArray.add(l);
                array.add(jsonArray);
            } else {
                logger.warn("Element with nbt typeId {} not found", element.getType());
            }
        }

        return array;
    }

}
