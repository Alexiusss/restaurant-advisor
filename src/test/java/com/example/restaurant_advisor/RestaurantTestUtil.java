package com.example.restaurant_advisor;

import com.example.restaurant_advisor.model.Restaurant;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class RestaurantTestUtil {
    
    public static final Restaurant RESTAURANT1 = new Restaurant(3, "restaurant1","seafood", "www.menu1.com", null);
    public static final Restaurant RESTAURANT2 = new Restaurant(4, "restaurant2","vegan", "www.menu2.com", null);
    public static final Restaurant RESTAURANT3 = new Restaurant(5, "cafe", "italian","www.menu3.com", null);
    public static final Restaurant RESTAURANT4 = new Restaurant(6, "bar", "italian", "www.menu4.com", null);
    public static final List<Restaurant> RESTAURANT_LIST = List.of(RESTAURANT1, RESTAURANT2, RESTAURANT3, RESTAURANT4);
}