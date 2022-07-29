package md.dev.webapi.configuration;

import lombok.Data;

@Data
public class WebApiResponse {
    private final int code;
    private final String response;

    @SuppressWarnings("checkstyle:MagicNumber")
    public boolean isOk() {
        return code == 200;
    }
}
