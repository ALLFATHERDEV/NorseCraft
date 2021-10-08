package com.norsecraft.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;

public class ItemModelDataGenerator implements NorseCraftDataGenerator {

    @Override
    public void generate() {

    }

    private void generateBlockItem(Supplier<Block> block, String additionalPath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        JsonObject main = new JsonObject();
        String blockName = Registry.BLOCK.getId(block.get()).getPath();
        main.addProperty("parent", "norsecraft:block/" + (additionalPath != null ? additionalPath + "/" + blockName : blockName));

        this.save(gson.toJson(main), blockName);
    }

    private void save(String jsonString, String blockName) {
        try {
            File folder = new File("../data_generator/models/item/");
            if (!folder.exists())
                if (!folder.mkdirs())
                    return;
            File file = new File(folder, blockName + ".json");
            FileWriter writer = new FileWriter(file);
            writer.write(jsonString);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDataGeneratorName() {
        return "Item Model";
    }
}
