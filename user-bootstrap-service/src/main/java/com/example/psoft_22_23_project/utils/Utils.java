package com.example.psoft_22_23_project.utils;

import com.nimbusds.jwt.JWT;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;

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


    public String getBearerToken(HttpServletRequest request){
        // Get the 'Authorization' header from the request
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extract the token part after "Bearer "
            return authHeader.substring(7);
        }

        // If the 'Authorization' header is not found or doesn't have the expected format
        return null;



        /*
        String token = request.getHeader("Authorization");
        String newToken = token.replace("Bearer", "");
        Jwt dToken;
        dToken.
        String s = (String) dToken.getClaims().get("sub");
        String id = String.valueOf(s.split(",")[0]);


        return id;*/
    }


}
