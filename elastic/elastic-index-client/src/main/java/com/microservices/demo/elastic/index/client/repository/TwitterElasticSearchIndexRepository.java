package com.microservices.demo.elastic.index.client.repository;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TwitterElasticSearchIndexRepository extends ElasticsearchRepository<TwitterIndexModel,String> {
}
