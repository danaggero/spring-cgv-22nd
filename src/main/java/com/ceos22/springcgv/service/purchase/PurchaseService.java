package com.ceos22.springcgv.service.purchase;

import com.ceos22.springcgv.domain.cinema.Cinema;
import com.ceos22.springcgv.domain.purchase.Purchase;
import com.ceos22.springcgv.domain.purchase.PurchaseDetail;
import com.ceos22.springcgv.domain.snack.Inventory;
import com.ceos22.springcgv.domain.snack.SnackItem;
import com.ceos22.springcgv.domain.user.User;
import com.ceos22.springcgv.dto.purchase.PurchaseItemDto;
import com.ceos22.springcgv.dto.purchase.PurchaseRequestDto;
import com.ceos22.springcgv.global.exception.CustomException;
import com.ceos22.springcgv.global.exception.ErrorCode;
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

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Cinema cinema = cinemaRepository.findById(requestDto.getCinemaId())
                .orElseThrow(() -> new CustomException(ErrorCode.CINEMA_NOT_FOUND));

        // 재고 확인 및 차감, 총 가격 계산
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (PurchaseItemDto itemDto : requestDto.getItems()) {
            SnackItem item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SNACK_NOT_FOUND));

            Inventory inventory = inventoryRepository.findByCinemaAndItem(cinema, item)
                    .orElseThrow(() -> new CustomException(ErrorCode.INVENTORY_NOT_FOUND));

            // 재고 확인
            if (inventory.getQuantity() < itemDto.getQuantity()) {
                throw new CustomException(ErrorCode.SNACK_NO_STOCK);
            }
            // 재고 차감
            inventory.decreaseQuantity(itemDto.getQuantity());

            // 총 가격 계산
            BigDecimal itemTotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);
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
            SnackItem item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new CustomException(ErrorCode.SNACK_NOT_FOUND));

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