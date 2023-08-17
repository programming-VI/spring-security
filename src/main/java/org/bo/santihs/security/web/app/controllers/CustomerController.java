package org.bo.santihs.security.web.app.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class CustomerController {
    private SessionRegistry sessionRegistry;

    @GetMapping("")
    public String index() {
        return "Hello world";
    }

    @GetMapping("/index2")
    public String index2() {
        return "Hello world not secure";
    }

    @GetMapping("/session")
    public ResponseEntity<?> getDetailSession() {
        String sessionId = "";
        User user = null;
        List<Object> sessions = sessionRegistry.getAllPrincipals();
        for (Object session : sessions) {
            if (session instanceof User) {
                user = (User) session;
            }
            List<SessionInformation> sessionInformation =
                    sessionRegistry.getAllSessions(user, false);
            for (SessionInformation info : sessionInformation) {
                sessionId = info.getSessionId();
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("hello", "world");
        response.put("sessionId", sessionId);
        response.put("sessionUser", user);
        return ResponseEntity.ok(response);
    }
}
