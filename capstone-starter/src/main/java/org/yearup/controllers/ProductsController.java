package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products") // Base URL path for all endpoints in this controller
@CrossOrigin // Allows requests from other origins
public class ProductsController {

    // Data Access Object for products
    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao) {
        this.productDao = productDao;
    }

    // GET endpoint to search products based on optional parameters
    @GetMapping("")
    @PreAuthorize("permitAll()") // Allows all users to access this endpoint
    public List<Product> search(
            @RequestParam(name = "cat", required = false) Integer categoryId,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "color", required = false) String color) {

        try {
            return productDao.search(categoryId, minPrice, maxPrice, color); // Retrieves products based on search criteria
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad."); // Handles internal server error
        }
    }

    // GET endpoint to retrieve a product by its ID
    @GetMapping("{id}")
    @PreAuthorize("permitAll()") // Allows all users to access this endpoint
    public Product getById(@PathVariable int id) {

        try {
            // Retrieves product from DAO by ID
            Product product = productDao.getById(id);
            // Throws 404 if product not found
            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            // Returns found product
            return product;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad."); // Handles internal server error
        }
    }

    // POST endpoint to add a new product
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Requires admin role to access this endpoint
    public Product addProduct(@RequestBody Product product) {

        try {
            // Creates and returns the new product
            return productDao.create(product);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad."); // Handles internal server error
        }
    }

    // PUT endpoint to update an existing product by its ID
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Requires admin role to access this endpoint
    public void updateProduct(@PathVariable int id, @RequestBody Product product) {

        try {
            // Updates the product with the given ID
            // was a bug and previously .create() changed to .update() !
            productDao.update(id, product);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad."); // Handles internal server error
        }
    }

    // DELETE endpoint to delete a product by its ID
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Requires admin role to access this endpoint
    public void deleteProduct(@PathVariable int id) {

        try {
            // Retrieves product by its ID
            Product product = productDao.getById(id);
            // Throws 404 if product not found
            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            // Deletes the product from the database
            productDao.delete(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad."); // Handles internal server error
        }
    }
}
