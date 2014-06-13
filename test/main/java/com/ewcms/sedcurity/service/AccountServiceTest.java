package com.ewcms.sedcurity.service;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ewcms.security.dao.UserDao;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.security.service.ShiroDbRealm.ShiroUser;
import com.ewcms.sedcurity.ShiroTestUtils;

public class AccountServiceTest {
	@InjectMocks
	private AccountService accountService;

	@Mock
	private UserDao mockUserDao;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		ShiroTestUtils.mockSubject(new ShiroUser("foo", "Foo", new HashSet<Role>()));
	}

	@After
	public void tearDown() {
		ShiroTestUtils.clearSubject();
	}

	@Test
	public void saveUser() {
		User admin = new User();
		admin.setId(1L);

		User user = new User();
		user.setId(2L);
		user.setPlainPassword("123");

		//正常保存用户.
		accountService.saveUser(user, true);

		//保存超级管理用户抛出异常.
		try {
			accountService.saveUser(admin, false);
			fail("expected ServicExcepton should be thrown");
		} catch (ServiceException e) {
			//expected exception
		}
		Mockito.verify(mockUserDao, Mockito.never()).delete(1L);
	}
}
