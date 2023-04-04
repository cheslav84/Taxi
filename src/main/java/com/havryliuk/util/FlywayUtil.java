//package com.havryliuk.util;
//
//import org.flywaydb.core.Flyway;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.Optional;
//
//public class FlywayUtil {
//    private static Flyway flyway;
//
//    @Value( "${spring.datasource.url}" )
//    private static String dataSource;
//    @Value( "${spring.datasource.username}" )
//    private static String user;
//    @Value( "${spring.datasource.password}" )
//    private static String password;
//    @Value( "${spring.flyway.locations}" )
//    private static String location;
//
//
//
//    public static Flyway getFlyway() {
//        flyway = Optional.ofNullable(flyway)
//                .orElseGet(() ->
//                        Flyway.configure()
//                                .dataSource("jdbc:postgresql://localhost:5432/taxi", user, password)
//                                .baselineOnMigrate(true)
//                                .schemas("public")
//                                .locations("db/migration")
//                                .load()
//                );
//
//        return flyway;
//    }
//}
