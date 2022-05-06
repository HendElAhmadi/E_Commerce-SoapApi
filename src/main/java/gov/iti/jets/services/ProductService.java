package gov.iti.jets.services;

import gov.iti.jets.dtos.ProductDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface ProductService {

    @WebMethod
    public String getAllProducts();

    @WebMethod
    public Object getProduct(int id);

    @WebMethod
    public Object getProductByName(String name);

    @WebMethod
    public String getCategories(Integer id);

    @WebMethod
    public String createProduct(ProductDto productDto);

    @WebMethod
    public String deleteProduct(int id);

    @WebMethod
    public String updateProductQuantity(int id, int quantity);

}
