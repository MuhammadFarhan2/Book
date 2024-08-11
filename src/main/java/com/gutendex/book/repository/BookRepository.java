package com.gutendex.book.repository;

import com.gutendex.book.mode.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
//    @Query(value = "INSERT INTO BookEntity (author_name) VALUES (:authorName)", nativeQuery = true)
//    void saveAllAuthors(@Param("authorName") String authorName);
//
//    @Query("SELECT authorName FROM BookEntity")
//    List<String> findAllAuthorNames();
}
