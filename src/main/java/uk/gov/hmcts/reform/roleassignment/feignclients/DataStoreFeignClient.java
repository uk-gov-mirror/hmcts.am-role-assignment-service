package uk.gov.hmcts.reform.roleassignment.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.hmcts.reform.roleassignment.domain.model.Case;
import uk.gov.hmcts.reform.roleassignment.feignclients.configuration.DatastoreFeignClientConfiguration;
import uk.gov.hmcts.reform.roleassignment.feignclients.configuration.DatastoreFeignClientFallback;

@FeignClient(value = "datastoreclient", url = "${feign.client.config.datastoreclient.url}",
             configuration = DatastoreFeignClientConfiguration.class,
             fallback = DatastoreFeignClientFallback.class)

public interface DataStoreFeignClient {

    @GetMapping(value = "/")
    String getServiceStatus();

    @GetMapping(value = "/caseworkers/{uid}/jurisdictions/{jid}/case-types/{ctid}/cases/{cid}")
    String getCaseDataV1(@PathVariable("uid") String uid, @PathVariable("jid") String jurisdictionId,
                         @PathVariable("ctid") String caseTypeId, @PathVariable("cid") String caseId);

    @GetMapping(value = "/cases/{caseId}", headers = "experimental=true")
    Case getCaseDataV2(@PathVariable("caseId") String caseId);

}
