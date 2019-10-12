create database `myblog` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use `myblog`;

SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- 用户
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `uid`         BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `phone`       varchar(64)           DEFAULT NULL COMMENT '手机号',
    `username`    varchar(32)  NOT NULL COMMENT '用户名',
    `password`    varchar(64)  NOT NULL COMMENT '密码',
    `sex`         varchar(32)  NOT NULL COMMENT '性别 male or female',
    `email`       varchar(100)          DEFAULT NULL COMMENT '邮箱地址',
    `avatar_img`  varchar(255) NOT NULL COMMENT '头像地址',
    `state`       tinyint(1)   NOT NULL default 1 COMMENT '状态 0删除',
    `create_time` DATETIME     NOT NULL COMMENT '创建时间',
    `logged`      DATETIME              DEFAULT NULL COMMENT '上次登录时间',
    PRIMARY KEY (`uid`),
    UNIQUE KEY `name` (`username`),
    UNIQUE KEY `phone` (`phone`),
    UNIQUE KEY `mail` (`email`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` (`uid`, `phone`, `username`, `password`, `sex`, `email`, `avatar_img`,
                    `create_time`, `logged`)
VALUES ('16', NULL, 'admin', '74A11FB87D6A947022D1658D06E0D7AB', 'male', NULL,
        'https://avatars2.githubusercontent.com/u/20983152?s=460&v=4', '2019-10-04 05:11:31', NULL);
INSERT INTO `user` (`uid`, `phone`, `username`, `password`, `sex`, `email`, `avatar_img`,
                    `create_time`, `logged`)
VALUES ('17', NULL, 'user', '74A11FB87D6A947022D1658D06E0D7AB', 'male', NULL,
        'http://blog.wiblog.cn/img/reply-avatar.svg', '2019-10-05 07:41:30', NULL);
INSERT INTO `user` (`uid`, `phone`, `username`, `password`, `sex`, `email`, `avatar_img`,
                    `create_time`, `logged`)
VALUES ('18', NULL, 'user2', '74A11FB87D6A947022D1658D06E0D7AB', 'male', NULL,
        'http://blog.wiblog.cn/img/reply-avatar.svg', '2019-10-05 07:57:55', NULL);


-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`   BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL COMMENT '角色名',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role`
VALUES ('1', '超级管理员');
INSERT INTO `role`
VALUES ('2', '管理员');
INSERT INTO `role`
VALUES ('3', '用户');


-- ----------------------------
-- 用户角色关系表
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id`      BIGINT(11) NOT NULL AUTO_INCREMENT,
    `uid`     BIGINT(11) NOT NULL COMMENT '用户id',
    `role_id` BIGINT(11) NOT NULL COMMENT '角色id',
    PRIMARY KEY (`id`),
    UNIQUE key `uid` (`uid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role`
VALUES ('1', '16', '1');



-- ----------------------------
-- 文章
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`
(
    `id`              bigint(11)   NOT NULL AUTO_INCREMENT,
    `author`          varchar(255) NOT NULL COMMENT '作者名',
    `title`           varchar(255) NOT NULL COMMENT '标题',
    `content`         longtext     NOT NULL COMMENT '内容',
    `tags`            varchar(255) NOT NULL COMMENT '标签',
    `category_id`     bigint(11)   NOT NULL COMMENT '分类id',
    `img_url`         varchar(255) NOT NULL COMMENT '文章封面',
    `article_url`     varchar(255) NOT NULL COMMENT '文章地址',
    `article_summary` text         NOT NULL COMMENT '简介',
    `hits`            int(11)      NOT NULL COMMENT '点击量',
    `privately`       tinyint(1)   NOT NULL default 0 COMMENT '是否设为私密 1私密',
    `reward`          tinyint(1)   NOT NULL default 0 COMMENT '是否开放打赏 1开启',
    `comment`         tinyint(1)   NOT NULL default 0 COMMENT '是否开放评论 1开放',
    `create_time`     DATETIME     NOT NULL,
    `update_time`     DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- 评论
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
    `uid`         bigint(11) NOT NULL COMMENT '用户id',
    `article_id`  bigint(11) NOT NULL COMMENT '文章id',
    `parent_id`   bigint(11) NOT NULL COMMENT '父评论id 0为评论文章',
    `gen_id`      bigint(11) NOT NULL COMMENT '主评论id 0为评论文章',
    `likes`       int(11)    NOT NULL COMMENT '点赞数量',
    `floor`       int(5) COMMENT '楼层',
    `content`     text       NOT NULL COMMENT '评论内容',
    `state`       tinyint(1) NOT NULL default 1 COMMENT '状态 0删除',
    `create_time` DATETIME   NOT NULL,
    `update_time` DATETIME   NOT NULL,
    PRIMARY KEY (`id`),
    INDEX article (article_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`        bigint(11)   NOT NULL AUTO_INCREMENT COMMENT '分类id',
    `parent_id` bigint(11)   NOT NULL COMMENT '上级分类id 0为最上级',
    `name`      varchar(255) NOT NULL COMMENT '分类名',
    `url`       varchar(255) NOT NULL COMMENT '链接地址',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of categories
-- ----------------------------
INSERT INTO `category`
VALUES ('1', '0', 'java', 'java');


-- ----------------------------
-- Table structure for article_likes_record
-- ----------------------------
DROP TABLE IF EXISTS `article_likes`;
CREATE TABLE `article_likes`
(
    `id`          bigint(11)      NOT NULL AUTO_INCREMENT,
    `article_id`  bigint(11)   NOT NULL COMMENT '文章id',
    `uid`    bigint(11)      NOT NULL COMMENT '用户id',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX article (article_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 29
  DEFAULT CHARSET = utf8;



-- ---------------------------------------------------------------------------
-- ----------------------------
-- Table structure for archives
-- ----------------------------
DROP TABLE IF EXISTS `archives`;
CREATE TABLE `archives`
(
    `id`          int(11)      NOT NULL AUTO_INCREMENT,
    `archiveName` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of archives
-- ----------------------------
INSERT INTO `archives`
VALUES ('1', '2018年07月');
INSERT INTO `archives`
VALUES ('2', '2018年08月');



-- ----------------------------
-- Table structure for comment_likes_record
-- ----------------------------
DROP TABLE IF EXISTS `comment_likes_record`;
CREATE TABLE `comment_likes_record`
(
    `id`         int(11)      NOT NULL AUTO_INCREMENT,
    `article_id` bigint(20)   NOT NULL,
    `p_id`       int(11)      NOT NULL,
    `liker_id`   int(11)      NOT NULL,
    `likeDate`   varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of comment_likes_record
-- ----------------------------


-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`
(
    `id`              int(11)      NOT NULL AUTO_INCREMENT,
    `feedbackContent` text         NOT NULL,
    `contactInfo`     varchar(255) DEFAULT NULL,
    `personId`        int(11)      NOT NULL,
    `feedbackDate`    varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of feedback
-- ----------------------------

-- ----------------------------
-- Table structure for leave_message_likes_record
-- ----------------------------
DROP TABLE IF EXISTS `leave_message_likes_record`;
CREATE TABLE `leave_message_likes_record`
(
    `id`       int(11)      NOT NULL AUTO_INCREMENT,
    `pageName` varchar(255) NOT NULL,
    `pId`      int(11)      NOT NULL,
    `likerId`  int(11)      NOT NULL,
    `likeDate` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 11
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of leave_message_likes_record
-- ----------------------------

-- ----------------------------
-- Table structure for leave_message_record
-- ----------------------------
DROP TABLE IF EXISTS `leave_message_record`;
CREATE TABLE `leave_message_record`
(
    `id`                  int(11)      NOT NULL AUTO_INCREMENT,
    `pageName`            varchar(255) NOT NULL,
    `pId`                 int(255)     NOT NULL,
    `answererId`          int(11)      NOT NULL,
    `respondentId`        int(11)      NOT NULL,
    `leaveMessageDate`    varchar(255) NOT NULL,
    `likes`               int(11)      NOT NULL,
    `leaveMessageContent` text         NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of leave_message_record
-- ----------------------------
INSERT INTO `leave_message_record`
VALUES ('14', 'categories', '0', '1', '1', '2018-09-19 13:53', '0', '分类留言测试');
INSERT INTO `leave_message_record`
VALUES ('15', 'archives', '0', '1', '1', '2018-09-19 13:53', '0', '归档留言');
INSERT INTO `leave_message_record`
VALUES ('16', 'tags', '0', '1', '1', '2018-09-19 13:53', '0', '标签留言');
INSERT INTO `leave_message_record`
VALUES ('17', 'update', '0', '1', '1', '2018-09-19 13:53', '0', '更新留言');
INSERT INTO `leave_message_record`
VALUES ('18', 'friendlylink', '0', '1', '1', '2018-09-19 13:54', '0',
        '需要添加友链的朋友可在www.zhyocean.cn/friendlylink下方留言（网站名称+网址），随后验证后会在本人博客中添加友链链接');

-- ----------------------------
-- Table structure for privateword
-- ----------------------------
DROP TABLE IF EXISTS `privateword`;
CREATE TABLE `privateword`
(
    `id`            int(11)      NOT NULL AUTO_INCREMENT,
    `privateWord`   varchar(255) NOT NULL,
    `publisherId`   varchar(255) NOT NULL,
    `replierId`     varchar(255) DEFAULT NULL,
    `replyContent`  varchar(255) DEFAULT NULL,
    `publisherDate` varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of privateword
-- ----------------------------
INSERT INTO `privateword`
VALUES ('8', '悄悄话测试', '1', '0', null, '2018-09-19 14:13:32');



-- ----------------------------
-- Table structure for visitor
-- ----------------------------
DROP TABLE IF EXISTS `visitor`;
CREATE TABLE `visitor`
(
    `id`         int(11)    NOT NULL AUTO_INCREMENT,
    `visitorNum` bigint(20) NOT NULL,
    `pageName`   text       NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 22
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of visitor
-- ----------------------------
INSERT INTO `visitor`
VALUES ('1', '3228', 'totalVisitor');
INSERT INTO `visitor`
VALUES ('2', '1032', 'visitorVolume');
INSERT INTO `visitor`
VALUES ('3', '42', 'article/1532884460');
INSERT INTO `visitor`
VALUES ('5', '57', 'article/1533196734');

-- ----------------------------
-- tags
-- ----------------------------
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags`
(
    `id`      int(11)      NOT NULL AUTO_INCREMENT,
    `tagName` varchar(255) NOT NULL,
    `tagSize` int(11)      NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 19
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Records of tags
-- ----------------------------
INSERT INTO `tags`
VALUES ('1', '随笔感悟', '15');
INSERT INTO `tags`
VALUES ('4', 'SpringBoot', '17');
INSERT INTO `tags`
VALUES ('5', '个人博客', '18');
INSERT INTO `tags`
VALUES ('18', '原创', '20');