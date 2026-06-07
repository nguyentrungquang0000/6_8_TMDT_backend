package com.quangnt.common.tenant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TenantContextHolder {
    private static final ThreadLocal<Integer> TENANT_CONTEXT = new ThreadLocal<>();

    public static void setTenantId(Integer tenantId) {
        TENANT_CONTEXT.set(tenantId);
    }

    public static Integer getTenantId() {
        return TENANT_CONTEXT.get();
    }

    public static void clear() {
        TENANT_CONTEXT.remove();
    }
}
