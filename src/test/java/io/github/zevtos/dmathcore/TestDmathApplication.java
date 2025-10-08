package io.github.zevtos.dmathcore;

import org.springframework.boot.SpringApplication;

public class TestDmathApplication {

    public static void main(String[] args) {
        SpringApplication.from(DmathApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
