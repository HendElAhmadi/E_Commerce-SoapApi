package gov.iti.jets.services;



import gov.iti.jets.dtos.CategoryDto;


import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface CategoryService {

    @WebMethod
    public String getAllCategories();

    @WebMethod
    public String getCategory(int id);

    @WebMethod
    public String getProducts(Integer id);

    @WebMethod
    public String createCategory(CategoryDto categoryDto);

}
