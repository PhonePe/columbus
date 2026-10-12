package com.phonepe.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

@JsonClassDescription("""
        A retriever is a specification to describe top documents returned from a search. A retriever may have child retrievers.
        This allows for complex behavior to be depicted in a tree-like structure, called the retriever
        tree, which clarifies the order of operations that occur during a search.
        """)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = KnnRetriever.class),
        @JsonSubTypes.Type(value = StandardRetriever.class),
        @JsonSubTypes.Type(value = RrfRetriever.class)
})
public interface Retriever {

    @NonNull
    @JsonPropertyDescription("Type of the retriever")
    String getType();
}
