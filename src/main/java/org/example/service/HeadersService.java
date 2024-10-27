package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class HeadersService {
    public Map<String,String> filterHeaders(Map<String, String> headersMap) {
        if (headersMap == null || headersMap.size() < 3) {
            throw new NoSuchElementException("Not Found");
        }
        if (headersMap.keySet().stream().anyMatch(k -> k.toLowerCase(Locale.ROOT).startsWith("х"))) {
            throw new IllegalArgumentException("Bad Request");
        }
        return headersMap.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase(Locale.ROOT).startsWith("x"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
