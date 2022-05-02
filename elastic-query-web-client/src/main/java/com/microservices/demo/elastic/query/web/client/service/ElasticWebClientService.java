package com.microservices.demo.elastic.query.web.client.service;

import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.microservices.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;

import java.util.List;

public interface ElasticWebClientService {
     List<ElasticQueryWebClientResponseModel> getResponse(ElasticQueryWebClientRequestModel requestModel);
}
