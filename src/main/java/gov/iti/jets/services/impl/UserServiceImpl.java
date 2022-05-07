package gov.iti.jets.services.impl;

import java.util.ArrayList;
import java.util.List;

import gov.iti.jets.dtos.UserDto;
import gov.iti.jets.dtos.UserWalletDto;
import gov.iti.jets.exceptions.NotFoundDtoException;
import gov.iti.jets.persistence.entities.CartProducts;
import gov.iti.jets.persistence.entities.Order;
import gov.iti.jets.persistence.entities.User;
import gov.iti.jets.persistence.entitiesservices.QueryService;
import gov.iti.jets.persistence.entitiesservices.QueryServiceImpl;
import gov.iti.jets.persistence.util.ManagerFactory;
import gov.iti.jets.services.UserService;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@WebService(endpointInterface = "gov.iti.jets.services.UserService")
public class UserServiceImpl implements UserService {

    EntityManagerFactory entityManagerFactory = ManagerFactory.getEntityManagerFactory();
    private EntityManager entityManager = entityManagerFactory.createEntityManager();
    private QueryService queryService = new QueryServiceImpl();
    
    @Override
    public List<UserDto> getAllUsers() throws NotFoundDtoException {
        TypedQuery<User> query = queryService.getAllUsers(entityManager);
        List<UserDto> userDtoList = new ArrayList<UserDto>();
        

            List<User> userList = query.getResultList();
            UserDto userDto;
            
            for (User user : userList) {
                userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setUserType(user.getUserType());
                userDto.setUserName(user.getUserName());
                userDto.setEmail(user.getEmail());
                userDto.setPhoneNumber(user.getPhoneNumber());
                userDto.setWallet(user.getWallet());
                userDto.setPassword(user.getPassword());
                userDtoList.add(userDto);
            }
            if (userDtoList.size() == 0) {
                
                throw new NotFoundDtoException("There are no users");
            }

            return userDtoList;

        
    }

    @Override
    public UserDto getUserById(int id) throws NotFoundDtoException {

        TypedQuery<User> query = queryService.getUserById(entityManager, id);

        try {
            User user = query.getSingleResult();

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUserType(user.getUserType());
            userDto.setUserName(user.getUserName());
            userDto.setEmail(user.getEmail());
            userDto.setPhoneNumber(user.getPhoneNumber());
            userDto.setWallet(user.getWallet());
            userDto.setPassword(user.getPassword());

            System.out.print(user);

            return userDto;
        } catch (Exception e) {

             throw new NotFoundDtoException("There is no user with this id!");
            
        }
    }

    @Override
    public String createUser(UserDto userDto) {

        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        TypedQuery<User> query = queryService.getUserEmail(entityManager2, userDto.getEmail());
        if (query.getResultList().size() != 0) {
            return "email already exists";

        }
        

        EntityTransaction entityTransaction = entityManager2.getTransaction();
        entityTransaction.begin();
        User user1 = new User();
        user1.setUserName(userDto.getUserName());
        user1.setPassword(userDto.getPassword());
        user1.setUserType(userDto.getUserType());
        user1.setEmail(userDto.getEmail());
        user1.setPhoneNumber(userDto.getPhoneNumber());
        user1.setWallet(userDto.getWallet());

        entityManager2.persist(user1);
        entityTransaction.commit();
        System.out.println(user1);
        System.out.println("userDto = " + userDto);

        entityManager2.close();

        return "user created successfully";
    }

    @Override
    public String deleteUser(int id) {

        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        TypedQuery<User> query =  queryService.getUserById(entityManager2, id);

        try {
            EntityTransaction entityTransaction = entityManager2.getTransaction();
            entityTransaction.begin();
            User user = query.getSingleResult();

            TypedQuery<Order> query2 = queryService.getOrderByUserId(entityManager2, id);
            if (query2.getResultList().size() != 0) {

                return "user has an order ,you should delete order first";
            }

            TypedQuery<CartProducts> query3 = queryService.getCartByUserId(entityManager2, id);
            if (query3.getResultList().size() != 0) {

                return "user has a cart ,you should delete the  user cart first";
            }

            entityManager2.remove(user);
            entityTransaction.commit();
            entityManager2.close();
            return "user deleted successfully";
        } catch (Exception e) {

            return "There is no user with this id!";
        }
    }

    @Override
    public String upadateUserWallet(int id,UserWalletDto userWalletDto){

        TypedQuery<User> query =  queryService.getUserById(entityManager, id);

        try {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            User user = query.getSingleResult();

            user.setWallet(user.getWallet() + userWalletDto.getUserWallet());
            entityManager.persist(user);
            entityTransaction.commit();
           
            return "User wallet updated successfully";
        } catch (Exception e) {

            return "There is no user with this id!";
        }

    }

}
