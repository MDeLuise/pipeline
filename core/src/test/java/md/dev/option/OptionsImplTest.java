package md.dev.option;

import md.dev.options.Options;
import md.dev.options.OptionsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptionsImplTest {
    private Options options;

    @Before
    public void init() {
        options = new OptionsImpl();
    }


    @Test
    public void shouldAddValueCorrectly() {
        String key = "foo";
        String value = "bar";
        options.add(key, value);
        Assert.assertEquals(value, options.get(key));
    }

    @Test
    public void shouldOverrideValueCorrectly() {
        String key = "foo";
        String value = "bar";
        String newValue = "new";
        options.add(key, value);
        options.add(key, newValue);
        Assert.assertEquals(newValue, options.get(key));
    }

    @Test
    public void shouldHasValuesCorrectlyTrue() {
        String key = "foo";
        String value = "bar";
        options.add(key, value);
        Assert.assertTrue(options.has(key));
    }

    @Test
    public void shouldHasValuesCorrectlyFalse() {
        String key = "foo";
        String value = "bar";
        options.add(key, value);
        Assert.assertFalse(options.has("taz"));
    }

    @Test
    public void shouldParseStringValueCorrectly() {
        String key = "foo";
        String value = "bar";
        options.add(key, value);
        Assert.assertEquals(value, options.getString(key));
    }

    @Test
    public void shouldParseIntValueCorrectly() {
        String key = "foo";
        int value = 42;
        options.add(key, value);
        Assert.assertEquals(value, options.getInt(key));
    }

    @Test(expected = ClassCastException.class)
    public void shouldErrorWhenParseWrongType() {
        String key = "foo";
        int value = 42;
        options.add(key, value);
        Assert.assertEquals(value, options.getString(key));
    }
}
