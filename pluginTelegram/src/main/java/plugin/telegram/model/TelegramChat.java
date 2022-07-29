package plugin.telegram.model;

import lombok.Data;

@Data
public class TelegramChat {
    private long id;
    private String first_name;
    private String username;
    private String type;
}
