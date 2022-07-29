package md.dev.facade;

import md.dev.action.Action;
import md.dev.factory.ApplicationFactories;
import md.dev.factory.ApplicationFactoriesBuilder;
import md.dev.factory.FilterOperatorFactory;
import md.dev.log.LoggerHandler;
import md.dev.modifier.filter.Filter;
import md.dev.modifier.filter.operator.FilterOperator;
import md.dev.modifier.filter.operator.LeafFilterOperator;
import md.dev.modifier.transformer.Transformer;
import md.dev.pipeline.TriggerOutputProcessor;
import org.junit.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

abstract class AbstractApplicationLoaderBaseTest {
    FilterOperatorFactory filterOperationFactory;
    ApplicationFactories applicationFactories;

    protected void init() {
        ApplicationFactoriesBuilder applicationFactoriesBuilder = new ApplicationFactoriesBuilder();
        applicationFactoriesBuilder.addPackage(new HashSet<>(Arrays.asList(
                new File("src/main/resources/base-plugin-manifest.property")
        )));
        applicationFactories = applicationFactoriesBuilder.getApplicationFactories();
        filterOperationFactory = new FilterOperatorFactory();
        LoggerHandler.disableLog();
    }

    protected void testPipelineNum(int wantedPipelineNum, File file) {
        List<ApplicationLoader> applicationLoaders =
                FileToPipelineConfigurationConverter.convert(file, applicationFactories);

        Assert.assertEquals(wantedPipelineNum, applicationLoaders.size());
    }


    protected void testClassesInPipelines(
            File file, List<TriggerOutputProcessor>... wantedClasses) {
        for (int i = 0; i < wantedClasses.length; i++) {
            testClassesInPipelineNum(wantedClasses[i], file, i);
        }
    }

    protected void testClassesInPipelineNum(
            List<TriggerOutputProcessor> wantedClasses,
            File file,
            int pipelineNum) {
        List<ApplicationLoader> applicationLoaders =
                FileToPipelineConfigurationConverter.convert(file, applicationFactories);

        List<TriggerOutputProcessor> givenClasses = new ArrayList<>();
        applicationLoaders.get(pipelineNum).build().iterator().forEachRemaining(givenClasses::add);

        for (int i = 0; i < givenClasses.size(); i++) {
            isSameClass(wantedClasses.get(i), givenClasses.get(i));
        }
    }

    protected void isSameClass(
            TriggerOutputProcessor wantedObject, TriggerOutputProcessor givenObject) {
        if (givenObject instanceof Filter ||
                givenObject instanceof Action ||
                givenObject instanceof Transformer) {
            Assert.assertEquals(wantedObject.getClass(), givenObject.getClass());
        } else {
            FilterOperator<TriggerOutputProcessor> wantedFilterOperator =
                    (FilterOperator) wantedObject;
            FilterOperator<TriggerOutputProcessor> givenFilterOperator =
                    (FilterOperator) givenObject;

            if (wantedFilterOperator.getClass().isAssignableFrom(LeafFilterOperator.class) &&
                    givenFilterOperator.getClass().isAssignableFrom(LeafFilterOperator.class)) {
                Assert.assertEquals(
                        wantedFilterOperator.getFilter().getClass(),
                        givenFilterOperator.getFilter().getClass()
                );
            } else {
                for (int i = 0; i < wantedFilterOperator.getOperators().size(); ++i) {
                    isSameClass(
                            wantedFilterOperator.getOperators().get(i),
                            givenFilterOperator.getOperators().get(i)
                    );
                }
            }
        }
    }
}
