package gov.iti.jets.services;

import java.util.List;

import gov.iti.jets.dtos.CartDto;
import gov.iti.jets.dtos.UserCart;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface CartService {

    @WebMethod
    public List<CartDto> getAllcarts();

    @WebMethod
    public List<UserCart> getUserCart(int userId);

    @WebMethod
    public String addToCart(int userId, String productName, int quantity);

    @WebMethod
    public String deleteCart(int userId);

}
