package com.github.Finance.filters;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TrailingSlashFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
            
                String url = request.getRequestURI();

                if (url.length() > 1 && url.endsWith("/")) {

                    String newUrl = url.substring(0, url.length() - 1);
                    String queryString = request.getQueryString();
                    if (queryString != null) {
                        newUrl += "?" + queryString;
                    }
                    response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
                    response.setHeader("Location", newUrl);
                    return;
                }

                filterChain.doFilter(request, response);

    }
    


}
