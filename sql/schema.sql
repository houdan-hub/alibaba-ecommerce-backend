-- ============================================================
-- E-commerce Backend — Database Schema
-- Migrated from Oracle to MySQL (Alibaba internship, Jul 2024)
-- ============================================================

CREATE DATABASE IF NOT EXISTS ecommerce DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ecommerce;

-- -----------------------------------------------------------
-- 1. User table  (用户表)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'primary key',
    `user_id`       VARCHAR(64)  NOT NULL COMMENT 'business user ID',
    `email`         VARCHAR(20)  DEFAULT NULL COMMENT 'login email',
    `password`      VARCHAR(128) DEFAULT NULL COMMENT 'encrypted password',
    `nick_name`     VARCHAR(128) DEFAULT NULL COMMENT 'display name',
    `gender`        VARCHAR(5)   DEFAULT NULL COMMENT 'M / F / U',
    `avatar_url`    VARCHAR(255) DEFAULT NULL COMMENT 'profile picture URL',
    `balance`       DECIMAL(7,2) DEFAULT 0.00 COMMENT 'wallet balance',
    `paykey`        VARCHAR(64)  DEFAULT NULL COMMENT 'payment password hash',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='user table';

-- -----------------------------------------------------------
-- 2. Address table  (收货地址表)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT,
    `address_id`         VARCHAR(64)  NOT NULL COMMENT 'business address ID',
    `user_id`            VARCHAR(64)  NOT NULL COMMENT 'owner user_id',
    `name`               VARCHAR(20)  NOT NULL COMMENT 'recipient name',
    `phone`              VARCHAR(20)  NOT NULL COMMENT 'recipient phone',
    `detailed_address`   VARCHAR(255) NOT NULL COMMENT 'street-level detail',
    `state`              VARCHAR(2)   DEFAULT '1' COMMENT '1=default, 0=normal',
    `province_id`        VARCHAR(20)  DEFAULT NULL,
    `city_id`            VARCHAR(20)  DEFAULT NULL,
    `area_id`            VARCHAR(20)  DEFAULT NULL,
    `completed_address`  VARCHAR(255) DEFAULT NULL COMMENT 'full concatenated address',
    `create_time`        DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time`        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_address_id` (`address_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='shipping address table';

-- -----------------------------------------------------------
-- 3. Goods table  (商品表)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT,
    `goods_id`        VARCHAR(64)  NOT NULL COMMENT 'business goods ID',
    `goods_name`      VARCHAR(64)  NOT NULL COMMENT 'product name',
    `original_price`  DECIMAL(7,2) DEFAULT 0.00,
    `discount_price`  DECIMAL(7,2) DEFAULT 0.00,
    `master_img`      VARCHAR(255) DEFAULT NULL,
    `intro`           VARCHAR(512) DEFAULT NULL,
    `address`         VARCHAR(64)  DEFAULT NULL COMMENT 'ship-from location',
    `begin_time`      DATETIME     DEFAULT NULL COMMENT 'sale start',
    `end_time`        DATETIME     DEFAULT NULL COMMENT 'sale end',
    `postage`         DECIMAL(7,0) DEFAULT 0 COMMENT 'shipping fee',
    `inventory`       INT          DEFAULT 0 COMMENT 'stock count',
    `sale_volume`     INT          DEFAULT 0 COMMENT 'units sold',
    `video_url`       VARCHAR(255) DEFAULT NULL,
    `version`         INT          DEFAULT 0 COMMENT 'optimistic-lock version',
    `create_time`     DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time`     TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_goods_id` (`goods_id`),
    KEY `idx_name` (`goods_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='goods / product table';

-- -----------------------------------------------------------
-- 4. Order table  (订单表)
-- -----------------------------------------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `order_id`      VARCHAR(64)  NOT NULL COMMENT 'business order ID',
    `user_id`       VARCHAR(64)  NOT NULL,
    `goods_id`      VARCHAR(64)  NOT NULL,
    `purchase_num`  DECIMAL(3,0) DEFAULT 1 COMMENT 'quantity',
    `address_id`    VARCHAR(64)  NOT NULL,
    `order_state`   VARCHAR(20)  NOT NULL DEFAULT 'PENDING_PAY' COMMENT 'PENDING_PAY / PAID / SHIPPED / COMPLETED / CANCELLED',
    `paykey`        VARCHAR(20)  DEFAULT NULL,
    `total_money`   DECIMAL(7,0) DEFAULT 0,
    `cancel_time`   DATETIME     DEFAULT NULL,
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    `update_time`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_state` (`order_state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='order table';
