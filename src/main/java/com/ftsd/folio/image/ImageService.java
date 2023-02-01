package com.ftsd.folio.image;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.ftsd.folio.exception.MessageResponse;
import com.ftsd.folio.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import com.ftsd.folio.util.Mappers;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ImageService { 

    private final ImageRepository repository;
    private final FileStorageService fileStorageService;

    public List<ImageDto> getAllImages(){

        List<ImageDto> imageList = new ArrayList<ImageDto>();

        for (Image image : repository.findAll()) {

            imageList.add(Mappers.toImageDto(image));
            
          }
      
          return imageList;

      }


      public List<ImageDto> getPublicImages(){

        List<ImageDto> imageList = new ArrayList<ImageDto>();

        for (Image image : repository.findByType(Type.PUBLIC)) {

            imageList.add(Mappers.toImageDto(image));
            
          }
      
          return imageList;

      }


    public ImageDto getImagesId(Integer ImageId){
    

        Image image = repository.findById(ImageId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Image with id = " + ImageId));
    
        
        return Mappers.toImageDto(image);
    }

    public ResponseEntity<?> addImage(ImageDto request) {

        ResponseEntity<?> responseEntity = ResponseEntity.badRequest().body(new MessageResponse("TypeError: Type doesn't exist"));

        if(request.getType().equalsIgnoreCase(Type.PUBLIC.name()) ){

            var image = Image.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .url(request.getUrl())
            .size(request.getSize())
            .type(Type.PUBLIC) 
            .filename(request.getFilename())
            .build();
          image.setCreatedAt(new Date());
          image.setUpdatedAt(new Date());
          repository.save(image);
          responseEntity = ResponseEntity.ok(new MessageResponse(" New Image created successfully"));
   
        }else if(request.getType().equalsIgnoreCase(Type.PRIVATE.name()) ){

            var image = Image.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .url(request.getUrl())
            .size(request.getSize())
            .filename(request.getFilename())
            .type(Type.PRIVATE) 
            .build();
          image.setCreatedAt(new Date());
          image.setUpdatedAt(new Date());
          repository.save(image);
          responseEntity = ResponseEntity.ok(new MessageResponse(" New Image created successfully"));
   

        }


        return responseEntity;
    }


    public ResponseEntity<?> update(ImageDto request, Integer imageId){

        ResponseEntity<?> responseEntity = ResponseEntity.ok(new MessageResponse("Image updated successfully"));

    
        if(request.getType().equalsIgnoreCase(Type.PUBLIC.name()) ){
    
          Integer id = repository.findById(imageId).map(image -> {
            image.setTitle(request.getTitle());
            image.setDescription(request.getDescription());
            //image.setUrl(request.getUrl());
            image.setUpdatedAt(new Date());
            image.setType(Type.PUBLIC);
            repository.save(image);
    
            return image.getId();
    
        }).orElseThrow(() -> new ResourceNotFoundException("ImageId " + imageId + " not found"));
    
        }else if(request.getType().equalsIgnoreCase(Type.PRIVATE.name()) ){

            Integer id = repository.findById(imageId).map(image -> {
                image.setTitle(request.getTitle());
                image.setDescription(request.getDescription());
                //image.setUrl(request.getUrl());
                image.setUpdatedAt(new Date());
                image.setType(Type.PRIVATE);
                repository.save(image);
        
                return image.getId();
        
            }).orElseThrow(() -> new ResourceNotFoundException("ImageId " + imageId + " not found"));
          
         
        }else{
            responseEntity = ResponseEntity.badRequest().body(new MessageResponse("ImageError: invalid image Type   "));
        }
    
        return responseEntity;
    }

    public void delete(Integer imageId){

        int i = repository.findById(imageId).map(image -> {

            fileStorageService.delete(image.getFilename());
            repository.delete(image);
            return 0;
        }).orElseThrow(() -> new ResourceNotFoundException("ImageId " + imageId + " not found"));
     }
    
}
