package gov.iti.jets.services;

import java.util.List;

import gov.iti.jets.dtos.OrderDto;
import gov.iti.jets.exceptions.NotFoundDtoException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface OrderService {

    @WebMethod
    public List<OrderDto> getAllOrders() throws NotFoundDtoException;

    @WebMethod
    public OrderDto getOrderById(int userId) throws NotFoundDtoException;

    @WebMethod
    public String makeOrder(int userId);

    @WebMethod
    public String checkout(int userId);

    @WebMethod
    public String deleteOrder(int userId);

    @WebMethod
    public String updateOrder(int userId) ;
}
