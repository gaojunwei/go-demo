DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
	user_id BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
	user_name varchar(100) NOT NULL COMMENT '用户名称',
	user_age INT DEFAULT -1 NULL COMMENT '用户年龄',
	from_source varchar(20) NULL COMMENT '用户来源',
	CONSTRAINT user_info_pk PRIMARY KEY (user_id)
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息';