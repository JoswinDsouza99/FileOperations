package com.example.demo;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class FileModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    private Long filesize;

    @Lob
    @Column(length = 1000)
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;
}
