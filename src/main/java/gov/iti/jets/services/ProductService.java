package gov.iti.jets.services;

import java.util.List;

import gov.iti.jets.dtos.CategoryDto;
import gov.iti.jets.dtos.ProductDto;
import gov.iti.jets.exceptions.NotFoundDtoException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface ProductService {

    @WebMethod
    public List<ProductDto> getAllProducts() throws NotFoundDtoException;

    @WebMethod
    public ProductDto getProduct(int id)throws NotFoundDtoException;

    @WebMethod
    public ProductDto getProductByName(String name)throws NotFoundDtoException;

    @WebMethod
    public List<CategoryDto> getCategories(Integer id)throws NotFoundDtoException;

    @WebMethod
    public String createProduct(ProductDto productDto);

    @WebMethod
    public String deleteProduct(int id);

    @WebMethod
    public String updateProductQuantity(int id, int quantity);

}
