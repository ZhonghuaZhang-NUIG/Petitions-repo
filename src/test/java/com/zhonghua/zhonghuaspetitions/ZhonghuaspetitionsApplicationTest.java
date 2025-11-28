package com.zhonghua.zhonghuaspetitions;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZhonghuaspetitionsApplicationTest {

    @Test
    void contextLoads() {
        // Verifies the Spring context starts successfully
    }

    @Test
    void main_runsWithoutErrors() {
        ZhonghuaspetitionsApplication.main(new String[] { "--spring.main.web-application-type=none" });
    }
}