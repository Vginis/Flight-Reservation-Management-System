package org.acme.config;

import jakarta.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class FTPConfiguration {
    @ConfigProperty(name = "ftp.url")
    private String ftpUrl;
    @ConfigProperty(name = "ftp.username")
    private String ftpUsername;
    @ConfigProperty(name = "ftp.password")
    private String ftpPassword;
    @ConfigProperty(name = "ftp.pollDelay")
    private Integer pollDelay;

    public String getFtpUrl() {
        return ftpUrl;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public Integer getPollDelay() {
        return pollDelay;
    }
}
