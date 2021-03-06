package gov.iti.jets.services.impl;

import java.util.ArrayList;
import java.util.List;

import gov.iti.jets.dtos.CategoryDto;
import gov.iti.jets.dtos.ProductDto;
import gov.iti.jets.exceptions.NotFoundDtoException;
import gov.iti.jets.persistence.entities.CartProducts;
import gov.iti.jets.persistence.entities.Category;
import gov.iti.jets.persistence.entities.Product;
import gov.iti.jets.persistence.entitiesservices.QueryService;
import gov.iti.jets.persistence.entitiesservices.QueryServiceImpl;
import gov.iti.jets.persistence.util.ManagerFactory;
import gov.iti.jets.services.ProductService;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@WebService(endpointInterface = "gov.iti.jets.services.ProductService")
public class ProductServiceImpl implements ProductService {

    private final static EntityManagerFactory entityManagerFactory = ManagerFactory.getEntityManagerFactory();
    private EntityManager entityManager = entityManagerFactory.createEntityManager();
    private QueryService queryService = new QueryServiceImpl();


    @Override
    public List<ProductDto> getAllProducts() throws NotFoundDtoException {

        TypedQuery<Product> query = queryService.getAllProducts(entityManager);        
        List<ProductDto> productDtoList = new ArrayList<ProductDto>();

        List<Product> productList = query.getResultList();
        ProductDto productDto;
        for (Product product : productList) {
            productDto = new ProductDto();

            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCategories(product.getCategories());
            productDto.setQuantity(product.getQuantity());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDtoList.add(productDto);
        }

        if (productDtoList.size() == 0) {
            throw new NotFoundDtoException("There are no products") ;
        }

        return  productDtoList;

    }

    @Override
    public ProductDto getProduct(int id) throws NotFoundDtoException {

        TypedQuery<Product> query = queryService.getProductById(entityManager, id);
        try {
            Product product = query.getSingleResult();

            ProductDto productDto = new ProductDto();

            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCategories(product.getCategories());
            productDto.setQuantity(product.getQuantity());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());

            return productDto;
        } catch (Exception e) {

            throw new NotFoundDtoException("There is no products!") ;
        }
    }

    @Override
    public ProductDto getProductByName(String name) throws NotFoundDtoException {

        TypedQuery<Product> query = queryService.getProductByName(entityManager, name);

             
        try {
            Product product = query.getSingleResult();
            ProductDto productDto = new ProductDto();

            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCategories(product.getCategories());
            productDto.setQuantity(product.getQuantity());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());

            return productDto;
        } catch (Exception e) {

            throw new NotFoundDtoException("There is no product with this name!");
            
        }
    }

    @Override
    public List<CategoryDto> getCategories(Integer id) throws NotFoundDtoException {

        try {

            TypedQuery<Product> query = queryService.getProductById(entityManager, id);
            List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();

            Product product = query.getSingleResult();

            for (Category category : product.getCategories()) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(category.getId());
                categoryDto.setDescription(category.getDescription());
                categoryDto.setValue(category.getValue());
                categoryDtoList.add(categoryDto);
            }
            if (categoryDtoList.size() == 0) {

                throw new NotFoundDtoException("There are no categories");
                
            }

            return  categoryDtoList;
        } catch (Exception e) {

            throw new NotFoundDtoException("There is no product with this id");
            

        }

    }

    @Override
    public String createProduct(ProductDto productDto) {
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        TypedQuery<Product> query = queryService.getProductByName(entityManager2, productDto.getName());
        if (query.getResultList().size() != 0) {
            return "Product already exists";

        }
        EntityTransaction entityTransaction = entityManager2.getTransaction();
        entityTransaction.begin();
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setQuantity(productDto.getQuantity());
        product.setCategories(productDto.getCategories());
        product.setPrice(productDto.getPrice());
        entityManager2.persist(product);
        entityTransaction.commit();
        System.out.println(product);
        System.out.println("productDto = " + productDto);
        entityManager2.close();

        return "Product is created successfully";
    }

    @Override
    public String deleteProduct(int id) {
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        TypedQuery<Product> query = queryService.getProductById(entityManager2, id);

        try {
            EntityTransaction entityTransaction = entityManager2.getTransaction();
            entityTransaction.begin();
            Product product = query.getSingleResult();

            TypedQuery<CartProducts> query3 = queryService.getCartByProductId(entityManager2, id);

            if (query3.getResultList().size() != 0) {
                List<CartProducts>CartProductsList=query3.getResultList();
                List<Integer>cartIds=new ArrayList<>();
                for (CartProducts cartProducts:CartProductsList) {
                    cartIds.add(cartProducts.getCartId().getUserId());
                }
                return "Here are the users Ids who use this product: "+cartIds+"\n ,you should delete theirs carts first";
            }

            entityManager2.remove(product);
            entityTransaction.commit();
            entityManager2.close();
            return "product deleted successfully";
        } catch (Exception e) {

            return "There is no product with this id!";
        }
    }

    @Override
    public String updateProductQuantity(int id, int quantity) {
        EntityManager entityManager2 = entityManagerFactory.createEntityManager();
        TypedQuery<Product> query = queryService.getProductById(entityManager2, id);

        try {
            EntityTransaction entityTransaction = entityManager2.getTransaction();
            entityTransaction.begin();
            Product product = query.getSingleResult();
            if (product.getQuantity() == quantity) {
                return "It is the same product quantity!!";
            }
            product.setQuantity(quantity);
            entityTransaction.commit();
            entityManager2.close();
            return "product updated successfully";
        } catch (Exception e) {

            return "There is no product with this id!";
        }
    }

}
