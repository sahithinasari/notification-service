package com.skinzen.notifications.connections;

import com.skinzen.notifications.util.SftpProperties;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

@Configuration
@EnableConfigurationProperties(SftpProperties.class)
public class SftpConfig {


    @Bean
    public SessionFactory<SftpClient.DirEntry> sftpSessionFactory(SftpProperties sftpProps) {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setHost(sftpProps.getHost());
        factory.setPort(sftpProps.getPort());
        factory.setUser(sftpProps.getUser());
        factory.setPassword(sftpProps.getPassword());
        factory.setAllowUnknownKeys(true);

        // ✅ Ensure explicit type
        return new CachingSessionFactory<>(factory); // `factory` is already a SessionFactory<SftpSession>
    }


}
