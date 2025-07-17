package com.skinzen.notifications.util;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {
    private String host;
    private int port;
    private String user;
    private String password;
    private String remoteDirectory;
    private String localDirectory;
}

