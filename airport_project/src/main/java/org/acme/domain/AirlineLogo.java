package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "airline_logo")
public class AirlineLogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Lob
    @Column(name = "content", columnDefinition = "LONGBLOB")
    private byte[] content;

    @Column(name = "content_type")
    private String contentType;

    @OneToOne(mappedBy = "logo")
    private Airline airline;

    public AirlineLogo() {}

    public AirlineLogo(String fileName, String filePath, String contentType, byte[] content, Airline airline) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.content = content;
        this.airline = airline;
    }

    public Integer getId() { return id; }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public Airline getAirline() {
        return airline;
    }

    public void setAirline(Airline airline) {
        this.airline = airline;
    }
}
