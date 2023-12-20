package com.lab.model.config.util;

import com.lab.model.model.RoleEntity;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public enum Role {
    AUTH,
    APPROVE_DAYS,
    APPROVE_DAYS_OFF_REQUEST,
    MANAGE_ACCOUNTS,
    SET_VACANCY_DAYS_NUMBER,
    INACTIVE;
}
