package com.ttulka.blog.samples.bad.product.jdbc;

import com.ttulka.blog.samples.bad.product.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductsJdbc implements Products {

    private final JdbcTemplate jdbc;

    @Override
    public Collection<Product> findAll() {
        return jdbc.queryForList("SELECT id, title, price, availability FROM products")
                .stream()
                .map(r -> new ProductJdbc(
                        new ProductId((Long)r.get("id")),
                        new Title((String)r.get("title")),
                        new Money((Double)r.get("price")),
                        Availability.valueOf((String)r.get("availability")),
                        jdbc))
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(ProductId id) {
        return jdbc.queryForList(
                "SELECT id, title, price, availability FROM products WHERE id = ?",
                id.value())
                .stream()
                .map(r -> (Product)new ProductJdbc(
                        id,
                        new Title((String)r.get("title")),
                        new Money((Double)r.get("price")),
                        Availability.valueOf((String)r.get("availability")),
                        jdbc))
                .findFirst()
                .orElseGet(ProductNull::new);
    }

    static class ProductNull implements Product {

        @Override
        public ProductId id() {
            return new ProductId(Long.MAX_VALUE);
        }

        @Override
        public Title title() {
            return new Title("not found");
        }

        @Override
        public Money price() {
            return new Money(0.);
        }

        @Override
        public Availability availability() {
            return Availability.UNKNOWN;
        }

        @Override
        public void changeTitle(Title newTitle) {
            // do nothing
        }

        @Override
        public void changePrice(Money newPrice) {
            // do nothing
        }
    }
}
