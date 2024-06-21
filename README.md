
<p align="center">
  <img src="https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/026ed6e9-f968-449f-bc92-99d50c588421" height="250">
</p>

# EasyShop E-Commerce Application

## Welcome to EasyShop, my enhanced E-Commerce Application!  
I have made significant improvements to the platform's backend, leveraging `Spring Boot` and `MySQL`.

- The updates include critical bug fixes, new features, and a more intuitive user interface that enhances both usability and performance.
- Additionally, all product pictures have been replaced with better, more detailed images to create a more engaging shopping experience.
- As the backend developer for EasyShop, I focused on:
  - Bug resolution
  - Feature implementation
  - Ensuring a well-tested API integrated seamlessly with the MySQL database
    
## HomeScreen and folder structure
![Screenshot 2024-06-21 at 11 34 51 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/14e8c3b8-3f09-4a51-b1db-b54b09405071)

## Interesting Piece of Code
This section of code highlights a pivotal part of my development process for EasyShop. Initially, integrating this code posed a challenge as it was not originally authored by me, necessitating a thorough understanding of its functionality. The ResultSet retrieved from the database contains crucial product details such as quantity, ID, name, price, category, description, color, stock availability, feature status, and image URL.

To process this data effectively:

Product Creation: Each iteration through the ResultSet constructs a Product object encapsulating all retrieved attributes.
Shopping Cart Management: Utilizing a ShoppingCartItem, the code organizes the product within a shopping cart, ensuring accurate quantity allocation.
This approach not only enhances the application's scalability and performance but also underscores my proficiency in integrating and optimizing backend functionalities.

I connected the dots where normally I would have simply created a Product and added it to an array list. With this code, I realized the need to first create the Product, then transform it into a ShoppingCartItem, and finally add it to the shopping cart. This process was crucial for accurately managing product details retrieved from the database and ensuring seamless integration into the EasyShop E-Commerce Application.

![Screenshot 2024-06-21 at 12 09 54 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/50597449-a022-4879-bdb2-9e108c055f53)


## UI Improvements  

### Before : 
The UI of the EasyShop E-Commerce Application was simple and functional but not visually appealing. The neon green button was not aesthetically pleasing, and the product images lacked detail, which gave the user a poor shopping experience.

![Screenshot 2024-06-21 at 11 08 39 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/ee18f877-0df0-4ed1-a666-e76d3bd25e7b)


### After : 
I addressed these issues by implementing changes to enhance user interface (UI) experience. This involved replacing all images with higher-quality pictures, adjusting button colors from neon green to a more visually appealing option, and increasing image sizes. These adjustments were made to ensure a more engaging and user-friendly UI for EasyShop's E-Commerce Application.

![Screenshot 2024-06-21 at 11 09 02 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/1c84d33f-ba13-471a-ac10-2fb87913ebee)

---

# Features Implemented
### Categories Management : Implemented complete CRUD operations for categories : 
- GET <code>/categories</code> : Retrieve all categories.
- GET <code>/categories/{id}</code> : Retrieve a category by ID.
- POST <code>/categories</code> : Create a new category.
- PUT <code>/categories/{id}</code> : Update category.
- DELETE <code>/categories/{id}</code> : Delete a category.
  
![Screenshot 2024-06-21 at 11 36 25 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/66b46929-d478-4807-910d-59d34bab59f9)

### Products Management : Enhanced product management to ensure only admins can modify products:

- GET <code>/products</code> : Retrieve all products.
- GET <code>/products/{id}</code> : Retrieve a product by ID.
- POST <code>/products</code> : Add a new product.
- PUT <code>/products/{id}</code> : Update a product.
- DELETE <code>/products/{id}</code> : Remove a product.
  
![Screenshot 2024-06-21 at 11 44 56 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/b3147a54-8fc9-456d-84f3-8c9022d88631)


### Bug Fixes
1. Search and Filter Functionality 
- Refined the logic to accurately retrieve products based on search criteria including category, price range, and color.
  
![Screenshot 2024-06-21 at 11 53 57 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/8cf46b1e-8b18-4cd4-8986-1eeef0dffc37)
![Screenshot 2024-06-21 at 11 54 37 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/e8680c88-e0c5-47dc-ba4f-ca36ce465539)
![Screenshot 2024-06-21 at 11 55 33 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/6a2ba03e-ea85-49b6-a01c-e52c801e0f8f)
![Screenshot 2024-06-21 at 11 56 02 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/e44ff6cc-9de3-4dd4-a662-1be21fd24f89)

2. Product Duplication
Eliminated product duplication issues:<br>
Code initally was <code>productDao.create();</code>

![Screenshot 2024-06-21 at 11 58 37 AM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/3b1378e4-6657-4875-8a35-53706bde32d0)

# New Features
### Shopping Cart
- GET <code>/cart</code> : Retrieve the current user's shopping cart.
- Cart Display
  
![Screenshot 2024-06-21 at 12 02 05 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/29b8f6a9-0f29-4520-8a7e-bea23596afb6)
- Shopping Cart Controller
  
![Screenshot 2024-06-21 at 12 02 43 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/7982d566-d855-4862-9d20-83b961e998a5)
- SQL CODE
  
![Screenshot 2024-06-21 at 12 03 05 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/4ce5b774-0aa1-41bd-a89c-d3dfbdfdd156)



### User Profile
Implemented user profile management:
Ensured that user profiles can be updated by the user.

- GET <code>/profile</code> : View the user's profile.
  
![Screenshot 2024-06-21 at 12 04 30 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/8c89d8a9-d9f0-4513-8871-537bf4b1a945)

- PUT <code>/profile</code> : Update the user's profile.
  - Last name changed from <code>Jeston</code> to <code>Jets</code>
  
![Screenshot 2024-06-21 at 12 05 29 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/8dc7f692-bdbd-47f5-aaba-a9b2d6c7d8bf)

## Database change reflected
![Screenshot 2024-06-21 at 12 06 05 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/99207f69-4824-48fa-b78b-e6990b1fbc6b)



## Testing
Extensively used Postman to test all API endpoints.

![Screenshot 2024-06-21 at 12 09 00 PM](https://github.com/twentyfive21/EasyShop-SpringApp/assets/107441301/aba46093-ca62-4bc9-b824-70c66b048cde)


