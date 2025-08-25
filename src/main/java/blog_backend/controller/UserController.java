package blog_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog_backend.apiError.CustomException.EmailAlreadyUsed;
import blog_backend.apiError.CustomException.UserNotFoundException;
import blog_backend.model.User;
import blog_backend.repository.UserRepository;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="user")
@RequiredArgsConstructor
public class UserController {
    @GetMapping
    private String h(){
        return "bonjour le monde ";
    }
    private final UserRepository userRepository;
    @PostMapping("/add")
    public User  add(@RequestBody User userBody){
        Optional<User> user = this.userRepository.findByEmail(userBody.getEmail());
        if(user.isPresent()){
            throw new EmailAlreadyUsed("email :"+userBody.getEmail() + "is already used");
        }
        
        return this.userRepository.save(userBody);
    }
    @GetMapping("/getAll")
    public List<User>  getAll(){
        return this.userRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object>  getAll(@PathVariable(name="id") long id){
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            throw new UserNotFoundException("echet because not user found with this id "+id);
        }
        
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object>  delete(@PathVariable(name="id") long id){
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            this.userRepository.deleteById(id);
            return ResponseEntity.ok("");
        } else {
            throw new UserNotFoundException("echet because not user found with this id "+id);
        }
        
    }
    @DeleteMapping("/update/{id}")
    public ResponseEntity<Object>  delete(@RequestBody User userBody , @PathVariable(name="id") long id){
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            userBody.setId(id);
            this.userRepository.save(userBody);
            return ResponseEntity.ok("");
        } else {
            throw new UserNotFoundException("echet because not user found with this id "+id);
        }
        
    }
    @DeleteMapping(path = "deleteAll")
    public ResponseEntity<?> deleteAll(){
        try {
            this.userRepository.deleteAll();
            return ResponseEntity.ok("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.METHOD_FAILURE);
    }
}
