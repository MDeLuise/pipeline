package md.dev.pipeline;

public interface RuntimeConfigurableEntity {
    boolean isConfigured();

    void configure();
}
