package org.hoongoin.interviewbank.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.context.SpringBootTest;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IbWithMockUser
@SpringBootTest(classes = {TestRedisConfig.class, TestMockConfig.class})
public @interface IbSpringBootTest {
}
