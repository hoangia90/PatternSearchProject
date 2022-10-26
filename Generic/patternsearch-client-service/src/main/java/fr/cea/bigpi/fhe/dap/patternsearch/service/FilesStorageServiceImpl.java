package fr.cea.bigpi.fhe.dap.patternsearch.service;

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
//	private final Path fileDir = Paths.get("cingulata/upload/test");
//	private final Path fileDir = Paths.get("seal/upload/test");
//	private Path fileDir = Paths.get("/home/hgnguyen/Desktop/seal/upload/");
	@Value("${application.seal.uploadDir}")
	private String fileDirStr; 
	
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

}
