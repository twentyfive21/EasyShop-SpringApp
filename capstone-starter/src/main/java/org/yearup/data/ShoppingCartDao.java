package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCart addItemToCart(int userId, int itemId);
    void clearCart(int userId);
    void updateCart(int userId, int itemId, int quantity);
}
