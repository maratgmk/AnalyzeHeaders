package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.HeadersService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("headers")
public class HeadersController {
    private final HeadersService headersService;

    public HeadersController(HeadersService headersService) {
        this.headersService = headersService;
    }

    @GetMapping
    public Map<String, String> headers(HttpServletRequest request, HttpServletResponse response) {
        Iterator<String> headerNames = request.getHeaderNames().asIterator();
        Map<String, String> headers = new HashMap<>();
        while (headerNames.hasNext()) {
            String headerName = headerNames.next();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        System.out.println(headers);

        try {
            var filtered = headersService.filterHeaders(headers);
            System.out.println(filtered);
            return filtered;

        } catch (NoSuchElementException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return null;
    }

    @GetMapping("new")
    public Map<String, String> newHeaders(@RequestHeader HttpHeaders headers, HttpServletResponse response) {
        var newHeaders = headers.entrySet().stream().
                collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(";", e.getValue())));
        System.out.println(newHeaders);

        var filtered = headersService.filterHeaders(newHeaders);
        System.out.println(filtered);
        return filtered;

    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return new ResponseEntity<>(e.getMessage(),NOT_FOUND);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(),BAD_REQUEST);
    }

}
