package com.ftsd.folio.image;

import com.ftsd.folio.util.*;
import jakarta.persistence.*;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_image")
public class Image extends AuditModel {

    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String url;
    private String description;
    private String filename;
    private long size;

    @Enumerated(EnumType.STRING)
    private Type type;
    
}
