package com.ftsd.folio.image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ImageDto {

    private Integer id;
    private String title;
    private String url;
    private String description;
    private String type;
    private long size;
    private String filename;
   
    
}
