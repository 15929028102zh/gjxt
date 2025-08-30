package com.gjxt.inspection;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 石油工业园区设备巡检系统启动类
 *
 * @author gjxt
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.gjxt.inspection.mapper")
@EnableScheduling
public class InspectionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InspectionSystemApplication.class, args);
        System.out.println("\n" +
                "   _____ _____  __   ________   ____                            __  _               \n" +
                "  / ____|_   _| \\ \\ / /__   __| |___ \\                          / / (_)              \n" +
                " | |  __  | |    \\ V /   | |      __) |                        / /   _  _ __   __ _  \n" +
                " | | |_ | | |     > <    | |     |__ <                        / /   | || '_ \\ / _` | \n" +
                " | |__| |_| |_   / . \\   | |     ___) |                      / /    | || | | | (_| | \n" +
                "  \\_____|_____|_/_/ \\_\\  |_|    |____/                      /_/     |_||_| |_|\\__, | \n" +
                "                                                                               __/ | \n" +
                "                                                                              |___/  \n" +
                "\n石油工业园区设备巡检系统启动成功！\n" +
                "访问地址：http://localhost:8080/api\n" +
                "接口文档：http://localhost:8080/api/swagger-ui/index.html\n" +
                "Druid监控：http://localhost:8080/api/druid\n");
    }

}