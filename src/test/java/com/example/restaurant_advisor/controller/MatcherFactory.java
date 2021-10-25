package com.example.restaurant_advisor.controller;

import com.example.restaurant_advisor.util.TestUtil;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class MatcherFactory {

    public static <T> Matcher<T> usingIgnoringFieldsComparator(Class<T> clazz, String... fieldsToIgnore) {
        return new Matcher<>(clazz,
                (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(e),
                (a, e) -> assertThat(a).usingElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(e));
    }

    public static class Matcher<T> {
        private final Class<T> clazz;
        private final BiConsumer<T, T> assertion;
        private final BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion;

        private Matcher(Class<T> clazz, BiConsumer<T, T> assertion, BiConsumer<Iterable<T>, Iterable<T>> iterableAssertion) {
            this.clazz = clazz;
            this.assertion = assertion;
            this.iterableAssertion = iterableAssertion;
        }

        public void assertMatch(T actual, T expected) {
            assertion.accept(actual, expected);
        }

        public ResultMatcher contentJson(T expected) {
            return result -> assertMatch(TestUtil.readFromJsonMvcResult(result, clazz), expected);
        }
    }
}