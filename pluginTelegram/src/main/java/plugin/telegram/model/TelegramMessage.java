package plugin.telegram.model;

import lombok.Data;

@Data
public class TelegramMessage {
    private long message_id;
    private TelegramFrom from;
    private TelegramChat chat;
    private long date;
    private String text;

}
