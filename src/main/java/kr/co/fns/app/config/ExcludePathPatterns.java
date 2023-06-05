package kr.co.fns.app.config;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder
public class ExcludePathPatterns {

//    final public static List<String> excludePathPatterns = Arrays.asList(
//            "/swagger-ui.html",
//            "/swagger-ui/**",
//            "/v3/api-docs/**",
//            "/api-docs/**",
//            "/swagger-resources",
//            "/swagger-resources/**",
//            "/configuration/ui",
//            "/configuration/security",
//            "/webjars/**",
//            "/actuator/**",
//            "/actuator",
//            "/favicon.ico",
//
//            // user api
//            // minute
//            "/api/v1/minute/{idx}",
//            "/api/v1/minute/last",
//            "/api/v1/minute/{idx}/reply-last",
//            "/api/v1/minute/{idx}",
//            "/api/v1/minute",
//            "/api/v1/minute/specific-person-list",
//            "/api/v1/minute/write-check",
//            "/api/v1/minute/{idx}/reply",
//            "/api/v1/minute/{idx}/reply/sync",
//
//            // fanit
//            "/api/v1/fanit",
//            "/api/v1/fanit/{idx}"
//    );

    final public static Map<String, String> excludePathPatternsMap = Stream.of(
            new String[][] {
                    {"/swagger-ui.html", "NONE"},
                    {"/swagger-ui/**", "NONE"},
                    {"/v3/api-docs/**", "NONE"},
                    {"/api-docs/**", "NONE"},
                    {"/swagger-resources", "NONE"},
                    {"/swagger-resources/**", "NONE"},
                    {"/configuration/ui", "NONE"},
                    {"/configuration/security", "NONE"},
                    {"/webjars/**", "NONE"},
                    {"/actuator/**", "NONE"},
                    {"/actuator", "NONE"},
                    {"/favicon.ico", "NONE"},
                    // minute
                    {"/api/v1/minute/{idx}", "GET, PUT"},
                    {"/api/v1/minute/last", "GET"},
                    {"/api/v1/minute/{idx}/reply-last", "GET"},
                    {"/api/v1/minute", "GET"},
                    {"/api/v1/minute/specific-person-list", "GET"},
                    {"/api/v1/minute/write-check", "GET"},
                    {"/api/v1/minute/{idx}/reply", "GET"},
                    {"/api/v1/minute/{idx}/reply/sync", "PUT"},
                    // fanit
                    {"/api/v1/fanit", "PATCH"},
                    {"/api/v1/fanit/{idx:\\d+}", "GET, PATCH"},
                    {"/api/v1/fanit/vote/check", "GET"},
                    {"/api/v1/fanit/{idx:\\d+}/story", "GET"},
                    // event
                    {"/api/v1/event/{idx:\\d+}/story","GET"},
                    {"/api/v1/event/story/{idx:\\d+}/reply","GET"}

            }
    ).collect(Collectors.toMap(data -> data[0], data -> data[1]));
}
