package com.skinzen.notifications.integration.inbound;

import com.skinzen.notifications.util.SftpProperties;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.file.filters.AcceptOnceFileListFilter;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.metadata.SimpleMetadataStore;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.messaging.Message;

import java.io.File;

@Configuration
@ConditionalOnProperty(name = "integration.flow.source", havingValue = "sftp")
public class SftpInboundIntegrationFlow {

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer(SessionFactory<SftpClient.DirEntry> sessionFactory,
                                                                    SftpProperties sftpProps) {
        SftpInboundFileSynchronizer synchronizer = new SftpInboundFileSynchronizer(sessionFactory);
        synchronizer.setDeleteRemoteFiles(true);
        synchronizer.setRemoteDirectory(sftpProps.getRemoteDirectory());
        synchronizer.setFilter(new SftpSimplePatternFileListFilter("*.CSV")); // only CSV
        return synchronizer;
    }

    @Bean
    public MessageSource<File> sftpMessageSource(SftpInboundFileSynchronizer synchronizer,
                                                 SftpProperties sftpProps,
                                                 BeanFactory beanFactory) {
        SftpInboundFileSynchronizingMessageSource source = new SftpInboundFileSynchronizingMessageSource(synchronizer);
        source.setLocalDirectory(new File(sftpProps.getLocalDirectory()));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new AcceptOnceFileListFilter<>());
        source.setBeanFactory(beanFactory);
        return source;
    }

    @Bean
    public IntegrationFlow sftpInboundFlow(MessageSource<File> sftpMessageSource) {
        return IntegrationFlow
                .from(sftpMessageSource, c -> c.poller(p -> p.fixedDelay(5000))) // every 5s
                .handle(message -> {
                    File file = (File) message.getPayload();
                    System.out.println("📥 File received from SFTP: " + file.getAbsolutePath());
                    // You can add parsing logic here if needed
                })
                .get();
    }
}
