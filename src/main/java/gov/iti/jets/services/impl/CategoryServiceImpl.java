package gov.iti.jets.services.impl;

import java.util.ArrayList;
import java.util.List;

import gov.iti.jets.dtos.CategoryDto;
import gov.iti.jets.dtos.ProductDto;
import gov.iti.jets.persistence.entities.Category;
import gov.iti.jets.persistence.entities.Product;
import gov.iti.jets.persistence.entitiesservices.QueryService;
import gov.iti.jets.persistence.entitiesservices.QueryServiceImpl;
import gov.iti.jets.persistence.util.ManagerFactory;
import gov.iti.jets.services.CategoryService;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@WebService(endpointInterface = "gov.iti.jets.services.CategoryService")
public class CategoryServiceImpl implements CategoryService {

    private final static EntityManagerFactory entityManagerFactory = ManagerFactory.getEntityManagerFactory();
    private EntityManager entityManager = entityManagerFactory.createEntityManager();
    private QueryService queryService =new QueryServiceImpl();

    @Override
    public String getAllCategories() {

        TypedQuery<Category> query = queryService.getAllCategories(entityManager);
        List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();

        List<Category> categoryList = query.getResultList();
        CategoryDto categoryDto;

        for (Category category : categoryList) {
            categoryDto = new CategoryDto();

            categoryDto.setId(category.getId());
            categoryDto.setDescription(category.getDescription());
            categoryDto.setValue(category.getValue());
            List<ProductDto> productDtoList = new ArrayList<ProductDto>();
            for (Product product : category.getProducts()) {
                ProductDto productDto = new ProductDto();

                productDto.setId(product.getId());
                productDto.setName(product.getName());
                productDto.setCategories(product.getCategories());
                productDto.setDescription(product.getDescription());
                productDto.setPrice(product.getPrice());
                productDto.setQuantity(product.getQuantity());
                productDtoList.add(productDto);
            }
            categoryDto.setProducts(productDtoList);

            categoryDtoList.add(categoryDto);
        }
        if (categoryDtoList.size() == 0) {
            return "There are no categories";
        }

        return "\n All categories : \n" + categoryDtoList;

    }

    @Override
    public String getCategory(int id) {

        TypedQuery<Category> query = queryService.getCategoryById(entityManager, id);

        try {
            Category category = query.getSingleResult();
            CategoryDto categoryDto = new CategoryDto();

            categoryDto.setId(category.getId());
            categoryDto.setDescription(category.getDescription());
            categoryDto.setValue(category.getValue());
            List<ProductDto> productDtoList = new ArrayList<ProductDto>();
            for (Product product : category.getProducts()) {
                ProductDto productDto = new ProductDto();

                productDto.setId(product.getId());
                productDto.setName(product.getName());
                productDto.setCategories(product.getCategories());
                productDto.setDescription(product.getDescription());
                productDto.setPrice(product.getPrice());
                productDto.setQuantity(product.getQuantity());
                productDtoList.add(productDto);
            }
            categoryDto.setProducts(productDtoList);

            return categoryDto.toString();
        } catch (Exception e) {

            return "There is no category with this id!";
        }
    }

    @Override
    public String getProducts(Integer id) {
      TypedQuery<Category> query = queryService.getCategoryById(entityManager, id);
        List<ProductDto> productDtoList = new ArrayList<ProductDto>();
        try {
            Category category = query.getSingleResult();

            for (Product product : category.getProducts()) {
                ProductDto productDto = new ProductDto();
                productDto.setId(product.getId());
                productDto.setName(product.getName());
                productDto.setCategories(product.getCategories());
                productDto.setDescription(product.getDescription());
                productDto.setQuantity(product.getQuantity());
                productDto.setPrice(product.getPrice());
                productDtoList.add(productDto);
            }
            

            return "\n products list : \n" + productDtoList;
        } catch (Exception e) {

            return "There are no products";
        }
    }

    @Override
    public String createCategory(CategoryDto categoryDto) {

        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        TypedQuery<Category> query = queryService.getCategoryValue(entityManager2, categoryDto.getValue());
        if (query.getResultList().size() != 0) {
            return "Category already exists";

        }

        EntityTransaction entityTransaction = entityManager2.getTransaction();
        entityTransaction.begin();
        Category category = new Category();
        category.setValue(categoryDto.getValue());
        category.setDescription(categoryDto.getDescription());

        entityManager2.persist(category);
        entityTransaction.commit();
        System.out.println(category);
        System.out.println("categoryDto = " + categoryDto);
        entityManager2.close();

        return "Category is created successfully";

    }

}
