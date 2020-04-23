/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad4;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class Tools
{
    public static Options createOptionsFromYaml(String path) throws FileNotFoundException {
        InputStream is = new FileInputStream(path);
        Yaml yaml = new Yaml(new Constructor(Options.class));
        return yaml.load(is);
    }

}
