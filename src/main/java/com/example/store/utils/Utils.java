package com.example.store.utils;//package com.example.elecstore.utils;

import com.example.store.entities.Product;
import com.example.store.entities.ProductDiscount;
import com.example.store.entities.Review;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Utils {
    public static final String  DEFAULT_PAGE_SIZE = "100";
    public static final String DEFAULT_PAGE_NUMBER = "1";

    public static double calculateAvgRate(List<Review> reviewList){
        double total = 0;
        if (reviewList.isEmpty()) return 0;

        for (Review review : reviewList){
            total += review.getVote();
        }
        return total/reviewList.size();
    }

    public static BigDecimal getTotalPrice(Product product, BigDecimal totalPrice, int quantity, ProductDiscount productDiscount) {
        if (productDiscount.getDiscount().getPercent() > 0) {
            totalPrice = totalPrice.add(product.getPrice()
                            .multiply(BigDecimal.valueOf((1 - (productDiscount.getDiscount().getPercent() / 100)) * (double) quantity)))
                    .setScale(2, RoundingMode.UP);
        } else {
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity))).setScale(2, RoundingMode.UP);
        }

        return totalPrice;
    }
}
