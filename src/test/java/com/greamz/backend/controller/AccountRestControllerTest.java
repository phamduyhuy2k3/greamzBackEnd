package com.greamz.backend.controller;

import com.greamz.backend.dto.account.AccountRequest;
import com.greamz.backend.dto.account.UserProfileDTO;
import com.greamz.backend.enumeration.Role;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.AccountModelService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountRestControllerTest {
    @Mock
    private AccountModelService accountModelService;

    @InjectMocks
    private AccountRestController accountRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCurrentUserWithValidUserPrincipal() {
        // Test for currentUser method with a valid UserPrincipal
        UserPrincipal userPrincipal = new UserPrincipal(1, "username", "password", Collections.singletonList(Role.USER));
        when(accountModelService.findAccountById(1)).thenReturn(new AccountModel(/*mock parameters*/));

        ResponseEntity<UserProfileDTO> response = accountRestController.currentUser(userPrincipal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

    @Test
    public void testCurrentUserWithNullUserPrincipal() {
        // Test for currentUser method with a null UserPrincipal
        ResponseEntity<UserProfileDTO> response = accountRestController.currentUser(null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }


    @Test
    public void testFindAll() {
        // Test for findAll method
        List<AccountModel> mockAccountModels = Arrays.asList(new AccountModel(/*mock parameters*/), new AccountModel(/*mock parameters*/));
        when(accountModelService.findAll()).thenReturn(mockAccountModels);

        ResponseEntity<Iterable<AccountModel>> response = accountRestController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

    @Test
    public void testAuthorities() {
        // Test for authorities method
        ResponseEntity<?> response = accountRestController.authorities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

    @Test
    public void testFindByIdWithValidId() {
        // Test for findById method with a valid ID
        Integer accountId = 1;
        when(accountModelService.findAccountById(accountId)).thenReturn(new AccountModel(/*mock parameters*/));

        ResponseEntity<AccountModel> response = accountRestController.findById(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

    @Test
    public void testFindByIdWithInvalidId() {
        // Test for findById method with an invalid ID
        Integer accountId = 999; // Assuming this ID does not exist
        when(accountModelService.findAccountById(accountId)).thenThrow(NoSuchElementException.class);

        ResponseEntity<AccountModel> response = accountRestController.findById(accountId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
//    @Test
//    public void testGetOne() {
//        // Test for getOne method
//        Integer accountId = 1;
//        AccountModel mockAccountModel = new AccountModel(/*mock parameters*/);
//        when(accountModelService.findById(accountId)).thenReturn(mockAccountModel);
//
//        AccountModel result = accountRestController.getOne(accountId);
//
//        assertNotNull(result);
//        // Add more assertions as needed
//    }

    @Test
    public void testSave() {
        // Test for save method
        AccountRequest accountRequest = new AccountRequest(/*mock parameters*/);
        AccountModel mockAccountModel = new AccountModel(/*mock parameters*/);
        when(accountModelService.saveAccount(accountRequest)).thenReturn(mockAccountModel);

        AccountModel result = accountRestController.save(accountRequest);

        assertNotNull(result);
        // Add more assertions as needed
    }

    @Test
    public void testUpdate() {
        // Test for update method
        AccountModel accountModelToUpdate = new AccountModel(/*mock parameters*/);

        AccountModel result = accountRestController.update(accountModelToUpdate);

        verify(accountModelService, times(1)).updateAccount(accountModelToUpdate);
        assertEquals(accountModelToUpdate, result);
        // Add more assertions as needed
    }

    @Test
    public void testDelete() {
        // Test for delete method
        Integer accountId = 1;

        assertDoesNotThrow(() -> accountRestController.delete(accountId));

        verify(accountModelService, times(1)).deleteAccountById(accountId);
    }
}
