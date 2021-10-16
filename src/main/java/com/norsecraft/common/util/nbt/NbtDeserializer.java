package com.norsecraft.common.util.nbt;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.norsecraft.NorseCraftMod;
import net.minecraft.nbt.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * This class takes a json object and deserialize this object to a nbt compound
 */
public class NbtDeserializer {

    protected Logger logger = LogManager.getLogger("Nbt Deserializer");

    /**
     * Deserialize a json object to a nbt compound
     *
     * @param object the json object to deserialize
     * @return the new nbt compound
     */
    public NbtCompound deserialize(JsonObject object) {
        NbtCompound nbt = new NbtCompound();
        if (object == null) {
            logger.warn("Could not deserialize nbt tag, because the json object is null");
            return nbt;
        }
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key = entry.getKey();
            JsonElement element = entry.getValue();
            NorseCraftMod.LOGGER.info("Key: {}", key);
            NbtElement nbtElement = this.fetchJsonElement(element);
            if (nbtElement == null)
                continue;
            nbt.put(key, nbtElement);
        }
        return nbt;
    }

    private NbtCompound fetchJsonObject(JsonObject object) {
        NbtCompound nbt = new NbtCompound();
        for(Map.Entry<String, JsonElement> entry : object.entrySet())
            nbt.put(entry.getKey(), this.fetchJsonElement(entry.getValue()));
        return nbt;
    }

    /**
     * This method looks which type of {@link NbtElement} is in the {@link JsonElement}
     *
     * @param element the element to fetch
     * @return the {@link NbtElement} or null, if no element was found
     */
    @Nullable
    private NbtElement fetchJsonElement(JsonElement element) {
        if (element == null)
            return null;
        if (element.isJsonObject()) {
            JsonObject obj = (JsonObject) element;
            return this.fetchJsonObject(obj);
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = (JsonPrimitive) element;
            String asString = primitive.getAsString();
            //Now we have to find out which type it is:
            try {
                return NbtInt.of(Integer.parseInt(asString));
            } catch (NumberFormatException e) {
                //If it is not an int, we do nothing here
            }
            try {
                return NbtDouble.of(Double.parseDouble(asString));
            } catch (NumberFormatException e) {
                //If it is not a double, we do nothing here
            }
            try {
                return NbtFloat.of(Float.parseFloat(asString));
            } catch (NumberFormatException e) {
                //If it is not a float, we do nothing here
            }
            try {
                return NbtByte.of(Byte.parseByte(asString));
            } catch (NumberFormatException e) {
                //If it is not a byte, we do nothing here
            }
            try {
                return NbtLong.of(Long.parseLong(asString));
            } catch (NumberFormatException e) {
                //If it is not a long, we do nothing here
            }
            try {
                return NbtShort.of(Short.parseShort(asString));
            } catch (NumberFormatException e) {
                //If it is hot a short, we do nothing here
            }
            //If it is not a number, it is a string
            return NbtString.of(asString);
        } else if (element.isJsonArray()) {
            JsonArray jsonArray = (JsonArray) element;
            String type = jsonArray.get(0).getAsString();
            //Now we have to find out the type of the array
            if (type.equals("BYTE")) {
                byte[] byteArray = new byte[jsonArray.size() - 1];
                for (int i = 1; i < jsonArray.size(); i++) {
                    byteArray[i - 1] = jsonArray.get(i).getAsByte();
                }
                return new NbtByteArray(byteArray);
            } else if (type.equals("LIST")) {
                NbtList nbtList = new NbtList();
                for (int i = 1; i < jsonArray.size(); i++)
                    nbtList.add(this.fetchJsonElement(jsonArray.get(i)));
                return nbtList;
            } else if (type.equals("INT")) {
                int[] intArray = new int[jsonArray.size() - 1];
                for (int i = 1; i < jsonArray.size(); i++) {
                    intArray[i - 1] = jsonArray.get(i).getAsInt();
                }
                return new NbtIntArray(intArray);
            } else if (type.equals("LONG")) {
                long[] longArray = new long[jsonArray.size() - 1];
                for (int i = 1; i < jsonArray.size(); i++)
                    longArray[i - 1] = jsonArray.get(i).getAsLong();
                return new NbtLongArray(longArray);
            } else {
                logger.warn("Could not fetch the element to a list. List type not found");
                return null;
            }
        }
        return null;
    }


}
