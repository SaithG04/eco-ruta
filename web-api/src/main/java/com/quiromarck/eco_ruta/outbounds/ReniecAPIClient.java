package com.quiromarck.eco_ruta.outbounds;

import com.quiromarck.eco_ruta.config.ReniecFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reniec-api", url = "${reniec.api.url}", configuration = ReniecFeignConfig.class)
public interface ReniecAPIClient {
    @GetMapping("/v2/reniec/dni")
    Object findByDNI(@RequestParam String numero);
}
