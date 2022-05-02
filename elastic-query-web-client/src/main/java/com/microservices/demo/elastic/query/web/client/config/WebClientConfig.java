package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.config.UserAuthConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;


@Configuration
@LoadBalancerClient(name = "elastic-query-service",configuration = ElasticQueryServiceInstanceListSupplierConfig.class)
public class WebClientConfig {
    private  final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;
    private final UserAuthConfig userAuthConfig;

    public WebClientConfig(ElasticQueryWebClientConfigData elasticQueryWebClientConfigData, UserAuthConfig userAuthConfig) {
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData.getWebClient();
        this.userAuthConfig = userAuthConfig;
    }
    @LoadBalanced
    @Bean("webClientBuilder")
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(userAuthConfig.getUserName(),userAuthConfig.getPassword()))
                .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE,elasticQueryWebClientConfigData.getContentType())
                .defaultHeader(HttpHeaders.ACCEPT,elasticQueryWebClientConfigData.getAcceptType())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTCPClient())))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(elasticQueryWebClientConfigData.getMaxInMemorySize()));
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
