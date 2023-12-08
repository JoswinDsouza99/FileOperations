package com.example.demo;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@JacksonXmlRootElement(localName = "file")
public class FileModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JacksonXmlProperty(isAttribute = true)
    private String filename;
    @JacksonXmlProperty(isAttribute = true)
    private String filesize;

    @Lob
    @Column(length = 1000)
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    //@Transient // Not stored in DB, used for XML serialization
    //@JacksonXmlProperty(localName = "data1")
    //private String base64Data;
}
