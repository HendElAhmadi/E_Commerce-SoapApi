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
    5.  delete product from cart by userId and productId

5. Order Service

    1.  get all orders
    2.  get user's order by userId
    3.  make order by user's id
    4.  checkout by userId
    5.  delete order by userId

# Handled Cases:


 1. When transaction is done :
    1. user cart items are deleted as well as to his order ,
    2. his money is decreased by the paid amount of money 
    3.the product quantity is decreased in stock
    
2. User get a message that can't add a product if the quantity is not enough

3. If two users added the same quantity in their order and one of them checked out:
    
    1. If product quantity gets zero a product out of stock message gets displayed
    2. If product quantity gets is less than the demanded the user get a message telling him that the quantity is not enough
4. If user to be deleted:
   
    1. If he has an order a message is sent as order should be deleted first due to the association
    2. If his order is deleted but he has a cart a message is sent as the cart should be deleted first due to the association
    3. If he doesn't have neither cart nor order he is deleted successfully
 

# Used in this project:

* JAVA language
* Maven
* MySQL DB
* JPA

# You can apply requests through POSTMAN collection file

# ER Diagram
![](/ER_DIAGRAM.png)
