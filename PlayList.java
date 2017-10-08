import java.nio.file.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;


public class PlayList
{
	public static void main(String[] args)
	{
		Tracks trax = new Tracks();
		trax.getMusicFiles(args[0]);
		trax.shuffle();
		trax.writePlayList(args[1]);
	}
}

class Tracks
{
	private ArrayList<Track> tracks;
	private PlayListFile m3uFile;
	private MusicFilesVisitor musicFiles;


	public Tracks()
	{
		tracks = new ArrayList<Track>();
		musicFiles = new MusicFilesVisitor();
	}
	public void getMusicFiles(String path)
	{
		Path start = Paths.get(path);
		try
		{	Files.walkFileTree(start, musicFiles);	}
		catch(Exception e) {	e.printStackTrace();	}
		ArrayList<String> files = musicFiles.getFileNames();
		// Place for get track's TAGs
		files = MusicFileNameParser.cropFileNames(files);
		for (String name : files)
		{
			String trackPath = name.substring(0, name.lastIndexOf('/'));
			String trackFileName = name.substring(name.lastIndexOf('/') + 1, name.length());
			tracks.add(new Track(trackFileName, trackPath, ""));
		}			
	}
	public void writePlayList(String name)
	{
		m3uFile = new PlayListFile(name);
		m3uFile.writePLFile();
		for (Track trk : tracks)
			m3uFile.writeString(trk.getPath() + '/' + trk.getFileName());
		m3uFile.closePLFile();
	}
	public void shuffle()
	{
		Collections.shuffle(tracks);
	}
	public void addTrack(Track t)
	{
		tracks.add(t);
	}
}

class Track
{
	private String fileName;
	private String path;
	private String artist;
	public Track()
	{
		fileName = "";
		path = "";
		artist = "";
	}
	public Track(String fn, String pt, String at)
	{
		fileName = fn;
		path = pt;
		artist = at;			
	}
	public String getFileName()
	{
		return fileName;
	}
	public String getPath()
	{
		return path;
	}
	public String getArtist()
	{
		return artist;
	}
}

class MusicFilesVisitor extends SimpleFileVisitor<Path> 	// Класс для рекурсивного обхода файлов
{
	private ArrayList<String> fnames;
	private static final String[] extensions = {".mp3", ".flac", ".ape"};


	public MusicFilesVisitor()
	{
		fnames = new ArrayList<String>();
	}
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException 
	{
		String pt = path.toString();
		for (String ext : extensions)
			if(pt.endsWith(ext)) {
				fnames.add(pt);
				break;
			}
		return FileVisitResult.CONTINUE;
	}
	public ArrayList<String> getFileNames()
	{
		return fnames;
	}
}
class MusicFileNameParser
{
	public static Track parceTrackFromFileName(String fname)
	{
		return null;
	}
	public static ArrayList<String> cropFileNames(ArrayList<String> fnames)
	{
		ArrayList<String> crpNams = new ArrayList<String>();
		for (String name : fnames)
			crpNams.add(name.substring(name.indexOf("NEW/"), name.length()));
		return crpNams;
	}
}
class PlayListFile
{
	private static final String m3uTag = "#EXTM3U\n";
	private String fname;
	private BufferedWriter fileWriter;

	public PlayListFile(String name)
	{
		fname = name;
	}
	public void writePLFile()
	{
		try // (FileWriter file = new FileWriter(fname, false))
		{
			fileWriter = new BufferedWriter(new FileWriter(fname));
			fileWriter.write(m3uTag);
		}
		catch(IOException ex){
			System.out.println(ex.getMessage());
		} 	
	}
	public void writeString(String str)
	{
		try//(FileWriter file = new FileWriter(fname, true)) 
		{
			fileWriter.write(str + '\n');
		} catch (IOException e) {e.printStackTrace(); }
	}
	public void closePLFile() {
		try {
		fileWriter.close();
		} catch(IOException ex) {
			System.out.println("Can't write file");
			ex.printStackTrace();
		}
	}
}
