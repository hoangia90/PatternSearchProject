package fr.cea.bigpi.fhe.dap.patternsearch.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
	
	@Value("${application.seal.uploadDir}")
	private String fileDirStr; 
	// = "/home/hgnguyen/Desktop/seal/upload/";
	private Path fileDir; //= Paths.get(fileDirStr);

	@PostConstruct public void initService()
	{
		fileDir = Paths.get(fileDirStr);		
	}
	
	@Override
	public void setFileDir(Path fileDir) {
		this.fileDir = fileDir;
	}

	@Override
	public Path getFileDir() {
		return fileDir;
	}

	@Override
	public void init() {
		try {
			Files.createDirectory(fileDir);
			System.out.println(fileDir.toString());
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public void save(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.fileDir.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	public void save(MultipartFile file, String filename) {
		try {
			Files.copy(file.getInputStream(), this.fileDir.resolve(filename));
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public void saveAll(MultipartFile[] files) {
		// TODO Auto-generated method stub
		for (MultipartFile file : files) {
			save(file);
		}
	}

	@Override
	public Resource load(String filename) {
		try {
			Path file = fileDir.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(fileDir.toFile());
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.fileDir, 1).filter(path -> !path.equals(this.fileDir)).map(this.fileDir::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}

	public static File createEmptyFile(String dir, MultipartFile multipartFile, String name) {
		String originalName = multipartFile.getOriginalFilename();
		File file = new File(dir + originalName);
		try {
			final boolean newFile = file.createNewFile();
			if (newFile) {
				return file;
			}
			throw new IOException("Could not save empty file!");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void saveContain(MultipartFile multipartFile, File file) {

	    // Creates a file stream of the file created.
	    FileOutputStream fileOutputStream = null;
	    try {
	      fileOutputStream = new FileOutputStream(file);
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }

	    // If the stream of files is not null then it passes the data of the file passed.
	    if (fileOutputStream != null) {
	      try {
	        fileOutputStream.write(multipartFile.getBytes());
	      } catch (IOException e) {
	        e.printStackTrace();
	      }
	    }

	    // Closes the file stream.
	    try {
	      if (fileOutputStream != null) {
	        fileOutputStream.close();
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

}
