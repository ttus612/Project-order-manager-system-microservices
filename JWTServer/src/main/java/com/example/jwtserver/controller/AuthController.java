    package com.example.jwtserver.controller;

    import com.example.jwtserver.dto.AuthRequest;
    import com.example.jwtserver.entity.UserCredential;
    import com.example.jwtserver.service.AuthService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/auth")
    public class AuthController {
        @Autowired
        private AuthService authService;

        @Autowired
        private AuthenticationManager authenticationManager;

        @PostMapping("/register")
        public String addNewUser(@RequestBody UserCredential userCredential) {
            return authService.saveUser(userCredential);
        }

        @PostMapping ("/token")
        public String getToken(@RequestBody AuthRequest authRequest) {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                return authService.generateToken(authRequest.getUsername());
            }else {
                throw new RuntimeException("Invalid username or password");
            }
        }

        @GetMapping ("/validate")
        public String validateToken(@RequestParam("token") String token) {
            authService.validateToken(token);
            return "Token is valid";
        }
    }
