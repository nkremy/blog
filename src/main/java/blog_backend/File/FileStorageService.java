package blog_backend.File;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import blog_backend.apiError.CustomException.EmailAlreadyUsed;

@Service
public class FileStorageService {
    public static final String STORAGE_DIRECTORY = "storage";

    public static String saveFile(MultipartFile fileToSave,String nomFile) throws IOException{
        //this method save a file of user within st0rage and return the absolute path of this file

        //file is null throw exception
        if(fileToSave == null){
            throw new NullPointerException("fileToSava c'ant been null");
        }

        if(!fileToSave.getContentType().startsWith("image/"))
            throw  new EmailAlreadyUsed("this file send  : < "+ fileToSave.getOriginalFilename() + " > must be a image ");

        //path for save the file of user
        String pathToFile = STORAGE_DIRECTORY + File.separator + nomFile + getExtension(fileToSave);
        var targetFile = new File(pathToFile);

        //check if th e parent folter of this file is truly STORAGE_DIRECTORY else throw execption
        if(!Objects.equals(targetFile.getParent(), "storage")){
            throw new SecurityException("UnSupported fileName");
        }
   
        //save file in STORAGE_DIRECTORY 
        Files.copy(fileToSave.getInputStream(),targetFile.toPath() ,StandardCopyOption.REPLACE_EXISTING);

        //return the path of this file
        return targetFile.getAbsolutePath();
    }

    public File getFile(String filename)throws Exception{
        if(filename == null){
            throw new NullPointerException("filename is null");
        }
        var fileToDownload = new File(STORAGE_DIRECTORY + File.separator + filename);
                if(!Objects.equals(fileToDownload.getParent(), STORAGE_DIRECTORY)){
            throw new SecurityException("UnSupported fileName");
        }
        if(!fileToDownload.exists()){
            throw new FileNotFoundException("No file named : " + filename);
        }
        return fileToDownload;
    }

    public static  String getExtension(MultipartFile file){
        //extract the extention of a file  
        int index = file.getOriginalFilename().lastIndexOf(".");
        return file.getOriginalFilename().substring(index);
    }

    public static boolean deleteFile(String pathOfFileWillDelete){
        try {
            Path path = Paths.get(pathOfFileWillDelete);
            return Files.deleteIfExists(path);
        } catch (Exception e) {
            return false;
        }
        
    }
    public static boolean deleteAllFileInFolder(){
       boolean success = true;
        File folder = new File(STORAGE_DIRECTORY);
        File[] files = folder.listFiles();
        if (files!= null) {
            for(File f:files){
                if(f.delete()){
                    success = false;
                }
            }
        }
        return success;

    }
}
