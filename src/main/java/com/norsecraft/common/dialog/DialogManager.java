package com.norsecraft.common.dialog;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.norsecraft.NorseCraftMod;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * This class loads the dialogs from the folder: resources/assets/norsecraft/dialogs
 */
public class DialogManager {

    private final int DIALOG_COUNTS = 1;

    private final List<DialogGroup> loadedDialogs;

    public DialogManager() {
        this.loadedDialogs = Lists.newArrayList();
    }

    public void loadDialogs(ResourceManager resourceManager) {
        try {
            for(int i = 0; i < DIALOG_COUNTS; i++) {
                Resource dialogResource = resourceManager.getResource(NorseCraftMod.nc("dialog/dialog_" + i + ".json"));
                Gson gson = new Gson();
                JsonObject dialogJson = gson.fromJson(new JsonReader(new InputStreamReader(dialogResource.getInputStream())), JsonObject.class);
                Dialog initialDialog = readDialog(dialogJson.getAsJsonObject("initialMessage"));
                JsonArray nextDialogsArray = dialogJson.getAsJsonArray("nextDialogs");
                Dialog[] nextDialogs = new Dialog[nextDialogsArray.size()];
                for(int j = 0; j < nextDialogsArray.size(); j++)
                    nextDialogs[j] = readDialog(nextDialogsArray.get(j).getAsJsonObject());

                Identifier id = dialogResource.getId();
                String name = id.toString().substring(id.toString().indexOf('/') + 1).replace(".json", "");
                NorseCraftMod.LOGGER.info("NAME: {}", name);
                this.loadedDialogs.add(new DialogGroup(name, initialDialog, nextDialogs));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<DialogGroup> getDialogGroupByName(String name) {
        return loadedDialogs.stream().filter(dialogGroup -> dialogGroup.getName().equals(name)).findFirst();
    }

    public Optional<Dialog> findDialogById(String id) {
        return loadedDialogs.stream().filter(dialogGroup -> {
            if(dialogGroup.getInitialMessage() != null && dialogGroup.getInitialMessage().getId().equals(id))
                return true;
            Optional<Dialog> dialog = dialogGroup.getNextDialogById(id);
            return dialog.isPresent();
        }).findFirst().orElseThrow().getNextDialogById(id);
    }

    private Dialog readDialog(JsonObject dialogObject) {
        String message = dialogObject.get("message").getAsString();
        String dialogId = dialogObject.get("dialogId").getAsString();
        JsonArray dialogAnswers = dialogObject.get("dialogAnswers").getAsJsonArray();
        Dialog.DialogAnswer[] answers = new Dialog.DialogAnswer[dialogAnswers.size()];
        for(int i = 0; i < dialogAnswers.size(); i++)
            answers[i] = readAnswer(dialogAnswers.get(i).getAsJsonObject(), dialogId);
        return new Dialog(dialogId, message, answers);
    }

    private Dialog.DialogAnswer readAnswer(JsonObject object, String parentDialogId) {
        String name = object.get("name").getAsString();
        String message = object.get("message").getAsString();
        String referenceDialogId = object.get("nextDialog").getAsString();
        return new Dialog.DialogAnswer(parentDialogId, name, message, referenceDialogId);
    }

}
