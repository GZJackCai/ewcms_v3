package com.ewcms.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.security.model.User;

public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
	
	User findByLoginName(String loginName);
	
	User findByLoginNameAndPassword(String loginName, String password);

	@Modifying
	@Query("update User u set u.password=?2 where u.loginName=?1")
	void updatePassword(String loginName, String password);
}
