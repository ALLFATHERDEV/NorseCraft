package com.norsecraft.common.dialog;

public class Dialog {

    private final String id;
    private final String message;
    private final DialogAnswer[] answers;

    public Dialog(String id, String message, DialogAnswer[] answers) {
        this.id = id;
        this.message = message;
        this.answers = answers;
    }

    public String getMessage() {
        return message;
    }

    public DialogAnswer[] getAnswers() {
        return answers;
    }

    public String getId() {
        return id;
    }

    public static class DialogAnswer {

        private final String parentDialogId;
        private final String id;
        private final String message;
        private final String referenceDialogId;

        public DialogAnswer(String parentDialogId, String id, String message, String referenceDialogId) {
            this.parentDialogId = parentDialogId;
            this.id = id;
            this.message = message;
            this.referenceDialogId = referenceDialogId;
        }

        public String getParentDialogId() {
            return parentDialogId;
        }

        public String getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }

        public String getReferenceDialogId() {
            return referenceDialogId;
        }
    }

}
