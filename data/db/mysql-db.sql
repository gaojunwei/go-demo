CREATE TABLE `user` (
                        `user_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `login_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '登录用户名称',
                        `login_password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '登录密码',
                        `gender` int DEFAULT NULL COMMENT '性别',
                        `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '手机号',
                        `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '地址',
                        `organization_id` int DEFAULT NULL COMMENT '部门-组织ID',
                        `state` tinyint unsigned DEFAULT '1' COMMENT '用户状态（0停用，1正常）',
                        `email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '邮箱地址',
                        `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT '' COMMENT '注释 ',
                        PRIMARY KEY (`user_id`) USING BTREE,
                        UNIQUE KEY `uniq_login_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=COMPACT;