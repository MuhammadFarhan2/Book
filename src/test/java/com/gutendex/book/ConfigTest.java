package com.gutendex.book;

import com.gutendex.book.controller.BookController;
import com.gutendex.book.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.awt.print.Book;

@Configuration
public class ConfigTest {

//    @Bean
//    public RestTemplate restTemplate(){
//        return new RestTemplate();
//    }
    @Bean
    public BookService bookService(){
        return new BookService();
    }
    @Bean
    public BookController bookController(){
        return  new BookController();
    }

}

