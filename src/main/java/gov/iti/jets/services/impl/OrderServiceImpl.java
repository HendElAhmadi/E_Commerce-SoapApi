package gov.iti.jets.services.impl;

import java.util.ArrayList;
import java.util.List;

import gov.iti.jets.dtos.OrderDto;
import gov.iti.jets.dtos.UserDto;
import gov.iti.jets.persistence.entities.CartProducts;
import gov.iti.jets.persistence.entities.Order;
import gov.iti.jets.persistence.entities.Product;
import gov.iti.jets.persistence.entities.User;
import gov.iti.jets.persistence.util.ManagerFactory;
import gov.iti.jets.services.OrderService;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@WebService(endpointInterface = "gov.iti.jets.services.OrderService")
public class OrderServiceImpl implements OrderService {

    private final static EntityManagerFactory entityManagerFactory = ManagerFactory.getEntityManagerFactory();
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public String getAllOrders() {
        List<OrderDto> orderDtoList = new ArrayList<OrderDto>();

        TypedQuery<Order> query = entityManager.createQuery("select o from Order o", Order.class);
        List<Order> orderList = query.getResultList();
        OrderDto orderDto;

        for (Order order : orderList) {
            orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setTotalPrice(order.getTotalPrice());
            User user = order.getUser();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUserType(user.getUserType());
            userDto.setUserName(user.getUserName());
            userDto.setEmail(user.getEmail());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setWallet(user.getWallet());
            userDto.setPassword(user.getPassword());
            orderDto.setUserDto(userDto);
            orderDtoList.add(orderDto);
        }
        if (orderDtoList.size() == 0) {
            return "There are no orders!!";
        }

        return "\n All orders : \n" + orderDtoList;

    }

    @Override
    public Object getOrderById(int userId) {

        try {
            TypedQuery<Order> query = entityManager
                    .createQuery("select o from Order o where o.user.id= :id ", Order.class)
                    .setParameter("id", userId);
            Order order = query.getSingleResult();
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setTotalPrice(order.getTotalPrice());
            User user = order.getUser();
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUserType(user.getUserType());
            userDto.setUserName(user.getUserName());
            userDto.setEmail(user.getEmail());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setWallet(user.getWallet());
            userDto.setPassword(user.getPassword());
            orderDto.setUserDto(userDto);

            return orderDto;
        } catch (Exception e) {

            return "There is no order!";
        }
    }

    @Override
    public String makeOrder(int userId) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<User> query1 = entityManager.createQuery("select u from User u where u.id= :id ", User.class)
                .setParameter("id", userId);
        if (query1.getResultList().size() == 0) {
            return "user doesn't exist!!";
        }

        TypedQuery<Order> query = entityManager.createQuery("select o from Order o where o.user.id= :id ", Order.class)
                .setParameter("id", userId);
        if (query.getResultList().size() != 0) {

            return "order already exists check it out by get Method";
        }

        List<CartProducts> cartProductsList = getUserCart(entityManager);
        int totalPrice = 0;
        for (CartProducts cartProducts : cartProductsList) {
            if (cartProducts.getCartId().getUserId() == userId) {

                int productQuantity = cartProducts.getProduct().getQuantity();
                if (productQuantity == 0) {

                    return "we are sorry but the product is out of stock !!";
                }
                if (productQuantity < cartProducts.getQuantity()) {

                    return "the" + cartProducts.getProduct().getName() + " product quantity\n" +
                            "in stock is only : " + productQuantity;
                }
                totalPrice += cartProducts.getTotalPrice();
            }

        }
        if (totalPrice == 0) {

            return "cart is empty";
        }
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        Order order = new Order();
        order.setTotalPrice(totalPrice);
        order.setUser(entityManager.find(User.class, userId));
        entityManager.persist(order);
        entityTransaction.commit();

        entityManager.close();
        return "Order is created successfully";

    }

    @Override
    public String checkout(int userId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Order> query = entityManager.createQuery("select o from Order o where o.user.id= :id ", Order.class)
                .setParameter("id", userId);
        if (query.getResultList().size() == 0) {

            return "Please place your order";
        }

        Order order = query.getSingleResult();
        User user = order.getUser();

        if (user.getWallet() < order.getTotalPrice()) {

            return "user doesn't have enough money";
        }

        List<CartProducts> cartProductsList = getUserCart(entityManager);
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        int i = 0;
        for (CartProducts cartProducts : cartProductsList) {

            if (cartProducts.getCartId().getUserId() == userId) {

                TypedQuery<Product> query2 = entityManager
                        .createQuery("select p from Product p where p.id= :id ", Product.class)
                        .setParameter("id", cartProducts.getCartId().getProductId());
                Product product = query2.getSingleResult();
                product.setQuantity(product.getQuantity() - cartProducts.getQuantity());
                entityManager.persist(product);
                entityManager.remove(cartProducts);
                i++;
            }
        }
        if (i == 0) {

            return "there is no cart !!";
        }
        user.setWallet(user.getWallet() - order.getTotalPrice());
        entityManager.persist(user);
        entityManager.remove(order);
        entityTransaction.commit();
        entityManager.close();
        return "Transaction done successfully";
    }

    @Override
    public String deleteOrder(int userId) {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            TypedQuery<Order> query = entityManager
                    .createQuery("select o from Order o where o.user.id= :id ", Order.class)
                    .setParameter("id", userId);
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            Order order = query.getSingleResult();

            entityManager.remove(order);
            entityTransaction.commit();
            entityManager.close();
            return "user order deleted successfully";
        } catch (Exception e) {

            return "There is no order to delete!";
        }
    }

    private List<CartProducts> getUserCart(EntityManager entityManager) {
        TypedQuery<CartProducts> query2 = entityManager.createQuery("select C from CartProducts C", CartProducts.class);
        List<CartProducts> cartProductsList = query2.getResultList();

        return cartProductsList;
    }

}
