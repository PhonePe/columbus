/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic9.internal.search.page;

import com.phonepe.columbus.elastic9.search.page.ElasticPage;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.search.page.PageNumber;
import com.phonepe.columbus.search.page.PageResolver;
import com.phonepe.columbus.search.page.Pages;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WithElasticToolkit.All
class ElasticPageResolverTest {

    static PageResolver<ElasticPage, Object> pageResolver;

    @Test
    @DisplayName("page offset")
    void testPageOffset() {
        final var page = Pages.offset(10, 12);

        final var elasticPage = pageResolver.resolve(page);

        assertAll(
                "page offset",
                () -> assertNotNull(elasticPage, "must return a valid page"),
                () -> assertEquals(10, elasticPage.getSize(), "size must match"),
                () -> assertEquals(12, elasticPage.getFrom(), "from must match"));
    }

    @Test
    @DisplayName("page pointer string")
    void testPagePointer() {
        final var page = Pages.pointer(10, "pointer");

        final var elasticPage = pageResolver.resolve(page, null);

        assertAll(
                "page pointer",
                () -> assertNotNull(elasticPage, "must return a valid page"),
                () -> assertEquals(10, elasticPage.getSize(), "size must match"),
                () -> assertEquals(List.of("pointer"), elasticPage.getAfterKeys(), "key must match"));
    }

    @Test
    @DisplayName("page without pointer")
    void testPageWithoutPointer() {
        final var page = Pages.pointer(10, null);

        final var elasticPage = pageResolver.resolve(page);

        assertAll(
                "page pointer",
                () -> assertNotNull(elasticPage, "must return a valid page"),
                () -> assertEquals(10, elasticPage.getSize(), "size must match"),
                () -> assertTrue(elasticPage.getAfterKeys().isEmpty(), "no keys must be present"));
    }

    @Test
    @DisplayName("page pointer list")
    void testPagePointers() {
        final var keys = List.of("pointer", 20);
        final var page = Pages.pointer(10, keys);

        final var elasticPage = pageResolver.resolve(page);

        assertAll(
                "page pointer",
                () -> assertNotNull(elasticPage, "must return a valid page"),
                () -> assertEquals(10, elasticPage.getSize(), "size must match"),
                () -> assertEquals(keys, elasticPage.getAfterKeys(), "keys must match"));
    }

    @BeforeAll
    static void beforeAll(final ElasticToolkit toolkit) {
        pageResolver = toolkit.pageResolver();
    }

    @Nested
    class PageNumberTest {

        @Test
        @DisplayName("all valid fields")
        void testAllValid() {
            final var page = Pages.number(10, 2);

            final var elasticPage = pageResolver.resolve(page);

            assertAll(
                    "page number",
                    () -> assertNotNull(elasticPage, "must return a valid page"),
                    () -> assertEquals(10, elasticPage.getSize(), "size must match"),
                    () -> assertEquals(20, elasticPage.getFrom(), "from must match"));
        }

        @Test
        @DisplayName("first page")
        void testFirstPage() {
            final var page = Pages.number(10, 0);

            final var elasticPage = pageResolver.resolve(page);

            assertAll(
                    "page number",
                    () -> assertNotNull(elasticPage, "must return a valid page"),
                    () -> assertEquals(10, elasticPage.getSize(), "size must match"),
                    () -> assertEquals(0, elasticPage.getFrom(), "from must match"));
        }

        @Test
        @DisplayName("null page number")
        void testNullPageNumber() {
            final var page = PageNumber.builder()
                    .size(10)
                    .build();

            final var elasticPage = pageResolver.resolve(page);

            assertAll(
                    "page number",
                    () -> assertNotNull(elasticPage, "must return a valid page"),
                    () -> assertEquals(10, elasticPage.getSize(), "size must match"),
                    () -> assertNull(elasticPage.getFrom(), "from must be null"));
        }

        @Test
        @DisplayName("null size")
        void testNullSize() {
            final var page = PageNumber.builder()
                    .number(0)
                    .build();

            final var elasticPage = pageResolver.resolve(page);

            assertAll(
                    "page number",
                    () -> assertNotNull(elasticPage, "must return a valid page"),
                    () -> assertNull(elasticPage.getSize(), "size must be null"),
                    () -> assertEquals(0, elasticPage.getFrom(), "from must match"));
        }

        @Test
        @DisplayName("null size for second page and later")
        void testNullSizeNonFirstPage() {
            final var page = PageNumber.builder()
                    .number(1)
                    .build();

            assertThrows(IllegalArgumentException.class, () -> pageResolver.resolve(page), "size must be provided");
        }

        @Test
        @DisplayName("null fields")
        void testNullFields() {
            final var page = PageNumber.builder().build();

            final var elasticPage = pageResolver.resolve(page);

            assertAll(
                    "page number",
                    () -> assertNotNull(elasticPage, "must return a valid page"),
                    () -> assertNull(elasticPage.getSize(), "size must be null"),
                    () -> assertNull(elasticPage.getFrom(), "from must be null"));
        }

        @Test
        @DisplayName("negative size")
        void testNegativeSize() {
            final var page = Pages.number(-10, 2);

            assertThrows(IllegalArgumentException.class, () -> pageResolver.resolve(page), "size must be positive");
        }

        @Test
        @DisplayName("negative number")
        void testNegativeNumber() {
            final var page = Pages.number(10, -2);

            assertThrows(IllegalArgumentException.class, () -> pageResolver.resolve(page), "size must be positive");
        }
    }
}
