
package com.ecommerce.book_store.service.abstraction;

public interface MailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithTemplate(String to, String subject, String templateName, Object model);
}
