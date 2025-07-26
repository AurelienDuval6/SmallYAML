package io.small.yaml;

import io.smallyaml.YamlObject;
import io.smallyaml.YamlParser;
import io.smallyaml.YamlPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;

class YamlParserTest {

    @Test
    void shouldMapSimpleFile() {
        try (var inputStream = this.getClass().getClassLoader().getResourceAsStream("test-simple.yml")) {
            var object = YamlParser.parse(inputStream);

            Assertions.assertAll(
                    () -> assertYamlValue(object, YamlPath.of("author"), "Tom"),
                    () -> assertYamlValue(object, YamlPath.of("database"), null),
                    () -> assertYamlValue(object, YamlPath.of("database", "driver"), "newdriver"),
                    () -> assertYamlValue(object, YamlPath.of("database", "port"), "6601"),
                    () -> assertYamlValue(object, YamlPath.of("database", "dbname"), "'newdb'"),
                    () -> assertYamlValue(object, YamlPath.of("database", "username"), "appuser"),
                    () -> assertYamlValue(object, YamlPath.of("database", "password"), "apppassword")
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldMapComplexArray() {
        try (var inputStream = this.getClass().getClassLoader().getResourceAsStream("test-complex-arrays.yml")) {
            var object = YamlParser.parse(inputStream);

            Assertions.assertAll(
                    () -> assertYamlValue(object, YamlPath.of("data"), null),
                    () -> assertYamlValue(object, YamlPath.of("data", "0"), null),
                    () -> assertYamlValue(object, YamlPath.of("data", "0", "id"), "1"),
                    () -> assertYamlValue(object, YamlPath.of("data", "0", "name"), "Franc"),
                    () -> assertYamlValue(object, YamlPath.of("data", "0", "roles"), null),
                    () -> assertYamlValue(object, YamlPath.of("data", "0", "roles", "0"), "admin"),
                    () -> assertYamlValue(object, YamlPath.of("data", "0", "roles", "1"), "hr"),
                    () -> assertYamlValue(object, YamlPath.of("data", "1", "id"), "2"),
                    () -> assertYamlValue(object, YamlPath.of("data", "1", "name"), "John"),
                    () -> assertYamlValue(object, YamlPath.of("data", "1", "roles"), null),
                    () -> assertYamlValue(object, YamlPath.of("data", "1", "roles", "0"), "admin"),
                    () -> assertYamlValue(object, YamlPath.of("data", "1", "roles", "1"), "finance")
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void assertYamlValue(YamlObject object, YamlPath path, String expectedValue) {
        Assertions.assertEquals(Optional.ofNullable(expectedValue), object.findValue(path));
    }

}
