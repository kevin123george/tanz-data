package com.tanzdata.tanzdata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "reddit_comments")
public class RedditComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String parentId;

    @Column(columnDefinition = "TEXT", unique = true)
    private String commentId;

    @Column(columnDefinition = "TEXT")
    private String parent;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Integer unix;

    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String subReddit;
}
