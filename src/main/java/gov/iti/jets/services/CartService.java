package gov.iti.jets.services;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface CartService {

    @WebMethod
    public String getAllcarts();

    @WebMethod
    public String getUserCart(int userId);

    @WebMethod
    public String addToCart(int userId, String productName, int quantity);

    @WebMethod
    public String deleteCart(int userId);

    @WebMethod
    public String deleteProductInCart(int userId,int pId) ;

}
