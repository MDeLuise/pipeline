package md.dev.factory;

import lombok.SneakyThrows;
import md.dev.processor.transformer.Transformer;

import java.lang.reflect.Constructor;
import java.util.List;

public class TransformerFactory extends EntityFactory<Transformer> {
    public TransformerFactory(String prefix) {
        super(prefix);
    }


    @SneakyThrows
    @Override
    protected Transformer create(FactoryConfiguration factoryConfiguration,
                                 Constructor constructor,
                                 List<Object> defaultParams) {
        return (Transformer) constructor.newInstance();
    }

}
