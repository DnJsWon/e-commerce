package com.dsapkl.backend.repository.query;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CartQueryDto {
    private Long cartItemId;
    private Long itemId;
    private String itemName;  //상품명
    private int itemStockQuantity; //상품 수량
    private int count;  //주문 수량
    private int price; //상품 가격
    private String imgUrl; // 대표 상품 이미지 경로
//    private Long itemId;

    public CartQueryDto(Long cartItemId, String itemName, int itemStockQuantity,  int count, int price, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.itemStockQuantity = itemStockQuantity;
        this.count = count;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public CartQueryDto(Long cartItemId, Long itemId, String itemName, int itemStockQuantity,  int count, int price, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemStockQuantity = itemStockQuantity;
        this.count = count;
        this.price = price;
        this.imgUrl = imgUrl;
    }
}
