package gov.iti.jets.services;



import java.util.List;

import gov.iti.jets.dtos.CategoryDto;
import gov.iti.jets.dtos.ProductDto;
import gov.iti.jets.exceptions.NotFoundDtoException;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface CategoryService {

    @WebMethod
    public List<CategoryDto> getAllCategories() throws NotFoundDtoException;

    @WebMethod
    public CategoryDto getCategory(int id)throws NotFoundDtoException;

    @WebMethod
    public List<ProductDto> getProducts(Integer id)throws NotFoundDtoException;

    @WebMethod
    public String createCategory(CategoryDto categoryDto);

}
