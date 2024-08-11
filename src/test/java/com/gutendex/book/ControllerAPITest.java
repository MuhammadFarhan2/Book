package com.gutendex.book;

import com.gutendex.book.controller.BookController;
import com.gutendex.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
//@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@WebMvcTest(BookController.class)
class ControllerAPITest   {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAuthorNames() {
        // Mocking behavior of bookService
        List<String> authorNames = Arrays.asList("Author1", "Author2");
        when(bookService.getAuthorNames()).thenReturn(authorNames);

        // Testing the method
        ResponseEntity<List<String>> responseEntity = bookController.getAuthorNames();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(authorNames, responseEntity.getBody());

        // Verifying method calls
        verify(bookService, times(3)).getAuthorNames();
    }

    @Test
    void testFetchAndStoreAuthors() {
        // Mocking behavior of restTemplate
        when(restTemplate.getForObject(anyString(), any())).thenReturn(Collections.singletonMap("results", Collections.emptyList()));

        // Testing the method
        ResponseEntity<String> responseEntity = bookController.fetchAndStoreAuthors();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String msg =  responseEntity.getBody().toString();
        int numberOfRecords = 0;
        // Define the pattern to match numbers
        Pattern pattern = Pattern.compile("\\d+");
        // Create a Matcher object
        Matcher matcher = pattern.matcher(msg);
        // Find and print all numbers in the input string
        // Find and store the first number in the input string
        if (matcher.find()) {
            numberOfRecords = Integer.parseInt(matcher.group());
        }

        assertEquals("Author's name fetched and stored successfully, Number of Records is: "+numberOfRecords,msg);

//        // Verifying method calls
//        verify(restTemplate, times(3)).getForObject(anyString(), any());
//        verify(bookService, times(3)).fetchAndStoreAuthors(any());
    }
}