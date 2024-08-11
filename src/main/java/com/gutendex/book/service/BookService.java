package com.gutendex.book.service;

import com.gutendex.book.mode.BookEntity;
import com.gutendex.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public void fetchAndStoreAuthors(String authorName) {

            BookEntity newBook = new BookEntity(authorName);
            bookRepository.save(newBook);
    }

    public List<String> getAuthorNames() {
        List<String> authorNames = new ArrayList<>();
        List<BookEntity> all = bookRepository.findAll();
        if (all!= null) {
            for (BookEntity book :all) {
                authorNames.add(book.getAuthorName());
            }
        }
        return authorNames;
    }

    public void flushData() {
         bookRepository.deleteAll();
    }
}
