package com.ceos22.springcgv.service;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.purchase.Purchase;
import com.ceos22.springcgv.domain.purchase.PurchaseDetail;
import com.ceos22.springcgv.domain.snack.Inventory;
import com.ceos22.springcgv.domain.snack.SnackItem;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.dto.purchase.PurchaseItemDto;
import com.ceos22.springcgv.dto.purchase.PurchaseRequestDto;
import com.ceos22.springcgv.repository.cinema.CinemaRepository;
import com.ceos22.springcgv.repository.purchase.PurchaseDetailRepository;
import com.ceos22.springcgv.repository.purchase.PurchaseRepository;
import com.ceos22.springcgv.repository.snack.InventoryRepository;
import com.ceos22.springcgv.repository.snack.SnackItemRepository;
import com.ceos22.springcgv.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final UserRepository userRepository;
    private final CinemaRepository cinemaRepository;
    private final SnackItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public Long createPurchase(Long userId, PurchaseRequestDto requestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        Cinema cinema = cinemaRepository.findById(requestDto.getCinemaId()).orElseThrow(() -> new IllegalArgumentException("영화관 없음"));

        // 재고 확인 및 차감, 총 가격 계산
        BigDecimal totalPrice = new BigDecimal(0);
        for (PurchaseItemDto itemDto : requestDto.getItems()) {
            SnackItem item = itemRepository.findById(itemDto.getItemId()).orElseThrow(() -> new IllegalArgumentException("상품 없음"));
            Inventory inventory = inventoryRepository.findByCinemaAndItem(cinema, item)
                    .orElseThrow(() -> new IllegalArgumentException("재고 정보 없음"));

            // 재고 확인
            if (inventory.getQuantity() < itemDto.getQuantity()) {
                throw new IllegalStateException(item.getName() + " 상품의 재고가 부족합니다.");
            }
            // 재고 차감
            inventory.decreaseQuantity(itemDto.getQuantity());

            // 총 가격 계산
            totalPrice = totalPrice.add(item.getPrice().multiply(new BigDecimal(itemDto.getQuantity())));
        }

        // 구매 정보 저장
        Purchase purchase = Purchase.builder()
                .user(user)
                .cinema(cinema)
                .totalPrice(totalPrice)
                .build();
        purchaseRepository.save(purchase);

        // 구매 상세 정보 저장
        for (PurchaseItemDto itemDto : requestDto.getItems()) {
            SnackItem item = itemRepository.findById(itemDto.getItemId()).get();
            PurchaseDetail detail = PurchaseDetail.builder()
                    .purchase(purchase)
                    .item(item)
                    .quantity(itemDto.getQuantity())
                    .build();
            purchaseDetailRepository.save(detail);
        }

        return purchase.getId();
    }
}