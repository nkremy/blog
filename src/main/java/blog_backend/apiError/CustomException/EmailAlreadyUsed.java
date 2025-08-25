package blog_backend.apiError.CustomException;

public class EmailAlreadyUsed  extends RuntimeException{
    public EmailAlreadyUsed(String m){
        super(m);
    }
}
