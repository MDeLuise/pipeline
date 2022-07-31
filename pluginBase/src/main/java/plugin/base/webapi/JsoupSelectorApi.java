package plugin.base.webapi;

import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiConfiguration;
import md.dev.webapi.configuration.WebApiResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupSelectorApi implements WebApi {
    private String url;
    private String selector;


    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public WebApiResponse perform() {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new WebApiResponse(200, doc.select(selector).toString());
    }


    @Override
    public void configure(WebApiConfiguration configuration) {
        selector = (String) configuration.get("selector");
        url = (String) configuration.get("url");
    }
}
