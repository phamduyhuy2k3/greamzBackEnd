package com.greamz.backend.controller;

import com.greamz.backend.dto.account.*;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Orders;
import com.greamz.backend.model.Review;

import com.greamz.backend.model.Voucher;

import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.*;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.greamz.backend.util.Mapper.mapObject;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class AccountRestController {
    private final AccountModelService service;
    private final OrderService orderService;
    private final ReviewService reviewService;
    private final EmailService emailService;

    @GetMapping("/sendEmailToRevoke/{email}/{fullname}")
    public ResponseEntity<String> sendEmailToRevoke(@PathVariable String email, @PathVariable String fullname) {
        try {
            emailService.sendEmailRevokedAccount(email, fullname);
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Send email failed");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Email not found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Send email failed");
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/toggle-enable/{enabled}")
    public ResponseEntity<Void> toggleEnable(@PathVariable Integer userId, @PathVariable boolean enabled) {
        service.updateEnabledField(userId, enabled);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/currentUser")
    public ResponseEntity<UserProfileDTO> currentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        if (currentUser == null) {
            return ResponseEntity.notFound().build();
        }
        AccountModel accountModel = service.findAccountById(currentUser.getId());
        UserProfileDTO userProfileDTO = mapObject(accountModel, UserProfileDTO.class);
        log.info("userProfileDTO: {}", userProfileDTO);
        return ResponseEntity.ok(userProfileDTO);
    }

    @PutMapping("/edit-username")
    public ResponseEntity<UserProfileDTO> editUsername(@RequestBody @Valid EditUsername accountModel) {
        return ResponseEntity.ok(service.editUsername(accountModel));
    }

    @PutMapping("/edit-fullname")
    public ResponseEntity<UserProfileDTO> editEmail(@RequestBody @Valid EditFullname accountModel) {
        return ResponseEntity.ok(service.editFullname(accountModel));
    }

    @PutMapping("/edit-photo")
    public ResponseEntity<UserProfileDTO> editPhoto(@RequestBody @Valid EditPhoto accountModel) {

        return ResponseEntity.ok(service.editPhoto(accountModel));
    }

    @PutMapping("/edit-sensitive")
    public ResponseEntity<UserProfileDTO> editProfile(@RequestBody @Valid UserProfileImportant accountModel) {
        return ResponseEntity.ok(service.userEditProfileImportant(accountModel));
    }

    @GetMapping("/findAll")
    public ResponseEntity<Iterable<AccountModel>> findAll() {
        List<AccountModel> accountModels = service.findAll();
        return ResponseEntity.ok(accountModels);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> authorities() {
        return ResponseEntity.ok(Arrays.stream(Role.values()).map(Role::name).toList());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<AccountModel> findById(@PathVariable("id") Integer id) {
        try {
            AccountModel accountModel = service.findAccountById(id);
            return ResponseEntity.ok(accountModel);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public AccountBasicDTO getOne(@PathVariable("id") Integer id) {
        return service.findById(id);
    }

    @GetMapping("/findOrdersByAccountId/{id}")
    public ResponseEntity<List<Orders>> findOrdersByAccountId(@PathVariable("id") Integer id) {
        List<Orders> ordersDTOS = orderService.findAllOrdersByAccountId(id);
        return ResponseEntity.ok(ordersDTOS);
    }

    @GetMapping("/findReiewsByAccountId/{id}")
    public ResponseEntity<List<Review>> findReiewsByAccountId(@PathVariable("id") Integer id) {
        List<Review> reviewsUserDTOS = reviewService.findAllByAccountId(id);
        return ResponseEntity.ok(reviewsUserDTOS);
    }


    @GetMapping("/findReviewByAccountId/{id}")
    public ResponseEntity<List<Review>> findReviewByAccountId(@PathVariable("id") Integer id) {
        List<Review> reviewsDTOS = reviewService.findAllReviewsByAccountId(id);
        return ResponseEntity.ok(reviewsDTOS);
    }

    @GetMapping("/findVoucherByAccountId/{id}")
    public ResponseEntity<List<Voucher>> findVoucherByAccountId(@PathVariable("id") Integer id) {
        List<Voucher> voucherDTOS = service.findAllVouchersByAccountId(id);
        return ResponseEntity.ok(voucherDTOS);
    }

    @PostMapping("/save")
    public AccountModel save(@Validated @RequestBody AccountRequest account) {
        return service.saveAccount(account);
    }

    @PutMapping("/update")
    public AccountModel update( @RequestBody AccountRequest account) {
        return service.updateAccount(account);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        service.deleteAccountById(id);
    }
}
