package plugin.telegram.model;

import lombok.Data;

@Data
public class TelegramFrom {
    private long id;
    private String is_bot;
    private String first_name;
    private String username;
    private String language_code;
}
