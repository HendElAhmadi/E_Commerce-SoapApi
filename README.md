# E_Commerce-SoapApi

This api gives E_Commerce services 

# Given services are:

1. User service
    
    1.  get all users
    2.  get user by userId
    3.  delete user by userId
    4.  add user by sending userDto 
    5.  delete user by userId
    6.  update user wallet

2. Product service

    1.  get all products
    2.  get product by productId
    3.  get product by productName
    4.  get list of product's categories by productId
    5.  create product by sending productDto
    6.  delete product by productId
    7.  update productQunatity by sending productId and the required quantity

3. Category service
    
    1.  get all categories
    2.  get category by categoryId
    3.  get list of category's products by categoryId
    4.  create cateogry by sending categoryDto
   
4. Cart Service

    1.  get all carts
    2.  get list of user's cart items by userId
    3.  add to cart by sending userId, productName, the required quantity of that product
    4.  delete cart by userId

5. Order Service

    1.  get all orders
    2.  get user's order by userId
    3.  make order by user's id
    4.  checkout by userId
    5.  delete order by userId



# Used in this project:

* JAVA language
* Maven
* MySQL DB
* JPA

# You can apply requests through POSTMAN collection file

# ER Diagram
![](/ER_DIAGRAM.png)
