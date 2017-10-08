import org.jaudiotagger.*;
import org.jaudiotagger.audio.*;
import org.jaudiotagger.tag.*;
import java.io.*;


public class Test {
	public static void main(String[] args) {
		try {
			File testfile = new File("/media/fluxx/Music/Music/!Проработать/Грот - 2017 - Клавиши/03 - Наследство (Акустика).ape");
			AudioFile f = AudioFileIO.read(testfile);
			Tag tag = f.getTag();
			System.out.println(tag.getFirst(FieldKey.ARTIST));
		} catch(Exception e) {e.printStackTrace();}
	}
}
