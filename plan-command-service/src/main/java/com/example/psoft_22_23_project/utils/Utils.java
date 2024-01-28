package com.example.psoft_22_23_project.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class Utils {

    // Gets the id of current authenticated user
    public static String getAuthId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username.split(",")[0];
    }

    public static String transformSpaces(String input) {
        String trimmed = input.trim();
        String replaced = trimmed.replaceAll("\\s+", " ");
        return replaced.replaceAll(" ", "_");
    }

    public static String getBearerToken(WebRequest request){
        // Get the 'Authorization' header from the request
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the token part after "Bearer "
            return authHeader.substring(7);
        }

        // If the 'Authorization' header is not found or doesn't have the expected format
        return null;
    }


}
