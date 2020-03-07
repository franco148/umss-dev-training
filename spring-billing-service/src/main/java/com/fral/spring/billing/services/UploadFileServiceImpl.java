package com.fral.spring.billing.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements UploadFileService {
	
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final static String UPLOADS_FOLDER = "uploads";
	

	@Override
	public Resource load(String fileName) throws MalformedURLException {
		
		Path pathPhoto = getPath(fileName);
		logger.info("pathPhoto: " + pathPhoto);
		
		Resource resource = new UrlResource(pathPhoto.toUri());
		
		if (!resource.exists() || !resource.isReadable()) {
			throw new RuntimeException("Error: the image can not be loaded: " + pathPhoto.toString());
		}
		return resource;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		
		String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		Path rootPath = getPath(uniqueFileName);
		
		logger.info("rootPath: " + rootPath);
		
		Files.copy(file.getInputStream(), rootPath);
		
		return uniqueFileName;
	}

	@Override
	public boolean delete(String fileName) {
		
		Path rootPath = getPath(fileName);
		File file = rootPath.toFile();
		
		if (file.exists() && file.canRead()) {
			if (file.delete()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void deleteAll() {
		
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
	}

	
	private Path getPath(String fileName) {
		return Paths.get(UPLOADS_FOLDER).resolve(fileName).toAbsolutePath();
	}
}
