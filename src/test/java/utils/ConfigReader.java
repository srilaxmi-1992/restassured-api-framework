package utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    public static Properties getConfigProps() throws IOException {
        FileReader fileReader = new FileReader("src/test/resources/config.properties");
        Properties props = new Properties();
        props.load(fileReader);
        return props;
    }
}
