package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// only logged in users should have access to these actions
@RestController
@RequestMapping("cart")
@CrossOrigin
@PreAuthorize("permitAll()")
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    // create an Autowired controller to inject the Daos
    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // each method in this controller requires a Principal object as a parameter
    @GetMapping()
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // Extras
    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{id}")
    public ShoppingCart addItemToCart(@PathVariable int id, Principal principal){
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        Product product = productDao.getById(id);
        int productId = product.getProductId();
        return shoppingCartDao.addItemToCart(userId, productId);
    }

    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("/products/{id}")
    public ShoppingCart updateItemInCart(@PathVariable int itemId, Principal principal, @RequestBody ShoppingCartItem shoppingCartItem){
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        shoppingCartDao.updateCart(userId,itemId, shoppingCartItem.getQuantity());
        return shoppingCartDao.getByUserId(userId);
    }


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping()
    public ShoppingCart clearCart(Principal principal){
        // get the currently logged in username
        String userName = principal.getName();
        // find database user by userId
        User user = userDao.getByUserName(userName);
        int userId = user.getId();
        shoppingCartDao.clearCart(userId);
        // instantiate a new shopping cart to reload page. Class does not need any param to make a new class.
        return new ShoppingCart();
    }

}
