package com.microservices.demo.elastic.index.client.service.impl;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticSearchIndexRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@ConditionalOnProperty(name="elastic-config.elastic-repository-enabled", havingValue = "true", matchIfMissing = true)
@Service
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);
    private final TwitterElasticSearchIndexRepository twitterElasticSearchIndexRepository;

    public TwitterElasticRepositoryIndexClient(TwitterElasticSearchIndexRepository twitterElasticSearchIndexRepository) {
        this.twitterElasticSearchIndexRepository = twitterElasticSearchIndexRepository;
    }


    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> reposetoryResponse = (List<TwitterIndexModel>) twitterElasticSearchIndexRepository.saveAll(documents);
        List<String> docIds = reposetoryResponse.stream().map(TwitterIndexModel ::getId).collect(Collectors.toList());
        LOG.info("Documents indexed successfully with type{}, Ids {}",TwitterIndexModel.class.getName(),docIds);

        return docIds;
    }
}
