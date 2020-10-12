package com.appsdeveloperblog.app.ws.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.shared.AmazonSES;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	AmazonSES amazonSES;

	String userId = "jjafdsjfasjadhvsajvd";
	String encryptedPassword = "2344sdfgsgs";
	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("Dattu");
		userEntity.setUserId(userId);
		userEntity.setLastName("Chillal");
		userEntity.setEmail("test@gmail.com");
		userEntity.setEmailVerificationToken("77jehgrewhjr4324532");
		userEntity.setAddresses(getAddressesEntity());
	}

	@Test
	final void testGetUser() {
		userEntity.setEncryptedPassword(encryptedPassword);
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
		UserDto userDto = userService.getUser("test@gmail.com");
		assertNotNull(userDto);
		assertEquals("Dattu", userDto.getFirstName());
	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class, ()-> {
			userService.getUser("test@gmail.com");
		});
	}

	@Test
	final void testCreateUser_CreateUserServiceException() {
		UserDto userDto = new UserDto();
		userDto.setFirstName("Dattu");
		userDto.setLastName("Chillal");
		userDto.setEmail("chillalkar@gmail.com");
		userDto.setPassword("123");
		userDto.setAddresses(getAddressesDto());
		when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

		assertThrows(UserServiceException.class, ()-> {
			userService.createUser(userDto);
		});
	}

	@Test
	final void testCreateUser() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		when(utils.generateAddressId(anyInt())).thenReturn("ggjadskj2313");
		when(utils.generateUserId(anyInt())).thenReturn(userId);

		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
		Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

		UserDto userDto = new UserDto();
		userDto.setFirstName("Dattu");
		userDto.setLastName("Chillal");
		userDto.setEmail("chillalkar@gmail.com");
		userDto.setPassword("123");
		userDto.setAddresses(getAddressesDto());

		UserDto userDetails = userService.createUser(userDto);
		assertNotNull(userDetails);
		assertEquals(userEntity.getFirstName(), userDetails.getFirstName());
		assertEquals(userEntity.getLastName(), userDetails.getLastName());
		assertNotNull(userDetails.getUserId());
		assertEquals(userDetails.getAddresses().size(), userEntity.getAddresses().size());
		verify(utils, times(userDetails.getAddresses().size())).generateAddressId(30);
		verify(bCryptPasswordEncoder, times(1)).encode("123");
		verify(userRepository, times(1)).save(any(UserEntity.class));
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

	private List<AddressEntity> getAddressesEntity() {
		List<AddressDto> addresses = getAddressesDto();
		Type listType = new TypeToken<List<AddressEntity>>() {}.getType();
		return new ModelMapper().map(addresses, listType);
	}

}
