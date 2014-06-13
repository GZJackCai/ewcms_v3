insert into acct_user(id, e_mail, login_name, real_name, pass_word, status, salt) values(1,'','admin','管理员','6e7a1b75ef4d24a32d83c62a71dfad44606c342c',FALSE,'b10b376b8efb5339');

insert into acct_role(id, role_name, caption) values(1, 'ROLE_ADMIN', '管理员');

insert into acct_permission(id, caption, expression, name) values(1, '所有权限', '*', 'ALL');

insert into acct_permission(id, caption, expression, name) values(2, '查看用户', 'user:view', 'USER_VIEW');
insert into acct_permission(id, caption, expression, name) values(3, '修改用户', 'user:edit','USER_EDIT');
insert into acct_permission(id, caption, expression, name) values(4, '删除用户', 'user:delete', 'USER_DELETE');

insert into acct_permission(id, caption, expression, name) values(5, '查看角色', 'role:view', 'ROLE_VIEW');
insert into acct_permission(id, caption, expression, name) values(6, '修改角色', 'role:edit', 'ROLE_EDIT');
insert into acct_permission(id, caption, expression, name) values(7, '删除角色', 'role:delete', 'ROLE_DELETE');

insert into acct_permission(id, caption, expression, name) values(8, '查看权限', 'permission:view','PERMISSION_VIEW');

insert into acct_permission(id, caption, expression, name) values(9, '查看站点', 'site:view','SITE_VIEW');
insert into acct_permission(id, caption, expression, name) values(10, '创建站点', 'site:add','SITE_ADD');
insert into acct_permission(id, caption, expression, name) values(11, '删除站点', 'site:delete', 'SITE_DELETE');
insert into acct_permission(id, caption, expression, name) values(12, '设为主站', 'site:set', 'SITE_SET');
insert into acct_permission(id, caption, expression, name) values(13, '设置站点属性', 'site:config', 'SITE_CONFIG');
insert into acct_permission(id, caption, expression, name) values(14, '设置发布属性', 'site:server', 'SITE_SERVER');
insert into acct_permission(id, caption, expression, name) values(15, '重命名站点', 'site:rename', 'SITE_RENAME');
insert into acct_permission(id, caption, expression, name) values(16, '剪切/粘贴站点', 'site:parse', 'SITE_PARSE');

insert into acct_permission(id, caption, expression, name) values(17, '查看组织', 'organ:view', 'ORGAN_VIEW');
insert into acct_permission(id, caption, expression, name) values(18, '添加组织', 'organ:add', 'ORGAN_ADD');
insert into acct_permission(id, caption, expression, name) values(19, '重命名组织', 'organ:rename', 'ORGAN_RENAME');
insert into acct_permission(id, caption, expression, name) values(20, '修改组织', 'organ:edit', 'ORGAN_EDIT');
insert into acct_permission(id, caption, expression, name) values(21, '删除组织', 'organ:delete', 'ORGAN_DELETE');
insert into acct_permission(id, caption, expression, name) values(22, '剪切/粘贴组织', 'organ:parse', 'ORGAN_PARSE');

insert into acct_user_role(user_id, role_id) values(1, 1);

insert into acct_user_permission(user_id, permission_id) values(1, 1);
