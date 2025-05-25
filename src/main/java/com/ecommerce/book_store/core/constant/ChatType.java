package com.ecommerce.book_store.core.constant;

public enum ChatType {
    BOOK_SEARCH("book_search"),
    ORDER_LOOKUP("order_lookup"),
    GENERAL("general");

    private final String type;

    ChatType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ChatType fromString(String type) {
        for (ChatType chatType : ChatType.values()) {
            if (chatType.getType().equalsIgnoreCase(type)) {
                return chatType;
            }
        }
        throw new IllegalArgumentException("Unknown chat type: " + type);
    }

}
