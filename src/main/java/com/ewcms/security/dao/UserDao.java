package com.ewcms.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.model.User;

public interface UserDao extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User>, UserDaoCustom {
	User findByLoginName(String loginName);
	User findByLoginNameAndPassword(String loginName, String password);
}
