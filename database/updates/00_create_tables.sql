# ************************************************************
# Host: 127.0.0.1 (MySQL 5.6.25)
# Database: lionboard
# Generation Time: 2016-01-17 12:52:36 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table ACT_CMT_FIGURE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_CMT_FIGURE_TB`;

CREATE TABLE `ACT_CMT_FIGURE_TB` (
  `cmtId` int(10) unsigned NOT NULL,
  `likeCount` int(11) NOT NULL DEFAULT '0',
  `unlikeCount` int(11) NOT NULL DEFAULT '0',
  KEY `cmtId` (`cmtId`),
  CONSTRAINT `act_cmt_figure_tb_ibfk_1` FOREIGN KEY (`cmtId`) REFERENCES `ACT_CMT_TB` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_CMT_REPORT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_CMT_REPORT_TB`;

CREATE TABLE `ACT_CMT_REPORT_TB` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cmtId` int(10) unsigned NOT NULL,
  `reporterId` int(10) unsigned NOT NULL,
  `reason` varchar(255) NOT NULL DEFAULT '',
  `reportedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `processCode` int(10) unsigned DEFAULT '4',
  PRIMARY KEY (`id`),
  KEY `cmtId` (`cmtId`),
  CONSTRAINT `act_cmt_report_tb_ibfk_1` FOREIGN KEY (`cmtId`) REFERENCES `ACT_CMT_TB` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_CMT_STATE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_CMT_STATE_TB`;

CREATE TABLE `ACT_CMT_STATE_TB` (
  `cmtId` int(10) unsigned NOT NULL,
  `stateCode` int(10) unsigned NOT NULL,
  `pastDays` int(10) unsigned NOT NULL,
  `powerCode` int(10) unsigned NOT NULL,
  KEY `cmtId` (`cmtId`),
  KEY `stateCode` (`stateCode`),
  CONSTRAINT `act_cmt_state_tb_ibfk_1` FOREIGN KEY (`cmtId`) REFERENCES `ACT_CMT_TB` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `act_cmt_state_tb_ibfk_2` FOREIGN KEY (`stateCode`) REFERENCES `MAP_STATE_TB` (`stateCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_CMT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_CMT_TB`;

CREATE TABLE `ACT_CMT_TB` (
  `cmtId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postId` int(10) unsigned NOT NULL,
  `userId` int(10) unsigned NOT NULL,
  `cmtNum` int(10) unsigned NOT NULL,
  `depth` int(11) NOT NULL DEFAULT '0',
  `contents` text,
  `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cmtId`),
  KEY `postId` (`postId`),
  CONSTRAINT `act_cmt_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `ACT_POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_FIXED_CMT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_FIXED_CMT_TB`;

CREATE TABLE `ACT_FIXED_CMT_TB` (
  `cmtId` int(10) unsigned NOT NULL,
  `postId` int(10) unsigned NOT NULL,
  KEY `cmtId` (`cmtId`),
  KEY `postId` (`postId`),
  CONSTRAINT `act_fixed_cmt_tb_ibfk_1` FOREIGN KEY (`cmtId`) REFERENCES `ACT_CMT_TB` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `act_fixed_cmt_tb_ibfk_2` FOREIGN KEY (`postId`) REFERENCES `ACT_POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_FIXED_POST_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_FIXED_POST_TB`;

CREATE TABLE `ACT_FIXED_POST_TB` (
  `postId` int(10) unsigned NOT NULL,
  KEY `postId` (`postId`),
  CONSTRAINT `act_fixed_post_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `ACT_POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_POST_FIGURE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_POST_FIGURE_TB`;

CREATE TABLE `ACT_POST_FIGURE_TB` (
  `postId` int(10) unsigned NOT NULL,
  `cmtCount` int(11) NOT NULL DEFAULT '0',
  `likeCount` int(11) NOT NULL DEFAULT '0',
  `unlikeCount` int(11) NOT NULL DEFAULT '0',
  KEY `postId` (`postId`),
  CONSTRAINT `act_post_figure_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `ACT_POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_POST_FILE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_POST_FILE_TB`;

CREATE TABLE `ACT_POST_FILE_TB` (
  `postId` int(10) unsigned NOT NULL,
  `fileId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fileName` varchar(128) NOT NULL,
  `fileUrl` varchar(255) NOT NULL,
  PRIMARY KEY (`fileId`),
  KEY `postId` (`postId`),
  CONSTRAINT `act_post_file_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `ACT_POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_POST_REPORT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_POST_REPORT_TB`;

CREATE TABLE `ACT_POST_REPORT_TB` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postId` int(10) unsigned NOT NULL,
  `reporterId` int(10) unsigned NOT NULL,
  `reason` varchar(255) NOT NULL DEFAULT '',
  `reportedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `processCode` int(10) unsigned DEFAULT '4',
  PRIMARY KEY (`id`),
  KEY `postId` (`postId`),
  CONSTRAINT `act_post_report_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `ACT_POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_POST_STATE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_POST_STATE_TB`;

CREATE TABLE `ACT_POST_STATE_TB` (
  `postId` int(10) unsigned NOT NULL,
  `stateCode` int(10) unsigned NOT NULL,
  `pastDays` int(10) unsigned NOT NULL,
  `powerCode` int(10) unsigned NOT NULL,
  KEY `postId` (`postId`),
  KEY `stateCode` (`stateCode`),
  CONSTRAINT `act_post_state_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `ACT_POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `act_post_state_tb_ibfk_2` FOREIGN KEY (`stateCode`) REFERENCES `MAP_STATE_TB` (`stateCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_POST_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_POST_TB`;

CREATE TABLE `ACT_POST_TB` (
  `postId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(10) unsigned NOT NULL,
  `existFiles` tinyint(1) NOT NULL DEFAULT '0',
  `postNum` int(10) unsigned NOT NULL,
  `depth` int(11) NOT NULL DEFAULT '0',
  `title` varchar(128) NOT NULL,
  `contents` text,
  `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_POWER_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_POWER_TB`;

CREATE TABLE `ACT_POWER_TB` (
  `id` int(10) unsigned NOT NULL,
  `powerCode` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `powerCode` (`powerCode`),
  CONSTRAINT `act_power_tb_ibfk_1` FOREIGN KEY (`id`) REFERENCES `ACT_USERS_TB` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `act_power_tb_ibfk_2` FOREIGN KEY (`powerCode`) REFERENCES `MAP_POWER_TB` (`powerCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_USER_PW_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_USER_PW_TB`;

CREATE TABLE `ACT_USER_PW_TB` (
  `id` int(10) unsigned NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `act_user_pw_tb_ibfk_1` FOREIGN KEY (`id`) REFERENCES `ACT_USERS_TB` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_USER_STATE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_USER_STATE_TB`;

CREATE TABLE `ACT_USER_STATE_TB` (
  `id` int(10) unsigned NOT NULL,
  `userStateCode` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userStateCode` (`userStateCode`),
  CONSTRAINT `act_user_state_tb_ibfk_1` FOREIGN KEY (`id`) REFERENCES `ACT_USERS_TB` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `act_user_state_tb_ibfk_2` FOREIGN KEY (`userStateCode`) REFERENCES `MAP_USER_STATE_TB` (`userStateCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_USERINFO_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_USERINFO_TB`;

CREATE TABLE `ACT_USERINFO_TB` (
  `id` int(10) unsigned NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(8) NOT NULL,
  `profileUrl` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `act_userinfo_tb_ibfk_1` FOREIGN KEY (`id`) REFERENCES `ACT_USERS_TB` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table ACT_USERS_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ACT_USERS_TB`;

CREATE TABLE `ACT_USERS_TB` (
  `id` int(10) unsigned NOT NULL,
  `identity` varchar(255) NOT NULL,
  `isOAuth` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table MAP_POWER_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MAP_POWER_TB`;

CREATE TABLE `MAP_POWER_TB` (
  `power` varchar(128) NOT NULL,
  `powerCode` int(10) unsigned NOT NULL,
  PRIMARY KEY (`powerCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table MAP_PROCESS_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MAP_PROCESS_TB`;

CREATE TABLE `MAP_PROCESS_TB` (
  `process` varchar(20) NOT NULL,
  `processCode` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`processCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table MAP_STATE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MAP_STATE_TB`;

CREATE TABLE `MAP_STATE_TB` (
  `state` varchar(20) NOT NULL,
  `stateCode` int(10) unsigned NOT NULL,
  PRIMARY KEY (`stateCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table MAP_USER_STATE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MAP_USER_STATE_TB`;

CREATE TABLE `MAP_USER_STATE_TB` (
  `userState` varchar(128) NOT NULL DEFAULT '',
  `userStateCode` int(10) unsigned NOT NULL,
  PRIMARY KEY (`userStateCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
