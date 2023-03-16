package com.psj.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration // configuration 빈으로 등록
public class JpaConfig {


    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("psj"); // auditing 이 이루어 질 때 이름에 psj가 들어간다
        // TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정
    }
}
