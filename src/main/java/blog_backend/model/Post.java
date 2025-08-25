package blog_backend.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "le titre du post est obligatoire")
    @Column(nullable = false)
    private String titre;

    @NotBlank(message = "la descrition du post est aubligatoire")
    @Column(nullable = false)
    private String des;

    private String image;

    @NotBlank(message = "la date est obligaroire")
    @Column(nullable = false)
    private LocalDateTime date_post;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
