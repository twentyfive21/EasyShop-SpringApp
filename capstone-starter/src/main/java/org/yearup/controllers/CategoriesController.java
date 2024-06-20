package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

//✅ add the annotations to make this a REST controller
//✅ add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
//✅ add annotation to allow cross site origin requests
@RestController
@RequestMapping("categories")
@CrossOrigin
public class CategoriesController
{
    private CategoryDao categoryDao;
    private ProductDao productDao;

    // ✅ create an Autowired controller to inject the categoryDao and ProductDao
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    //✅ add the appropriate annotation for a get action
    @GetMapping()
    public List<Category> getAll()
    {
        // find and return all categories
        return categoryDao.getAllCategories();
    }

    // ✅ add the appropriate annotation for a get action
    @GetMapping("{id}") // Defines this method to handle GET requests at /{id}
    @PreAuthorize("permitAll()") // Allows all users (authenticated or not) to access this method
    public ResponseEntity<Category> getById(@PathVariable int id) {
        // Retrieve category from database by its ID
        Category category = categoryDao.getById(id);

        // Check if category exists and has a valid name
        if (category != null && category.getName() != null) {
            // If category found and has a valid name, return it with HTTP status 200 OK
            return ResponseEntity.ok(category);
        } else {
            // If category not found or name is null, return HTTP status 404 NOT FOUND
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping("{categoryId}/products")
    public List<Product> getProductsById(@PathVariable int categoryId)
    {
        // get a list of product by categoryId
        return productDao.listByCategoryId(categoryId);
    }

    // ✅add annotation to call this method for a POST action
    // ✅add annotation to ensure that only an ADMIN can call this function
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category) {
        return categoryDao.create(category);
    }

    // ✅add annotation to call this method for a PUT (update) action - the url path must include the categoryId
    // ✅add annotation to ensure that only an ADMIN can call this function
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category)
    {
        // update the category by id
        categoryDao.update(id,category);
    }


    // ✅add annotation to call this method for a DELETE action - the url path must include the categoryId
    // ✅add annotation to ensure that only an ADMIN can call this function
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int id)
    {
        // delete the category by id
       try{
           categoryDao.delete(id);
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
