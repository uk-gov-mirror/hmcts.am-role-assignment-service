package uk.gov.hmcts.reform.roleassignment.feignclients.configuration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.roleassignment.oidc.IdamRepository;
import uk.gov.hmcts.reform.roleassignment.util.Constants;
import uk.gov.hmcts.reform.roleassignment.util.SecurityUtils;

@Service
public class DatastoreFeignClientInterceptor {

    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    IdamRepository idamRepository;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            if (!requestTemplate.url().contains("health")) {
                requestTemplate.header(Constants.SERVICE_AUTHORIZATION2, "Bearer "
                    + securityUtils.getServiceAuthorizationHeader());
                requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + idamRepository.getManageUserToken());
                requestTemplate.header(HttpHeaders.CONTENT_TYPE, "application/json");
            }
        };
    }
}

