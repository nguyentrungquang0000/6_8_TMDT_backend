package com.quangnt.common.provider;

public class TenantContext {
    private static final String DEFAULT_TENANT = "DEFAULT";

    private static final ThreadLocal<String> TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return TENANT.get() != null
                ? TENANT.get()
                : DEFAULT_TENANT;
    }

    public static void clear() {
        TENANT.remove();
    }
}
