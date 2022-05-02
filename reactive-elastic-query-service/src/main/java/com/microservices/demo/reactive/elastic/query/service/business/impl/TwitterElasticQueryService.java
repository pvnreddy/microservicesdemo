package com.microservices.demo.reactive.elastic.query.service.business.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponceModel;
import com.microservices.demo.elastic.query.service.common.transformer.TwitterIndexModelToResponseModel;
import com.microservices.demo.reactive.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.reactive.elastic.query.service.business.ReactiveElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);
    private final ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient;
    private final TwitterIndexModelToResponseModel transformer;

    public TwitterElasticQueryService(ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient, TwitterIndexModelToResponseModel transformer) {
        this.reactiveElasticQueryClient = reactiveElasticQueryClient;
        this.transformer = transformer;
    }

    @Override
    public Flux<ElasticQueryServiceResponceModel> getDocumentsByText(String text) {
        LOG.info("Querying Elastic search for text {}",text);
        return reactiveElasticQueryClient.getIndexModelByText(text)
                .map(transformer::getResponseModel);
    }
}
