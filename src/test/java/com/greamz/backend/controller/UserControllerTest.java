package com.greamz.backend.controller;

import com.greamz.backend.dto.account.UserProfileDTO;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetProfile() {
        String username = "testUser";
        AccountModel accountModel = new AccountModel();
        accountModel.setUsername(username);

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        // Thiết lập thông tin UserProfileDTO cho username testUser
        userProfileDTO.setFullname("Test User");

        when(userService.getProfile(username)).thenReturn(userProfileDTO);

        ResponseEntity<UserProfileDTO> responseEntity = userController.getProfile(accountModel);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userProfileDTO, responseEntity.getBody());
        verify(userService, times(1)).getProfile(username);
    }
}
