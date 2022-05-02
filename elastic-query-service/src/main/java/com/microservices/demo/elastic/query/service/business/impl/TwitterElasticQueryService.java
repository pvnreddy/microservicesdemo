package com.microservices.demo.elastic.query.service.business.impl;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponceModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.microservices.demo.elastic.query.service.common.transformer.TwitterIndexModelToResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    //private final TwitterIndexModelToResponseModel twitterIndexModelToResponseModel;
    private final ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler;

    public TwitterElasticQueryService(ElasticQueryClient elasticQueryClient, ElasticQueryServiceResponseModelAssembler elasticQueryServiceResponseModelAssembler) {
        this.elasticQueryClient = elasticQueryClient;
        this.elasticQueryServiceResponseModelAssembler = elasticQueryServiceResponseModelAssembler;
        //this.twitterIndexModelToResponseModel = twitterIndexModelToResponseModel;
    }

    @Override
    public ElasticQueryServiceResponceModel getDocumentById(String id) {

        LOG.info("Querying Elastic search by ID "+id);
       return elasticQueryServiceResponseModelAssembler.toModel( elasticQueryClient.getIndexModelById(id));


    }

    @Override
    public List<ElasticQueryServiceResponceModel> getDocumentsByText(String text) {
        LOG.info("Querying Elastic search by Text "+text);
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getIndexModelsByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponceModel> getAllDocuments() {
        LOG.info("Querying all Elastic search docs");
        return elasticQueryServiceResponseModelAssembler.toModels(elasticQueryClient.getAllIndexModels());
    }
}
