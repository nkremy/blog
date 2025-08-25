package blog_backend.apiError.CustomException;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message){
        super(message);
    }
}
