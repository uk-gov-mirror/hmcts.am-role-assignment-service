package uk.gov.hmcts.reform.roleassignment.domain.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Value
@Builder
@AllArgsConstructor
@Getter
public class QueryRequest {
    @Singular("actorId")
    private final List<String> actorId;
    @Singular("roleType")
    private final List<String> roleType;
    @Singular("roleName")
    private final List<String> roleName;
    @Singular("classification")
    private final List<String> classification;
    @Singular("grantType")
    private final List<String> grantType;

    private LocalDateTime validAt;
    @Singular("roleCategory")
    private final List<String> roleCategory;

    private Map<String, List<String>> attributes;
    @Singular("authorisations")
    private final List<String> authorisations;

}
