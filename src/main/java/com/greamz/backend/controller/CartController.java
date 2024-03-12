package com.greamz.backend.controller;

import com.greamz.backend.dto.cart.CartItemDTO;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping("/items")
    public ResponseEntity<List<CartItemDTO>> getCartItemsByAccountId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(cartService.getCartItemsByAccountId(userPrincipal.getId()));
    }
    @DeleteMapping("/delete/all")
    public ResponseEntity<Long> clearCart(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        cartService.deleteAllCart(userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String,Object>> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok(Map.of("id",id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCartItemById(id));
    }
    @PutMapping("/adjust-quantity")
    public ResponseEntity<CartItemDTO> adjustQuantity(@AuthenticationPrincipal UserPrincipal userPrincipal,@RequestParam Long id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(cartService.adjustQuantity(userPrincipal.getId(),id, quantity));
    }
    @PostMapping("/add")
    public ResponseEntity<CartItemDTO> addCartItem(@RequestBody CartItemDTO cartItemDTO,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        cartItemDTO.setAccountId(userPrincipal.getId());
        CartItemDTO cartItem = cartService.addCartItem(cartItemDTO);
        if(cartItem.isOutOfStock()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cartItem);
        }
        return ResponseEntity.ok(cartItem);
    }

}
