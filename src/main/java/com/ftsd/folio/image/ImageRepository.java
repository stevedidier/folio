package com.ftsd.folio.image;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByType(Type type);
    
}
