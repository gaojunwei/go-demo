/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : 127.0.0.1:3306
 Source Schema         : my_flow

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 28/08/2024 10:03:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fw_his_instance
-- ----------------------------
DROP TABLE IF EXISTS `fw_his_instance`;
CREATE TABLE `fw_his_instance`  (
  `instance_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '流程实例ID',
  `instance_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程实例编号',
  `create_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人ID',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `start_time` timestamp NOT NULL COMMENT '创建时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `duration` bigint UNSIGNED NULL DEFAULT NULL COMMENT '处理耗时',
  `process_id` bigint NOT NULL COMMENT '流程定义ID',
  `process_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程定义 key 唯一标识',
  `parent_instance_no` bigint UNSIGNED NULL DEFAULT NULL COMMENT '父流程实例ID',
  `business_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '业务KEY',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '上次更新时间',
  `instance_state` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 0:审批中,1:审批完成,2:终止',
  `delete_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '删除原因',
  PRIMARY KEY (`instance_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '历史流程实例表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fw_his_task
-- ----------------------------
DROP TABLE IF EXISTS `fw_his_task`;
CREATE TABLE `fw_his_task`  (
  `task_id` bigint UNSIGNED NOT NULL COMMENT '任务ID',
  `start_time` timestamp NOT NULL COMMENT '创建时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `duration` bigint UNSIGNED NULL DEFAULT 0 COMMENT '耗时',
  `instance_id` bigint NOT NULL COMMENT '流程实例ID',
  `instance_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程实例编号',
  `process_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程定义KEY',
  `node_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务 key唯一标识',
  `node_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `assignee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '受让人',
  `form_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表单键',
  `delete_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '删除原因',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '历史任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fw_his_variable
-- ----------------------------
DROP TABLE IF EXISTS `fw_his_variable`;
CREATE TABLE `fw_his_variable`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务唯一ID',
  `task_id` int UNSIGNED NOT NULL DEFAULT 0,
  `var_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变量名称',
  `var_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变量值',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_instance_task_key`(`instance_no` ASC, `task_id` ASC, `var_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '历史变量表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fw_instance_ext
-- ----------------------------
DROP TABLE IF EXISTS `fw_instance_ext`;
CREATE TABLE `fw_instance_ext`  (
  `instance_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '流程实例ID',
  `instance_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程实例编号',
  `process_id` bigint NOT NULL COMMENT '流程定义ID',
  `process_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程定义 key 唯一标识',
  `model_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程模型定义JSON内容',
  PRIMARY KEY (`instance_id`) USING BTREE,
  UNIQUE INDEX `unique_instance_no`(`instance_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '扩展流程实例表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fw_process
-- ----------------------------
DROP TABLE IF EXISTS `fw_process`;
CREATE TABLE `fw_process`  (
  `process_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程定义 key 唯一标识',
  `process_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程定义名称',
  `version` int NOT NULL DEFAULT 1 COMMENT '流程版本，默认 1',
  `process_state` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '流程状态 0，不可用 1，可用',
  `model_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程模型定义JSON内容',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`process_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流程定义表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fw_ru_task
-- ----------------------------
DROP TABLE IF EXISTS `fw_ru_task`;
CREATE TABLE `fw_ru_task`  (
  `task_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `instance_id` bigint NOT NULL COMMENT '流程实例ID',
  `instance_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '流程实例编号',
  `node_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点名称',
  `node_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '节点key唯一标识',
  `assignee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '受让人',
  `candidates` json NULL COMMENT '候选人集合',
  `form_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表单键',
  `process_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程定义KEY',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for fw_ru_variable
-- ----------------------------
DROP TABLE IF EXISTS `fw_ru_variable`;
CREATE TABLE `fw_ru_variable`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务唯一ID',
  `task_id` bigint UNSIGNED NULL DEFAULT 0,
  `var_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变量名称',
  `var_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '变量值',
  `create_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uq_instance_task_key`(`instance_no` ASC, `task_id` ASC, `var_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '实例运行时变量' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
