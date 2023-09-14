drop table if exists `test`;
CREATE TABLE `test` (
    `id` BIGINT NOT NULL COMMENT 'id',
    `name` VARCHAR(50) COMMENT '名称',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='测试';
