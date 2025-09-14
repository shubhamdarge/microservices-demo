package com.example.user.api;

import com.example.user.domain.User;
import com.example.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repo;

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User u){
        u.setId(null);
        u.setActive(true);
        User saved = repo.save(u);
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-email")
    public ResponseEntity<User> byEmail(@RequestParam String email){
        return repo.findByEmail(email).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}