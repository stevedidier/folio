package com.ftsd.folio.image;

//import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.MediaType;

// import jakarta.validation.Valid;

// import java.io.File;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ImageController {

    // public static String uploadDirectory = "../../src/main/resources/uploads";

    // //@PostMapping(value = {"/api/v1/auth/upload"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    // @PostMapping("/api/v1/auth/upload")
    // public String upload(@RequestHeader HttpHeaders header, @RequestParam("file") MultipartFile file){
        
    //     StringBuilder fileNames = new StringBuilder();
    //     Path fileNamePath = Paths.get(uploadDirectory, file.getOriginalFilename());
    //     Files.write(fileNamePath, file.getBytes(), null);
    // }
    
}
