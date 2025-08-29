package blog_backend.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
 import java.awt.image.BufferedImage;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import blog_backend.File.FileStorageService;
import blog_backend.apiError.CustomException.EmailAlreadyUsed;
import blog_backend.apiError.CustomException.UserNotFoundException;
import blog_backend.model.User;
import blog_backend.model.Users2;
import blog_backend.repository.UserRepository;
import blog_backend.tools.ParserUser;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;
    @PostMapping(path="/add")
    public ResponseEntity<?>  add(@RequestPart("user") String user ,
                        @RequestPart(name = "file",required = false) MultipartFile file) throws IOException{

        Files.createDirectories(Paths.get("storage"));    
        System.err.println("le repertoire de stockage est creer ");
        
        try {
            ParserUser.parser(user);   
            System.err.println("l'utilisateur est convertie "); 
        } catch (Exception e) {
            System.out.println("echec du parsage de luser ");
            throw new EmailAlreadyUsed("failt tou parse");
        }

            User userBody =   ParserUser.parser(user);       
                            
        Optional<User> unUser = this.userRepository.findByEmail(userBody.getEmail());
        if(unUser.isPresent()){
            throw new EmailAlreadyUsed("email :"+userBody.getEmail() + "is already used");
        }
        System.err.println("cette email is not use ");

        
        userBody = this.userRepository.save(userBody);
        System.err.println("utilisateur mis dans la bd");

        String nomFile = "image"+userBody.getId();
        System.err.println("le repertoire de stockage est creer ");
        
        if(!file.isEmpty() && (file.getSize() != 0)){
            System.err.println("le fichier dans la requete est pas null ");
            try {
                String pathOfFile =  FileStorageService.saveFile(file, nomFile);
                userBody.photo_propfile = pathOfFile;  
                        // update user with the path file in db
                userBody = this.userRepository.save(userBody);  
            } catch (Exception e) {
                System.out.println("echet de la sauvegarde du fichier");
            }

        }else{

            System.err.println("le fichier dans la requete est  null ");
        }

        



        return ResponseEntity.ok(userBody);
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
        if (user.isPresent()  ) {
            User unUser = user.get();
            if(unUser.getPhoto_propfile() != null){
                if(FileStorageService.deleteFile(unUser.getPhoto_propfile())){
                    System.err.println("file delete successfuly");
                }
            }
            
            this.userRepository.deleteById(id);
            return ResponseEntity.ok("user delete success fully");
        } else {
            throw new UserNotFoundException("echet because not user found with this id "+id);
        }
        
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Object>  delete(@RequestPart("user") String user1 ,
                        @RequestPart("file") MultipartFile file, @PathVariable(name="id") long id) throws JsonMappingException, JsonProcessingException{
        //parse user 
        User userBody =  ParserUser.parser(user1);

        //chech if user with this id is in database
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            Optional<User> userbyEmail = this.userRepository.findByEmail(userBody.getEmail());
            if(userbyEmail.isPresent()){
                throw new EmailAlreadyUsed("this " + userBody.getEmail() + "iest already used");
            }
            userBody.setId(id);
            if(!file.isEmpty() && (file.getSize() != 0)){
                try {
                    userBody.photo_propfile = FileStorageService.saveFile(file, "image"+id);         
                } catch (Exception e) {
                    System.err.println("error message : " + e.getMessage());
                    return new ResponseEntity<>("falaid",HttpStatus.METHOD_FAILURE);
                }
            }else{
                userBody.photo_propfile = user.get().getPhoto_propfile();
            }
            
            this.userRepository.save(userBody);
            return ResponseEntity.ok(userBody);
        } else {
            throw new UserNotFoundException("echet because user not found with this id "+id);
        }
        
    }
    @DeleteMapping(path = "deleteAll")
    public ResponseEntity<?> deleteAll(){
        try {
            FileStorageService.deleteAllFileInFolder();
            this.userRepository.deleteAll();
            return ResponseEntity.ok("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.METHOD_FAILURE);
    }
}
