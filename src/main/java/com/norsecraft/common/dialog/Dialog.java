package com.norsecraft.common.dialog;

/**
 * A class that holds dialog data
 */
public class Dialog {

    /**
     * The dialog id
     */
    private final String id;

    /**
     * The dialog message
     */
    private final String message;

    /**
     * The dialog answers
     */
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

    /**
     * A class that holds the data for a dialog answer
     */
    public static class DialogAnswer {

        /**
         * The parent dialog id
         */
        private final String parentDialogId;

        /**
         * The dialog answer id
         */
        private final String id;

        /**
         * The answer message
         */
        private final String message;

        /**
         * The reference dialog id
         */
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
