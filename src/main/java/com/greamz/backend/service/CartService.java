package com.greamz.backend.service;

import com.cloudinary.provisioning.Account;
import com.greamz.backend.dto.cart.CartItemDTO;
import com.greamz.backend.dto.game.GameCart;
import com.greamz.backend.dto.game.GameDetailClientDTO;
import com.greamz.backend.dto.platform.PlatformBasicDTO;
import com.greamz.backend.dto.platform.PlatformDTO;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Cart;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.model.Platform;
import com.greamz.backend.repository.ICartRepo;
import com.greamz.backend.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ICartRepo cartRepo;
    private final CodeActiveService codeActiveService;
    @Transactional

    public void deleteCart(Long id) {
        cartRepo.deleteById(id);
    }
    @Transactional
    public void deleteAllCart(Integer id) {
        cartRepo.deleteAllByAccount_Id(id);
    }

    public List<CartItemDTO> getCartItemsByAccountId(Integer id) {
        List<Cart> carts = cartRepo.findAllByAccount_Id(id);
        return carts.stream().map(this::mapCartToCartItemDTO).toList();
    }
    public CartItemDTO addCartItem(CartItemDTO cartItemDTO) {
        Optional<Cart> cartOptional = cartRepo.findByGame_AppidAndPlatform_IdAndAccount_Id(cartItemDTO.getGame().getAppid(), cartItemDTO.getPlatform().getId(), cartItemDTO.getAccountId());
        Cart cart;
        int stock = codeActiveService.countByGameAppid(cartItemDTO.getPlatform().getId(),cartItemDTO.getGame().getAppid());
        if(stock < cartItemDTO.getQuantity()){
            cartItemDTO.setQuantity(stock);
            cartItemDTO.setOutOfStock(true);
            return cartItemDTO;
        }
        if(cartOptional.isPresent()){
            cart = cartOptional.get();
            cart.setQuantity(cart.getQuantity() + cartItemDTO.getQuantity());
            if(stock < cart.getQuantity()){
                cart.setQuantity(stock);
                Cart cartSaved= cartRepo.save(cart);
                cartItemDTO.setId(cartSaved.getId());
                cartItemDTO.setQuantity(stock);
                cartItemDTO.setOutOfStock(true);
                return cartItemDTO;

            }else {
                return mapCartToCartItemDTO(cartRepo.save(cart));
            }
        }else {
            cart = mapCartItemDTOToCart(cartItemDTO);
            if(stock < cart.getQuantity()){
                cart.setQuantity(stock);
                Cart cartSaved= cartRepo.save(cart);
                cartItemDTO.setId(cartSaved.getId());
                cartItemDTO.setQuantity(stock);
                cartItemDTO.setOutOfStock(true);
                return cartItemDTO;
            }else {
                return mapCartToCartItemDTO(cartRepo.save(cart));
            }
        }



    }
    public CartItemDTO adjustQuantity(Integer accountId,Long id, Integer quantity) {
        Cart cart = cartRepo.findByIdAndAccount_Id(id,accountId).orElse(null);
        if (Objects.isNull(cart)) return null;

        int stock = codeActiveService.countByGameAppid(cart.getPlatform().getId(),cart.getGame().getAppid());
        if (cart.getQuantity() > stock) {
            cart.setQuantity(stock);

            return mapCartToCartItemDTO(cartRepo.save(cart));
        };
        cart.setQuantity( quantity);

        return mapCartToCartItemDTO(cartRepo.save(cart));
    }
    public CartItemDTO getCartItemById(Long id) {
        Optional<Cart> cart = cartRepo.findById(id);
        return cart.map(this::mapCartToCartItemDTO).orElse(null);
    }
    public CartItemDTO mapCartToCartItemDTO(Cart cart) {
        CartItemDTO cartItemDTO=CartItemDTO.builder()
                .accountId(cart.getAccount().getId())
                .game(Mapper.mapObject(cart.getGame(), GameCart.class))
                .quantity(cart.getQuantity())
                .id(cart.getId())
                .stock(codeActiveService.countByGameAppid(cart.getPlatform().getId(),cart.getGame().getAppid()))
                .platform(Mapper.mapObject(cart.getPlatform(), PlatformBasicDTO.class))
                .build();

        cartItemDTO.setOutOfStock(cartItemDTO.getStock() <=0);
        return cartItemDTO;
    }
    public Cart mapCartItemDTOToCart(CartItemDTO cartItemDTO) {
        return Cart.builder()
                .account(AccountModel.builder().id(cartItemDTO.getAccountId()).build())
                .game(GameModel.builder().appid(cartItemDTO.getGame().getAppid()).build())
                .quantity(cartItemDTO.getQuantity())
                .platform(Platform.builder().id(cartItemDTO.getPlatform().getId()).build())
                .id(cartItemDTO.getId())
                .build();
    }
    private boolean isOutOfStock(CartItemDTO cartItemDTO) {
        int stock=codeActiveService.countByGameAppid(cartItemDTO.getPlatform().getId(),cartItemDTO.getGame().getAppid());
        if(stock < cartItemDTO.getQuantity()){
            cartItemDTO.setQuantity(stock);
            return true;
        }
        return false;
    }
}
