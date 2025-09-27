package org.yaroslaavl.communicationservice.database.entity.enums;

public enum MessageStatus {

    /**
     * Represents the status of a message that has been successfully sent but not yet delivered to the recipient.
     */
    SENT,

    /**
     * Represents the status of a message that has been successfully delivered to the recipient.
     */
    DELIVERED
}
