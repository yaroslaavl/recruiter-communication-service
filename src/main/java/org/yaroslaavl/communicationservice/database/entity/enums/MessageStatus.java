package org.yaroslaavl.communicationservice.database.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "MessageStatus")
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
