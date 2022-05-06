package gov.iti.jets.services.impl;

import java.util.ArrayList;
import java.util.List;

import gov.iti.jets.dtos.UserDto;
import gov.iti.jets.dtos.UserWalletDto;
import gov.iti.jets.persistence.entities.User;
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

    @Override
    public List<UserDto> getAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("select u from User u", User.class);
        List<UserDto> userDtoList = new ArrayList<UserDto>();
        try {

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

            return userDtoList;

        } catch (Exception e) {

            return userDtoList;
        }
    }

    @Override
    public Object getUserById(int id) {
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id= :id ", User.class)
                .setParameter("id", id);

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

            return "There is no user with this id!";
        }
    }

    @Override
    public String createUser(UserDto userDto) {

        EntityManager entityManager2 = entityManagerFactory.createEntityManager();

        TypedQuery<User> query = entityManager2.createQuery("select u from User u where u.email= :email ", User.class)
                .setParameter("email", userDto.getEmail());
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
        TypedQuery<User> query = entityManager2.createQuery("select u from User u where u.id= :id ", User.class)
                .setParameter("id", id);
        
        try {   
            EntityTransaction entityTransaction = entityManager2.getTransaction();
            entityTransaction.begin();
            User user = query.getSingleResult();

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

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = entityManager.createQuery("select u from User u where u.id= :id ", User.class)
                .setParameter("id", id);

        try {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            User user = query.getSingleResult();

            user.setWallet(user.getWallet()+userWalletDto.getUserWallet());
            entityManager.persist(user);
            entityTransaction.commit();
            entityManager.close();
            return "User wallet updated successfully";
        } catch (Exception e) {

            return "There is no user with this id!";
        }

    
    }

}
