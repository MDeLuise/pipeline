package md.dev.processor.filter;

import md.dev.options.exception.OptionMismatchTypeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import plugin.base.processor.filter.GreaterThenFilter;

import java.util.HashMap;

public class GreaterThenFilterTest extends BaseFilterTest {

    @Before
    public void init() {
        super.init();
        filter = new GreaterThenFilter();
    }

    @Test
    public void shouldFilterTrueInt() {
        int value = 1;
        Mockito.when(triggerOutput.getValue()).thenAnswer(foo -> value);
        insertInMockedOptions("compareValue", value - 1);
        filter.loadOptions(options);
        Assert.assertTrue(filter.filter(triggerOutput));
    }

    @Test
    public void shouldFilterFalseInt() {
        int value = 1;
        Mockito.when(triggerOutput.getValue()).thenAnswer(foo -> value);
        insertInMockedOptions("compareValue", value);
        filter.loadOptions(options);
        Assert.assertFalse(filter.filter(triggerOutput));
    }

    @Test
    public void shouldFilterTrueString() {
        String value = "ab";
        Mockito.when(triggerOutput.getValue()).thenAnswer(foo -> value);
        insertInMockedOptions("compareValue", "aa");
        filter.loadOptions(options);
        Assert.assertTrue(filter.filter(triggerOutput));
    }

    @Test
    public void shouldFilterFalseString() {
        String value = "aa";
        Mockito.when(triggerOutput.getValue()).thenAnswer(foo -> value);
        insertInMockedOptions("compareValue", "ab");
        filter.loadOptions(options);
        Assert.assertFalse(filter.filter(triggerOutput));
    }

    @Test
    public void shouldAcceptUnknownOption() {
        insertInMockedOptions("foo", "bar");
        insertInMockedOptions("compareValue", "foo");
        filter.loadOptions(options);
    }

    @Test(expected = OptionMismatchTypeException.class)
    public void shouldThrowExceptionIfWrongOptionType() {
        insertInMockedOptions("compareValue", new HashMap<>());
        filter.loadOptions(options);
    }
}
