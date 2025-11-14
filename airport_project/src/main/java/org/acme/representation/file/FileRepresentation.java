package org.acme.representation.file;

import jakarta.validation.constraints.NotEmpty;

public class FileRepresentation {

    @NotEmpty
    private String filename;
    @NotEmpty
    private String filetype;
    private byte[] content;

    public FileRepresentation() {
    }

    public FileRepresentation(String filename, String filetype, byte[] content) {
        this.filename = filename;
        this.filetype = filetype;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
