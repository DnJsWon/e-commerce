package com.dsapkl.backend.service;

import com.dsapkl.backend.entity.Cart;
import com.dsapkl.backend.entity.CartItem;
import com.dsapkl.backend.entity.Item;
import com.dsapkl.backend.entity.Member;
import com.dsapkl.backend.repository.CartItemRepository;
import com.dsapkl.backend.repository.CartRepository;
import com.dsapkl.backend.repository.ItemRepository;
import com.dsapkl.backend.repository.MemberRepository;
import com.dsapkl.backend.repository.query.CartQueryDto;
import com.dsapkl.backend.repository.query.CartQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final CartQueryRepository cartQueryRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public Cart findCart(Long memberId) {
        return cartRepository.findByMemberId(memberId).orElse(null);
    }

    @Transactional(readOnly = true)
    public CartItem findCartItem(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElse(null);
    }

    /**
     * 장바구니 조회
     */
    @Transactional(readOnly = true)
    public List<CartQueryDto> findCartItems(Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(EntityNotFoundException::new);  // () -> new EntityNotFoundException()
        List<CartQueryDto> cartQueryDtos = cartQueryRepository.findCartQueryDtos(cart.getId());
        return cartQueryDtos;
    }


    /**
     * 장바구니 담기(추가)
     */
    public Long addCart(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findById(memberId).get();
        Cart cart = cartRepository.findByMemberId(memberId).orElseGet(() -> null);
        Item item = itemRepository.findById(itemId).get();

        //장바구니 없으면 생성  --> 회원가입 할 때 장바구니 생성되어야 함 장바구니 눌렀을 때 생기는게 아니라
//        if (cart == null) {
//            log.info("장바구니 신규 생성 - memberId={}", memberId);
//            cart = Cart.createCart(member);
//            cartRepository.save(cart);
//        }

        //장바구니안에 장바구니 상품 조회
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId()).orElse(null);
//        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId())
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "CartItem not found for cartId: " + cart.getId() + " and itemId: " + item.getId()));

        //장바구니 상품이 없으면 생성
        if (cartItem == null) {
            cartItem = CartItem.createCartItem(count, cart, item);
            CartItem savedCartItem = cartItemRepository.save(cartItem);
            log.info("cartItemId={}", cartItem.getId());
            return savedCartItem.getId();
        }

        //장바구니 상품이 존재하면 수량 변경 (Dirty checking)
        cartItem.changeCount(count);
        return cartItem.getId();

    }

    /**
     * 장바구니 삭제
     */
    public void deleteCartItem(Long itemId) {
        CartItem findCartItem = cartItemRepository.findById(itemId).orElse(null);
        if (findCartItem == null) {
            throw new IllegalArgumentException("Cart item not found with id: " + itemId);
        }
        cartItemRepository.delete(findCartItem);
    }

}
