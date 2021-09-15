package com.norsecraft.common.dialog;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

public class DialogGroup {

    private final String name;
    private final Dialog initialMessage;
    private final List<Dialog> nextDialogs;
    private Dialog currentDialog;

    public DialogGroup(String name, Dialog initialMessage, Dialog[] nextDialogs) {
        this.name = name;
        this.initialMessage = initialMessage;
        this.nextDialogs = Lists.newArrayList(nextDialogs);
        this.currentDialog = initialMessage;
    }

    public boolean nextDialog(Dialog.DialogAnswer clickedAnswer) {
        if (this.currentDialog == null)
            return false;

        Optional<Dialog> maybeNext = this.nextDialogs.stream().filter(dialog -> dialog.getId().equals(clickedAnswer.getId())).findFirst();
        if (maybeNext.isEmpty())
            return false;
        this.currentDialog = maybeNext.get();
        return true;
    }

    public Dialog getCurrentDialog() {
        return currentDialog;
    }

    public Dialog getInitialMessage() {
        return initialMessage;
    }

    public Optional<Dialog> getNextDialogById(String dialogId) {
        if(dialogId.equals("initial_message"))
            return Optional.of(initialMessage);
        return nextDialogs.stream().filter(dialog -> dialog.getId().equals(dialogId)).findFirst();
    }

    public String getName() {
        return name;
    }
}
