package gov.iti.jets.services;

import java.util.List;

import gov.iti.jets.dtos.OrderDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface OrderService {
    
    @WebMethod
    public List<OrderDto> getAllOrders();

    @WebMethod
    public Object getOrderById(int userId);

    @WebMethod
    public String makeOrder(int userId);

    @WebMethod
    public String checkout(int userId);

    @WebMethod
    public String deleteOrder(int userId);
}
