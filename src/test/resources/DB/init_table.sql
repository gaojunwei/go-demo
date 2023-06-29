DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
    `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
    `user_name` varchar(100) NOT NULL COMMENT '用户名称',
    `user_age` int unsigned DEFAULT NULL COMMENT '用户年龄',
    `from_source` varchar(20) DEFAULT NULL COMMENT '用户来源',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息'