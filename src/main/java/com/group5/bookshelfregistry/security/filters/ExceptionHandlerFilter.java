package com.group5.bookshelfregistry.security.filters;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.group5.bookshelfregistry.entities.ErrorResponse;
import com.group5.bookshelfregistry.exceptions.EntityNotFoundException;
import com.group5.bookshelfregistry.exceptions.InvalidCredentialsException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {
        response.setContentType("application/json");
        ProblemDetail problemDetail=null;
        try {
            filterChain.doFilter(request, response);
        } catch (EntityNotFoundException e) {
            problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
            problemDetail.setTitle("Username doesn't exist");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(new JSONObject( problemDetail).toString());
            response.getWriter().flush();
        } catch (JWTVerificationException e) {
            problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
            problemDetail.setTitle("JWT NOT VALID");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(new JSONObject( problemDetail).toString());
            response.getWriter().flush();
        } catch (InvalidCredentialsException e) {
            problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
            problemDetail.setTitle("Unauthorized");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(new JSONObject( problemDetail).toString());
            response.getWriter().flush();
        }  catch (RuntimeException e) {
            problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
            problemDetail.setTitle("BAD REQUEST");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(new JSONObject( problemDetail).toString());
            response.getWriter().flush();
        } catch (ServletException e) {
            problemDetail= ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
            problemDetail.setTitle("Service not available");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write(new JSONObject( problemDetail).toString());
            response.getWriter().flush();
        }

    }
}
