package com.multi.blogging.multiblogging.auth.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Authority {
    GUEST("ROLE_GUEST"),MEMBER("ROLE_MEMBER"), ADMIN("ROLE_ADMIN");

    private final String key;
}