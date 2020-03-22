package Zad1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class Futil
{
    static void processDir(String dirName, String resutlFileName)
    {
        try
        {
            Charset inCharset = Charset.forName("Cp1250");
            Charset outCharset = StandardCharsets.UTF_8;
            FileChannel outChannel = new FileOutputStream(resutlFileName).getChannel();

            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException
                {
                    FileChannel inChannel = new FileInputStream(new File(path.toString())).getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate((int)inChannel.size());
                    inChannel.read(buffer);
                    buffer.flip();
                    CharBuffer cBuffer = inCharset.decode(buffer);
                    buffer = outCharset.encode(cBuffer);
                    outChannel.write(buffer);
                    inChannel.close();
                    return FileVisitResult.CONTINUE;
                }
            });
            outChannel.close();
            System.out.println("Finished");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
