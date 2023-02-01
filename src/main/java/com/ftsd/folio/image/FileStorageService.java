package com.ftsd.folio.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.util.FileSystemUtils;
import com.ftsd.folio.exception.FileStorageException;
import com.ftsd.folio.exception.MyFileNotFoundException;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;

@Service
public class FileStorageService {
    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads";
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService() {
        this.fileStorageLocation = Paths.get(uploadDirectory)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            String randomName = RandomStringUtils.randomAlphanumeric(30);
            String extension = fileName.split("\\.")[1];
            String fileRandomName = randomName +"."+extension;

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileRandomName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileRandomName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.fileStorageLocation, 1).filter(path -> !path.equals(this.fileStorageLocation)).map(this.fileStorageLocation::relativize);
        } catch (IOException e) {	return null; }

    }

    public Path load(String filename) {
        return fileStorageLocation.resolve(filename);
        }


    public void deleteAll() {
        FileSystemUtils.deleteRecursively(fileStorageLocation.toFile());
    }

    public void delete(String filename) {

        Path path = load(filename);
        FileSystemUtils.deleteRecursively(path.toFile());
    }
}

