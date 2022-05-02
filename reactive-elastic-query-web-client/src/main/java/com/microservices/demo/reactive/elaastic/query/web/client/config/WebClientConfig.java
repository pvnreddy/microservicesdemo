package com.microservices.demo.reactive.elaastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.config.UserAuthConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {
    private  final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;

    public WebClientConfig(ElasticQueryWebClientConfigData elasticQueryWebClientConfigData, UserAuthConfig userAuthConfig) {
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData.getWebClient();

    }

    @Bean("webClient")
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE,elasticQueryWebClientConfigData.getContentType())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTCPClient()))).build();
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
