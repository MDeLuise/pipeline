package md.dev.facade;

import md.dev.factory.ApplicationFactories;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileToPipelineConfigurationConverter {

    public static List<ApplicationLoader> convert(
            File file,
            ApplicationFactories applicationFactories) {
        String filePath = file.getAbsolutePath();
        String fileExtension = filePath.split("\\.")[filePath.split("\\.").length - 1];
        String fileContent = null;
        try {
            fileContent = Files.readString(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convert(fileContent, fileExtension, applicationFactories);
    }


    public static List<ApplicationLoader> convert(
            String string,
            String contentFormat,
            ApplicationFactories applicationFactories) {
        String stringToConvert = string;
        if ("yaml".equals(contentFormat)) {
            stringToConvert = YamlToJson.convertYamlToJson(string);
        }

        return convertJson(stringToConvert, applicationFactories);
    }

    private static List<ApplicationLoader> convertJson(
            String fileString,
            ApplicationFactories applicationFactories) {
        List<ApplicationLoader> applicationLoaders = new ArrayList<>();
        JSONArray jsonConfigurations = new JSONArray(fileString);
        for (Object jsonConfigurationObject : jsonConfigurations) {
            JSONObject jsonConfiguration = (JSONObject) jsonConfigurationObject;
            applicationLoaders.add(
                    new ApplicationLoader(jsonConfiguration, applicationFactories)
            );
        }
        return applicationLoaders;
    }
}
