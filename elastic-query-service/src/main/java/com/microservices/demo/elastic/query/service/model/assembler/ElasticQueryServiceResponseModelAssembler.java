package com.microservices.demo.elastic.query.service.model.assembler;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.api.ElasticDocumentController;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponceModel;
import com.microservices.demo.elastic.query.service.transform.TwitterIndexModelToResponseModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ElasticQueryServiceResponseModelAssembler extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponceModel> {
    private final TwitterIndexModelToResponseModel transformer;

    public ElasticQueryServiceResponseModelAssembler(TwitterIndexModelToResponseModel transformer) {

        super(ElasticDocumentController.class,ElasticQueryServiceResponceModel.class);
        this.transformer = transformer;
    }

    @Override
    public ElasticQueryServiceResponceModel toModel(TwitterIndexModel entity) {
        ElasticQueryServiceResponceModel responceModel= transformer.getResponseModel(entity);
        responceModel.add(
                linkTo(methodOn(ElasticDocumentController.class)
                        .getDocumentById((entity.getId())))
                        .withSelfRel());
        responceModel.add(
                linkTo(ElasticDocumentController.class)
                        .withRel("documents"));
        return responceModel;
    }

    public List<ElasticQueryServiceResponceModel> toModels(List<TwitterIndexModel> twitterIndexModels){
        return twitterIndexModels.stream().map(this::toModel).collect(Collectors.toList());
    }
}
