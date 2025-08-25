package blog_backend.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "le nom est obligatoire")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "le mot de passe est obligatoire")
    @Size(min = 8,max = 12 ,message = "le mot de passe est compris entre 8 et 12 caractere")
    @Column(nullable = false)
    private String pass;

    private String photo_prpfile;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;
    
}
