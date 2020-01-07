package me.kickscar.practices.jpa03.model08.repository;


import me.kickscar.practices.jpa03.model08.dto.BlogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface JpaBlogQryDslRepository {
    Page<BlogDto> findAll2(Optional<String> blogName, Optional<String> userName, Pageable pageable);
    Page<BlogDto> findAll3(Optional<String> blogName, Optional<String> userName, Pageable pageable);
    Page<BlogDto> findAll4(Optional<String> blogName, Optional<String> userName, Pageable pageable);
}
