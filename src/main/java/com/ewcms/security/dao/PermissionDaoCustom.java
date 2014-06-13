package com.ewcms.security.dao;

import com.ewcms.security.model.Permission;

public interface PermissionDaoCustom {
	Permission findSelectedPermission(Long roleId, Long permissionId);
}
