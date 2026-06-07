package com.quangnt.common.tenant;

import org.springframework.stereotype.Component;

@Component
public class TenantProvider {
    public Integer getTenantId() {
        return TenantContextHolder.getTenantId();
    }
}
