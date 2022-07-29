package md.dev.trigger;

import md.dev.webapi.WebApi;
import md.dev.webapi.configuration.WebApiResponse;
import org.mockito.Mockito;

abstract class BaseWebTriggerUnitTest extends BaseTriggerUnitTest {
    protected WebApi webApi;
    protected WebApiResponse webApiResponse;

    public void init() {
        super.init();
        webApi = Mockito.mock(WebApi.class);
        webApiResponse = Mockito.mock(WebApiResponse.class);
        Mockito.when(webApi.perform()).thenReturn(webApiResponse);
    }

    public void setWebResponseValue(String value) {
        Mockito.when(webApiResponse.getResponse()).thenAnswer(foo -> value);
        Mockito.when(webApiResponse.isOk()).thenAnswer(foo -> true);
    }
}
