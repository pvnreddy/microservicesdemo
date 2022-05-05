package com.microservices.demo.reactive.elaastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.config.UserAuthConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {
    private static final Logger LOG = LoggerFactory.getLogger(WebClientConfig.class);
    private  final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;

    public WebClientConfig(ElasticQueryWebClientConfigData elasticQueryConfigData, UserAuthConfig userAuthConfig) {
        this.elasticQueryWebClientConfigData = elasticQueryConfigData.getWebClient();

    }

    @Bean("webClient")
    WebClient webClient() {
        try {
            return WebClient.builder()
                    .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, elasticQueryWebClientConfigData.getContentType())
                    .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTCPClient())))
                    .codecs(configurer -> configurer
                            .defaultCodecs()
                            .maxInMemorySize(elasticQueryWebClientConfigData.getMaxInMemorySize()))
                    .build();
        }
        catch (Exception e){
            LOG.info("Print config value "+ elasticQueryWebClientConfigData.getBaseUrl());
        }

        return null;
    }

    private TcpClient getTCPClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,elasticQueryWebClientConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {connection.addHandlerLast(
                        new ReadTimeoutHandler(elasticQueryWebClientConfigData.getReadTimeoutMs(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(elasticQueryWebClientConfigData.getWriteTimeoutMs(),TimeUnit.MILLISECONDS));
                });
    }


}
