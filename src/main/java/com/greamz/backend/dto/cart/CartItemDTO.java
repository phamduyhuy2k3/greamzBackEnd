package com.greamz.backend.dto.cart;

import com.greamz.backend.dto.game.GameCart;
import com.greamz.backend.dto.game.GameDetailClientDTO;
import com.greamz.backend.dto.platform.PlatformBasicDTO;
import jakarta.persistence.GeneratedValue;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long id;
    private Integer accountId;
    private GameCart game;
    private PlatformBasicDTO platform;
    private Integer quantity;
    private Integer stock;
    private boolean isOutOfStock;

}

