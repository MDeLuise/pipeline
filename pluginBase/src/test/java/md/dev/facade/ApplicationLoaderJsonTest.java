package md.dev.facade;

import md.dev.processor.filter.operator.FilterOperator;
import md.dev.processor.transformer.Transformer;
import md.dev.pipeline.TriggerOutputProcessor;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ApplicationLoaderJsonTest extends AbstractApplicationLoaderBaseTest{
    final String examplesPath = "src/main/resources/examples/json/%s.json";


    @Before
    public void setup() {
        super.init();
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectNumOfPipelineCreated_0() {
        String pathExample = String.format(examplesPath, "example0");

        testPipelineNum(1, new File(pathExample));
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectPipelineCreated_0() {
        String pathExample = String.format(examplesPath, "example0");

        List<TriggerOutputProcessor> wantedClasses = Arrays.asList(
                applicationFactories.getTransformerFactories().build("objToStr"),
                applicationFactories.getActionFactories().build("print")
        );
        testClassesInPipelines(new File(pathExample), wantedClasses);
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectNumOfPipelineCreated_1() {
        String pathExample = String.format(examplesPath, "example1");

        testPipelineNum(1, new File(pathExample));
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectPipelineCreated_1() {
        String pathExample = String.format(examplesPath, "example1");

        FilterOperator filterOperator = filterOperationFactory.build("or");
        FilterOperator filterOperator1 = filterOperationFactory.build("not");
        FilterOperator filterOperator2 = filterOperationFactory.build("leaf");
        FilterOperator filterOperator3 = filterOperationFactory.build("leaf");

        filterOperator2.insertFilter(applicationFactories.getFilterFactories().build("eq"));
        filterOperator3.insertFilter(applicationFactories.getFilterFactories().build("eq"));
        filterOperator1.insertOperator(filterOperator3);
        filterOperator.insertOperator(filterOperator1);
        filterOperator.insertOperator(filterOperator3);

        List<TriggerOutputProcessor> wantedClasses = Arrays.asList(
                filterOperator,
                applicationFactories.getTransformerFactories().build("objToStr"),
                applicationFactories.getActionFactories().build("print")
        );

        testClassesInPipelines(new File(pathExample), wantedClasses);
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectNumOfPipelineCreated_2() {
        String pathExample = String.format(examplesPath, "example2");

        testPipelineNum(1, new File(pathExample));
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectPipelineCreated_2() {
        String pathExample = String.format(examplesPath, "example2");

        FilterOperator filterOperator = filterOperationFactory.build("not");
        FilterOperator filterOperator1 = filterOperationFactory.build("leaf");
        filterOperator1.insertFilter(applicationFactories.getFilterFactories().build("eq"));
        filterOperator.insertOperator(filterOperator1);

        List<TriggerOutputProcessor> wantedClasses = Arrays.asList(
                filterOperator,
                applicationFactories.getTransformerFactories().build("objToStr"),
                applicationFactories.getActionFactories().build("print")
        );

        testClassesInPipelines(new File(pathExample), wantedClasses);
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectNumOfPipelineCreated_3() {
        String pathExample = String.format(examplesPath, "example3");

        testPipelineNum(2, new File(pathExample));
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectPipelineCreated_3() {
        String pathExample = String.format(examplesPath, "example3");

        FilterOperator filterOperator = filterOperationFactory.build("not");
        FilterOperator filterOperator1 = filterOperationFactory.build("leaf");
        filterOperator1.insertFilter(applicationFactories.getFilterFactories().build("eq"));
        filterOperator.insertOperator(filterOperator1);

        List<TriggerOutputProcessor> wantedClass1 = Arrays.asList(
                filterOperator,
                applicationFactories.getTransformerFactories().build("objToStr"),
                applicationFactories.getActionFactories().build("print")
        );

        List<TriggerOutputProcessor> wantedClass2 = Arrays.asList(
                applicationFactories.getTransformerFactories().build("objToStr"),
                applicationFactories.getActionFactories().build("print")
        );

        testClassesInPipelines(new File(pathExample), wantedClass1, wantedClass2);
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectNumOfPipelineCreated_4() {
        String pathExample = String.format(examplesPath, "example4");

        testPipelineNum(1, new File(pathExample));
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectPipelineCreated_4() {
        String pathExample = String.format(examplesPath, "example4");

        Transformer transformer = applicationFactories.getTransformerFactories().build("regexExt");

        List<TriggerOutputProcessor> wantedClass1 = Arrays.asList(
                transformer, applicationFactories.getActionFactories().build("print")
        );

        testClassesInPipelines(new File(pathExample), wantedClass1);
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectNumOfPipelineCreated_5() {
        String pathExample = String.format(examplesPath, "example5");

        testPipelineNum(1, new File(pathExample));
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectPipelineCreated_5() {
        String pathExample = String.format(examplesPath, "example5");

        Transformer transformer = applicationFactories.getTransformerFactories().build("intThr");

        List<TriggerOutputProcessor> wantedClass1 = Arrays.asList(
                transformer, applicationFactories.getActionFactories().build("print")
        );

        testClassesInPipelines(new File(pathExample), wantedClass1);
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectNumOfPipelineCreated_6() {
        String pathExample = String.format(examplesPath, "example6");

        testPipelineNum(1, new File(pathExample));
    }

    @Test
    public void givenCorrectFile_whenLoaded_thenCorrectPipelineCreated_6() {
        String pathExample = String.format(examplesPath, "example6");

        List<TriggerOutputProcessor> wantedClass1 = Arrays.asList(
                applicationFactories.getActionFactories().build("print")
        );

        testClassesInPipelines(new File(pathExample), wantedClass1);
    }

}
