package blog_backend.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import blog_backend.model.User;

public class ParserUser {
    
    public static User parser(String userString) throws JsonMappingException, JsonProcessingException{
        ObjectMapper perser = new ObjectMapper();
        return perser.readValue(userString,User.class);
    }
}
