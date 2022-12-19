package us.group14.backend.email;

public interface EmailSender {
    void send(String to, String content);
}
