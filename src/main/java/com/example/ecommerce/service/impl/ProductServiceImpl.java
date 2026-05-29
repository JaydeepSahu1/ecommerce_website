package com.example.ecommerce.service.impl;

import com.example.ecommerce.Exceptions.ProductException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.Seller;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.request.CreateProductRequeat;
import com.example.ecommerce.service.ProductService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequeat req, Seller seller)
    {
        Category category1=categoryRepository.findByCategoryId(req.getCategory1());

        if(category1==null)
        {
            Category category=new Category();
            category.setCategoryId(req.getCategory1());
            category.setLevel(1);
            category1=categoryRepository.save(category);
        }

        Category category2=categoryRepository.findByCategoryId(req.getCategory2());
        if(category2==null)
        {
            Category category=new Category();
            category.setCategoryId((req.getCategory2()));
            category.setLevel(2);
            category.setParentCategory(category1);
            category2=categoryRepository.save(category);
        }

        Category category3=categoryRepository.findByCategoryId(req.getCategory3());
        if(category3==null)
        {
            Category category=new Category();
            category.setCategoryId((req.getCategory3()));
            category.setLevel(3);
            category.setParentCategory(category2);
            category3=categoryRepository.save(category);
        }

        int discountPercentage=calculateDiscountPercentage(req.getMrpPrice(),req.getMrpPrice());

        Product product=new Product();
        product.setSeller(seller);
        product.setCategory(category3);
        product.setDescription(req.getDescription());
        product.setCreatedAt(LocalDateTime.now());
        product.setTitle(req.getTitle());
        product.setColour(req.getColor());
        product.setSellingPrice(req.getSellingPrice());
        product.setMrpPrice(req.getMrpPrice());
        product.setImages(req.getImages());
        product.setSizes(req.getSizes());
        product.setDiscountPercentage(discountPercentage);

    return productRepository.save(product);
    }

    private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
        if(mrpPrice<=0)
        {
            throw new IllegalArgumentException("MrpPrice must be greater than 0");
        }
        double discount=mrpPrice-sellingPrice;
        double discountPercentage=(discount/mrpPrice)*100;
        return (int)discountPercentage;
    }

    @Override
    public void deleteProduct(Long productId) throws ProductException {
        Product product=findProductbyId(productId);
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {

        findProductbyId(productId);
        product.setId(productId);


        return productRepository.save(product);
    }

    @Override
    public Product findProductbyId(Long productId) throws ProductException {
        return productRepository.findById(productId).orElseThrow(()->
                new ProductException("product not found with id " + productId));
    }

    @Override
    public List<Product> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }

    @Override
    public Page<Product> getAllProducts(String category, String brand, String colors,
                                        String sizes, Integer minPrice, Integer maxPrice,
                                        Integer minDiscount, String sort, String stock, Integer pageNumber)
    {
        Specification<Product> spec=(root, query, criteriaBuilder) ->{
            List<Predicate> predicates=new ArrayList<>();

            if(category!=null)
            {
                Join<Product,Category> categoryJoin=root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("CategoryId"),category));
            }
            if(colors!=null && !colors.isEmpty())
            {
                predicates.add(criteriaBuilder.equal(root.get("color"),colors));
            }
            if(sizes!=null && !sizes.isEmpty())
            {
                predicates.add(criteriaBuilder.equal(root.get("sizes"),sizes));
            }
            if(minPrice!=null)
            {
             predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if(maxPrice!=null)
            {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if(minDiscount!=null)
            {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discount"), minDiscount));
            }
            if(stock!=null)
            {
                predicates.add(criteriaBuilder.equal(root.get("stock"),stock));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        if(sort!=null && !sort.isEmpty())
        {
            pageable = switch (sort)
            {
                case "price_low" -> PageRequest.of(pageNumber!=null? pageNumber:0 ,10,
                        Sort.by("sellingPrice").ascending());
                case "price_high" -> PageRequest.of(pageNumber!=null? pageNumber:0 ,10,
                        Sort.by("sellingPrice").descending());
                default -> PageRequest.of(pageNumber!=null? pageNumber:0 ,10,
                        Sort.unsorted());
            };
        }
        else{
            pageable=PageRequest.of(pageNumber!=null? pageNumber:0 ,10,Sort.unsorted());
        }

        return productRepository.findAll(spec,pageable);

    }

    @Override
    public List<Product> getProductBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }
}
