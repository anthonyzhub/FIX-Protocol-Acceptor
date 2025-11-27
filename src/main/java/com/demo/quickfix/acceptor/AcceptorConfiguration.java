package com.demo.quickfix.acceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.*;

//@Configuration
public class AcceptorConfiguration {

    @Bean
    public MessageFactory messageFactory() {
        return new DefaultMessageFactory();
    }

    @Bean
    public SessionSettings sessionSettings() throws ConfigError {
        return new SessionSettings("quickfixj-acceptor.cfg");
    }

    @Bean
    public LogFactory logFactory()  {
        return new ScreenLogFactory();
    }

    @Bean
    public MessageStoreFactory messageStoreFactory() {
        return new MemoryStoreFactory();
    }

    @Bean
    public Acceptor initiator(Application application,
                              MessageStoreFactory messageStoreFactory,
                              SessionSettings sessionSettings,
                              LogFactory logFactory,
                              MessageFactory messageFactory) throws ConfigError {
        SocketAcceptor socketAcceptor = new SocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
        socketAcceptor.start();
        return socketAcceptor;
    }
}
