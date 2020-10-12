package com.appsdeveloperblog.app.ws.ui.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.MockitoAnnotations;

import com.appsdeveloperblog.app.ws.service.impl.UserServiceImpl;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserControllerTest {

	@InjectMocks
	UserController userController;

	@Mock
	UserServiceImpl userService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		UserDto userDto = new UserDto();
		userDto.setFirstName("Dattu");
		userDto.setLastName("Chillal");
		userDto.setEmail("chillalkar@gmail.com");
		userDto.setEmailVerificationStatus(Boolean.FALSE);
		userDto.setEmailVerificationToken(null);
		userDto.setUserId("DFSRFSHD12637213gggg");
		userDto.setEncryptedPassword("jagfjahfgashfka");
		userDto.setAddresses(getAddressesDto());
	}

	@Test
	void testGetUsers() {
		when(userService.getUserByUserId(anyString())).thenReturn(null);
	}

	private List<AddressDto> getAddressesDto() {
		AddressDto shippingAddressDto = new AddressDto();
		shippingAddressDto.setCity("Bangalore");
		shippingAddressDto.setCountry("India");
		shippingAddressDto.setStreetName("KR PURAM");
		shippingAddressDto.setPostalCode("876543");
		shippingAddressDto.setType("shipping");

		AddressDto billingAddressDto = new AddressDto();
		billingAddressDto.setCity("Mumbai");
		billingAddressDto.setCountry("India");
		billingAddressDto.setStreetName("Shivaji");
		billingAddressDto.setPostalCode("765433");
		billingAddressDto.setType("billing");

		List<AddressDto> addresses = new ArrayList<>();
		addresses.add(shippingAddressDto);
		addresses.add(billingAddressDto);

		return addresses;
	}

}
