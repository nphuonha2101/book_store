package com.ecommerce.book_store.service.abstraction;

import java.io.InputStream;

public interface VoskService {
    public String transcribeAudio(InputStream audioStream);
}
