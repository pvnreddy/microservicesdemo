package com.microservices.demo.elastic.query.web.client.service.impl;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.elastic.query.web.client.common.exception.ElasticQueryWebClientException;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import com.microservices.demo.elastic.query.web.client.service.ElasticWebClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TwitterElasticWebClientService implements ElasticWebClientService {
private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticWebClientService.class);
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;
    private final WebClient.Builder webclientBuilder;

    public TwitterElasticWebClientService(ElasticQueryWebClientConfigData elasticQueryWebClientConfigData, @Qualifier("webClientBuilder") WebClient.Builder webclient) {
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData;
        this.webclientBuilder = webclient;
    }


    @Override
    public List<ElasticQueryWebClientResponseModel> getResponse(ElasticQueryWebClientRequestModel requestModel) {
        LOG.info("Querying with web client for text "+ requestModel.getText());
        return getWebClient(requestModel)
                .bodyToFlux(ElasticQueryWebClientResponseModel.class)
                .collectList().block();
    }

    private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequestModel requestModel){
        return webclientBuilder.build()
                .method(HttpMethod.valueOf(elasticQueryWebClientConfigData.getQueryByText().getMethod()))
                .uri(elasticQueryWebClientConfigData.getQueryByText().getUri())
                .accept(MediaType.valueOf( elasticQueryWebClientConfigData.getQueryByText().getAccept()))
                .body(BodyInserters.fromPublisher(Mono.just(requestModel),createParameterizedTypeReference() ))
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not Authenticated !")))
                .onStatus(HttpStatus::is4xxClientError,
                        cr -> Mono.just(new ElasticQueryWebClientException(cr.statusCode().getReasonPhrase())))
                .onStatus(
                        HttpStatus :: is5xxServerError,
                        cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase()))
                );
    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {

        return new ParameterizedTypeReference<T>() {
        };
    }
}
