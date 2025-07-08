package com.shoponline.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@Configuration
@EnableSpringDataWebSupport(
        pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO
)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class WebConfig {
}