package com.microservices.demo.elastic.query.client.service.impl;

import com.microservices.demo.common.util.CollectionsUtil;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.repository.TwitterElasticSearchQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@ConditionalOnProperty(name="elastic-config.elastic-repository-enabled", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {
    private final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryQueryClient.class);
    private final TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository;

    public TwitterElasticRepositoryQueryClient(TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository) {
        this.twitterElasticSearchQueryRepository = twitterElasticSearchQueryRepository;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {

           Optional<TwitterIndexModel> searchResult = twitterElasticSearchQueryRepository.findById(id);
           LOG.info("Document with id {} searched successfully",
                   searchResult.orElseThrow(() -> new ElasticQueryClientException("No document found with id "+id)).getId());
            return searchResult.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelsByText(String text) {

        List<TwitterIndexModel> searchResult= twitterElasticSearchQueryRepository.findByText(text);
        LOG.info("{} no of documents searched with text {}",searchResult.size(),text);
        return searchResult;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        List<TwitterIndexModel> searchResult= CollectionsUtil.getInstance().getListFromIterable( twitterElasticSearchQueryRepository.findAll());
        LOG.info("total {} no of documents identified",searchResult.size());
        return searchResult;
    }
}
