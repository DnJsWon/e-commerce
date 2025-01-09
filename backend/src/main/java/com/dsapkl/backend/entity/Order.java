package com.dsapkl.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    private Order(OrderStatus status, Member member) {
        this.status = status;
        this.member = member;
        this.orderDate = LocalDateTime.now();
    }

    //==연관 관계 메서드==//
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.changeOrder(this);
    }

    public static Order createOrder(OrderStatus status, Member member, List<OrderItem> orderItems) {  //List<OrderItem> list?? OrderItem... orderItems
        Order order = new Order(status, member);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        //총 주문 가격 추가
        order.totalPrice = order.getTotalPrice();

        return order;
    }


    //== 전체 주문 가격 조회 ==/
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }

    //주문 취소
    public void cancelOrder() {
        this.status = OrderStatus.CANCEL;

        //상품 재고 수량 복귀
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }


}
