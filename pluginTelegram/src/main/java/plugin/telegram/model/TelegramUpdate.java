package plugin.telegram.model;

import lombok.Data;

@Data
public class TelegramUpdate {
    private long update_id;
    private TelegramMessage message;
}
