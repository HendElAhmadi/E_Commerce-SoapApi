package gov.iti.jets.services;

import java.util.List;

import gov.iti.jets.dtos.CategoryDto;

import gov.iti.jets.dtos.ProductDto;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface CategoryService {

    @WebMethod
    public List<CategoryDto> getAllCategories();

    @WebMethod
    public Object getCategory(int id);

    @WebMethod
    public List<ProductDto> getProducts(Integer id);

    @WebMethod
    public String createCategory(CategoryDto categoryDto);

}
