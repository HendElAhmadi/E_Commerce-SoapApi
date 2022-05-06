package gov.iti.jets.services;

import java.util.List;
import gov.iti.jets.dtos.ProductDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import gov.iti.jets.dtos.CategoryDto;

@WebService
public interface ProductService {

    @WebMethod
    public List<ProductDto> getAllProducts();

    @WebMethod
    public Object getProduct(int id) ;

    @WebMethod
    public Object getProductByName(String name) ;

    @WebMethod
    public List<CategoryDto> getCategories(Integer id) ;

    @WebMethod
    public String createProduct(ProductDto productDto) ;
     
    @WebMethod
    public String deleteProduct( int id) ;

    @WebMethod
    public String updateProductQuantity(int id,int quantity) ;

 
}
