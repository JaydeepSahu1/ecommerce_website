package com.example.ecommerce.controller;

import com.example.ecommerce.Exceptions.ProductException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.AddItemRequest;
import com.example.ecommerce.response.ApiResponse;
import com.example.ecommerce.service.CartItemService;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception, ProductException
    {
        User user=userService.findUserbyJwtToken(jwt);

        Cart cart=cartService.findUserCart(user);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws Exception
    {
        User user=userService.findUserbyJwtToken(jwt);
        Product product=productService.findProductbyId(req.getProductId());

        CartItem item =cartService.addCartItem
                (user,product,req.getSize(),req.getQuantity());

        ApiResponse res=new ApiResponse();
        res.setMessage("item added successfully");

        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt)
        throws Exception
    {
      User user=userService.findUserbyJwtToken(jwt);
      cartItemService.removeCartItem(user.getId(),cartItemId);

      ApiResponse res=new ApiResponse();
      res.setMessage("item deleted successfully");

      return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt)
        throws Exception
    {
        User user=userService.findUserbyJwtToken(jwt);

        CartItem updatedCartItem = null;

        if(cartItem.getQuantity()>0)
        {
            updatedCartItem=cartItemService.updateCartItem(
                    user.getId(),
                    cartItemId,
                    cartItem);
        }

        return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
    }

}
