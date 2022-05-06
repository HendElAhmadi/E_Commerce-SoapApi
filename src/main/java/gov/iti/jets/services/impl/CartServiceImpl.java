package gov.iti.jets.services.impl;

import java.util.ArrayList;
import java.util.List;

import gov.iti.jets.dtos.CartDto;
import gov.iti.jets.dtos.UserCart;
import gov.iti.jets.persistence.entities.CartId;
import gov.iti.jets.persistence.entities.CartProducts;
import gov.iti.jets.persistence.entities.Product;
import gov.iti.jets.persistence.util.ManagerFactory;
import gov.iti.jets.services.CartService;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@WebService(endpointInterface = "gov.iti.jets.services.CartService")
public class CartServiceImpl implements CartService {

    private final static EntityManagerFactory entityManagerFactory = ManagerFactory.getEntityManagerFactory();
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public List<CartDto> getAllcarts() {

        TypedQuery<CartProducts> query = entityManager.createQuery("select C from CartProducts C", CartProducts.class);
        List<CartDto> cartDtoList = new ArrayList<CartDto>();
        try {

            List<CartProducts> cartProductsList = query.getResultList();
            CartDto cartDto;

            for (CartProducts cartProducts : cartProductsList) {

                cartDto = new CartDto();

                cartDto.setCartProductId(cartProducts.getCartId().getProductId());
                cartDto.setCartUserId(cartProducts.getCartId().getUserId());
                cartDto.setTotalPrice(cartProducts.getTotalPrice());
                cartDto.setQuantity(cartProducts.getQuantity());

                cartDtoList.add(cartDto);
            }

            return cartDtoList;

        } catch (Exception e) {

            return cartDtoList;
        }
    }

    @Override
    public List<UserCart> getUserCart(int userId) {
        TypedQuery<CartProducts> query = entityManager.createQuery("select C from CartProducts C", CartProducts.class);

        List<CartProducts> cartProductsList = query.getResultList();
        UserCart userCart;
        List<UserCart> userCartList = new ArrayList<UserCart>();
        for (CartProducts cartProducts : cartProductsList) {
            if (cartProducts.getCartId().getUserId() == userId) {

                userCart = new UserCart();

                userCart.setCartProductId(cartProducts.getCartId().getProductId());

                userCart.setProductName(cartProducts.getProduct().getName());
                userCart.setQuantity(cartProducts.getQuantity());
                userCart.setTotalPrice(cartProducts.getTotalPrice());
                userCartList.add(userCart);
            }

        }

        return userCartList;
    }

    @Override
    public String addToCart(int userId, String productName, int quantity) {

        try {
            EntityManager entityManager2 = entityManagerFactory.createEntityManager();
            TypedQuery<Product> query = entityManager2
                    .createQuery("select p from Product p where p.name= :name ", Product.class)
                    .setParameter("name", productName);

            Product product = query.getSingleResult();

            int productQuantity = product.getQuantity();

            if (quantity > productQuantity) {

                return " Product quantity is less than the demanded!!";
            }

            EntityTransaction entityTransaction = entityManager2.getTransaction();
            entityTransaction.begin();

            CartId cartId = new CartId();
            cartId.setProductId(product.getId());
            cartId.setUserId(userId);

            TypedQuery<CartProducts> query2 = entityManager2
                    .createQuery("select c from CartProducts c where c.cartId= :cartId ", CartProducts.class)
                    .setParameter("cartId", cartId);

            if (query2.getResultList().size() != 0) {

                CartProducts cartProducts = query2.getResultList().get(0);
                if (cartProducts.getQuantity() == quantity) {
                    return "your product already exist with the same quantiy";
                }
                setUserCart(cartProducts, product, quantity);
                entityManager2.persist(cartProducts);
                entityTransaction.commit();
                entityManager2.close();

                return "Product is successfully added to your cart!!";
            }

            CartProducts cartProducts = new CartProducts();
            cartProducts.setCartId(cartId);
            setUserCart(cartProducts, product, quantity);
            entityManager2.persist(cartProducts);
            entityTransaction.commit();
            entityManager2.close();

            return "Product is successfully added to your cart!!";
        } catch (Exception e) {

            return "ther is no user with this id!!";
        }

    }

    private void setUserCart(CartProducts cartProducts, Product product, int quantity) {
        cartProducts.setQuantity(quantity);
        cartProducts.setTotalPrice(product.getPrice() * quantity);

    }

    @Override
    public String deleteCart(int userId) {
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        TypedQuery<CartProducts> query = entityManager2
                .createQuery("select C from CartProducts C where C.user.id= :id ", CartProducts.class)
                .setParameter("id", userId);
        if (query.getResultList().size() == 0) {
            return "there is no cart to delete";
        }
        List<CartProducts> cartProductsList = query.getResultList();

        EntityTransaction entityTransaction = entityManager2.getTransaction();
        entityTransaction.begin();
        for (CartProducts cartProducts : cartProductsList) {

            entityManager2.remove(cartProducts);

        }

        entityTransaction.commit();
        entityManager2.close();
        return "Cart is deleted succesfully";

    }

}
