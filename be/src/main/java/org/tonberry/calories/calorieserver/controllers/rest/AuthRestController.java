package org.tonberry.calories.calorieserver.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tonberry.calories.calorieserver.schema.AuthenticatRequest;
import org.tonberry.calories.calorieserver.schema.AuthenticateResponse;
import org.tonberry.calories.calorieserver.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @RequestMapping(value = "/v1/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody AuthenticatRequest authenticatRequest, HttpServletResponse servletResponse) throws Exception {
        if (authService.authenticate(authenticatRequest.getUsername(), authenticatRequest.getPassword())) {
            return ResponseEntity.ok(new AuthenticateResponse("Success"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AuthenticateResponse("Failure"));
        }
    }

    @RequestMapping(value = "/v1/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        authService.deauthorize();
        return ResponseEntity.ok(new AuthenticateResponse("Success"));
    }
}
