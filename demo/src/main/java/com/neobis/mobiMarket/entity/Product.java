package com.neobis.mobiMarket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private long id;
    private String name;
    private String shortDescription;
    private String detailedDescription;
    private double price;
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image")
    private List<String> images = new ArrayList<>();
    @ManyToOne
 //   @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(
            name = "user_liked_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likedBy;
    @ManyToMany
    @JoinTable(
            name = "user_favorite_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> favoriteBy;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", detailedDescription='" + detailedDescription + '\'' +
                ", price=" + price +
                ", images=" + images +
                ", user=" + user +
                ", likedBy=" + likedBy +
                ", favoriteBy=" + favoriteBy +
                '}';
    }
}