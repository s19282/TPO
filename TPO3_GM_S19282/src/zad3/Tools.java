/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad3;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class Tools
{
    public static Options createOptionsFromYaml(String path)
    {
        try(InputStream is = new FileInputStream(path))
        {
            Yaml yaml = new Yaml(new Constructor(Options.class));
            return yaml.load(is);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new Options();
    }

}
