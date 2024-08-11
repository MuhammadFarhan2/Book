package com.gutendex.book.controller;

import com.gutendex.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/fetchAndStoreAuthors")
    public ResponseEntity<String> fetchAndStoreAuthors() {
        try {
            int totalPages =   findNumberOfPages();
            ExecutorService executor = Executors.newFixedThreadPool((totalPages/4 +1));
            List<Callable<Void>> tasks = new ArrayList<>();
            int count = 0;
            for (int page = 1; page <= totalPages; page++) {
                final int currentPage = page;
                tasks.add(() -> {
                    String url = "https://gutendex.com/books/?page=" + currentPage;
                    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                    List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                    if (results != null) {
                        for (Map<String, Object> result : results) {
                            List<Map<String, Object>> authors = (List<Map<String, Object>>) result.get("authors");
                            if (authors != null) {
                                for (Map<String, Object> author : authors) {
                                    String name = (String) author.get("name");
                                    if (name != null) {
                                        System.out.println(currentPage);

                                        bookService.fetchAndStoreAuthors(name);
                                    }
                                }
                            }
                        }
                    }
                    return null;
                });
            }

            executor.invokeAll(tasks);
            executor.shutdown();

            return ResponseEntity.ok("Author's name fetched and stored successfully, Number of Records is: "+totalPages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch and store authors: " + e.getMessage());
        }
    }

    @GetMapping("/getAuthorNames")
    public ResponseEntity<List<String>> getAuthorNames() {
        try {
            if (!bookService.getAuthorNames().isEmpty()) {
                getFlushStoreData();
                List<String> authorNames = bookService.getAuthorNames();
                return ResponseEntity.ok(authorNames);
            }
            else {
                List<String> authorNames = bookService.getAuthorNames();
                return ResponseEntity.ok(authorNames);
            }

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }


    @GetMapping("/getTotalPages")
    public int findNumberOfPages() {
        int totalPage = 2279;
        boolean check = true;
        while(check){
            Map<String, Object> response = restTemplate.getForObject("https://gutendex.com/books/?page=" +totalPage, Map.class);
            if (response.containsKey("detail") && response.get("detail").equals("Invalid page.")) {
                totalPage--;
            }
            else if (response.get("next") != null) {
                totalPage++;
            }
            else {
                check=false;
            }
        }
        return totalPage;
    }


    @GetMapping("/flushData")
    public ResponseEntity<String> getFlushStoreData() {
        try {
                 bookService.flushData();
            if (bookService.getAuthorNames().isEmpty()) {
                return ResponseEntity.ok("the data is cleared!");
            }
            else
            {
            return ResponseEntity.ok("the data is not cleared!");

            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList().toString());
        }
    }



}