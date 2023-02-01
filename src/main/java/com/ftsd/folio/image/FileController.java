package com.ftsd.folio.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/api/v1/imagesList") 
    public ResponseEntity<List<ImageDto>> getAllImages(@RequestHeader HttpHeaders header) {
        return new ResponseEntity<List<ImageDto>>(imageService.getAllImages(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/auth/publicImages") 
    public ResponseEntity<List<ImageDto>> getPublicImages(@RequestHeader HttpHeaders header) {
        return new ResponseEntity<List<ImageDto>>(imageService.getPublicImages(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/images/{imageId}")
    public ResponseEntity<ImageDto> getImageId(@RequestHeader HttpHeaders header, @PathVariable (value = "imageId") Integer imageId) {

        return new ResponseEntity<>(imageService.getImagesId(imageId), HttpStatus.OK);
    }

    @PutMapping("/api/v1/admin/images/{imageId}")
    public ResponseEntity<?> updateImage(@RequestHeader HttpHeaders header, @PathVariable Integer imageId, @Valid @RequestBody ImageDto userRequest) {
        
        return imageService.update(userRequest,imageId ); 
    }

    @DeleteMapping("/api/v1/admin/images/{imageId}")
    public ResponseEntity<?> deleteImage(@RequestHeader HttpHeaders header, @PathVariable Integer imageId) {

        imageService.delete(imageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = {"/api/v1/admin/upload"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ImageDto addImage( @RequestPart("image") ImageDto image, @RequestPart("file") MultipartFile file ){

        String fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = null;

        if(image.getType().equalsIgnoreCase(Type.PUBLIC.name()) ){
             fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/auth/downloadFile/")
                .path(fileName)
                .toUriString();
        }else if(image.getType().equalsIgnoreCase(Type.PRIVATE.name()) ){
            fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        }
        
        image.setUrl(fileDownloadUri);
        image.setSize(file.getSize());
        image.setFilename(fileName);
        imageService.addImage(image);
       
        return image;
    }
    
    @PostMapping("/api/v1/admin/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/api/v1/admin/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/api/v1/auth/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadPublicFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}