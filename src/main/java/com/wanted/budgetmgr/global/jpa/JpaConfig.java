package com.wanted.budgetmgr.global.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // @CreatedDate, @LastModifiedDate 사용을 위한 Auditing 활성화
public class JpaConfig {
}
