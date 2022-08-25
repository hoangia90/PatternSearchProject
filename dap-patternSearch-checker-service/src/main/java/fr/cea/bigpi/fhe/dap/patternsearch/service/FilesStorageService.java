package fr.cea.bigpi.fhe.dap.patternsearch.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
	public void init();

	public void save(MultipartFile file);
	
	public void save(MultipartFile file, String filename);

	public void saveAll(MultipartFile[] file);

	public Resource load(String filename);

	public void deleteAll();

	public Stream<Path> loadAll();

	public Path getFileDir();

	public void setFileDir(Path fileDir);
}

