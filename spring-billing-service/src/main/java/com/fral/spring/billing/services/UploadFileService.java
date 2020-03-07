package com.fral.spring.billing.services;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
	
	Resource load(String fileName) throws MalformedURLException;
	String copy(MultipartFile file) throws IOException;
	boolean delete(String fileName);
	void deleteAll();
	void init() throws IOException;
}
