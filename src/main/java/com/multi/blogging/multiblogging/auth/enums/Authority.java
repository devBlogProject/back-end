package com.multi.blogging.multiblogging.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Authority {
    MEMBER("ROLE_MEMBER"), ADMIN("ROLE_ADMIN");

    private final String key;
}