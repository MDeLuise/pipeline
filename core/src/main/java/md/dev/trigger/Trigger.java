package md.dev.trigger;


import md.dev.options.description.OptionsLoadableEntity;
import md.dev.pipeline.Pipeline;
import md.dev.pipeline.PipelineEntity;
import md.dev.state.StatefulEntity;

public interface Trigger<T> extends OptionsLoadableEntity, PipelineEntity, StatefulEntity {
    void startListening();

    void linkPipeline(Pipeline pipeline);

    void unlinkPipeline(Pipeline pipeline);
}
