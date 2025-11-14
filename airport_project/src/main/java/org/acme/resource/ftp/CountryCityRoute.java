package org.acme.resource.ftp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.config.FTPConfiguration;
import org.acme.service.CountryCityService;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class CountryCityRoute extends RouteBuilder {

    @Inject
    CountryCityService countryCityService;
    @Inject
    FTPConfiguration ftpConfiguration;

    @Override
    public void configure() throws Exception {

        from(ftpConfiguration.getFtpUrl()+"?username="+ftpConfiguration.getFtpUsername()+"&"+
                "password="+ftpConfiguration.getFtpPassword()+"&delay="+ftpConfiguration.getPollDelay()+"s&noop=true")
                .routeId("country-city-import-route")
                .log("Processing file: ${header.CamelFileName}")
                .unmarshal().csv()
                .split(body())
                .bean(CountryCityService.class, "processCsvRow")
                .end()
                .log("Finished processing ${header.CamelFileName}");
    }
}
