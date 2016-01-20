# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.25)
# Database: lionboard
# Generation Time: 2016-01-20 12:45:35 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table CMT_REPORT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CMT_REPORT_TB`;

CREATE TABLE `CMT_REPORT_TB` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cmtId` int(10) unsigned NOT NULL,
  `reporterId` int(10) unsigned NOT NULL,
  `reason` varchar(255) NOT NULL DEFAULT '',
  `reportedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `processStatus` int(10) unsigned DEFAULT '4',
  PRIMARY KEY (`id`),
  KEY `cmtId` (`cmtId`),
  CONSTRAINT `cmt_report_tb_ibfk_1` FOREIGN KEY (`cmtId`) REFERENCES `cmt_tb` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CMT_STATUS_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CMT_STATUS_TB`;

CREATE TABLE `CMT_STATUS_TB` (
  `cmtId` int(10) unsigned NOT NULL,
  `cmtStatus` int(10) unsigned NOT NULL,
  `pastDays` int(10) unsigned NOT NULL,
  KEY `cmtId` (`cmtId`),
  KEY `stateCode` (`cmtStatus`),
  CONSTRAINT `cmt_status_tb_ibfk_2` FOREIGN KEY (`cmtId`) REFERENCES `CMT_TB` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CMT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CMT_TB`;

CREATE TABLE `CMT_TB` (
  `cmtId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postId` int(10) unsigned NOT NULL,
  `userId` int(10) unsigned NOT NULL,
  `cmtNum` int(10) unsigned NOT NULL,
  `depth` int(11) NOT NULL DEFAULT '0',
  `contents` text,
  `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cmtStatus` varchar(2) NOT NULL DEFAULT 'S',
  `likeCount` int(10) unsigned NOT NULL DEFAULT '0',
  `hateCount` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`cmtId`),
  KEY `postId` (`postId`),
  CONSTRAINT `cmt_tb_ibfk_2` FOREIGN KEY (`postId`) REFERENCES `POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table STICKY_CMT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `STICKY_CMT_TB`;

CREATE TABLE `STICKY_CMT_TB` (
  `cmtId` int(10) unsigned NOT NULL,
  `postId` int(10) unsigned NOT NULL,
  KEY `cmtId` (`cmtId`),
  KEY `postId` (`postId`),
  CONSTRAINT `sticky_cmt_tb_ibfk_1` FOREIGN KEY (`cmtId`) REFERENCES `cmt_tb` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sticky_cmt_tb_ibfk_2` FOREIGN KEY (`postId`) REFERENCES `post_tb` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table STICKY_POST_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `STICKY_POST_TB`;

CREATE TABLE `STICKY_POST_TB` (
  `postId` int(10) unsigned NOT NULL,
  KEY `postId` (`postId`),
  CONSTRAINT `sticky_post_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `post_tb` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table POST_FILE_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `POST_FILE_TB`;

CREATE TABLE `POST_FILE_TB` (
  `postId` int(10) unsigned NOT NULL,
  `fileId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fileName` varchar(128) NOT NULL,
  `fileUrl` varchar(255) NOT NULL,
  PRIMARY KEY (`fileId`),
  KEY `postId` (`postId`),
  CONSTRAINT `post_file_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `post_tb` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table POST_REPORT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `POST_REPORT_TB`;

CREATE TABLE `POST_REPORT_TB` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postId` int(10) unsigned NOT NULL,
  `reporterId` int(10) unsigned NOT NULL,
  `reason` varchar(255) NOT NULL DEFAULT '',
  `reportedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `processStatus` varchar(2) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `postId` (`postId`),
  CONSTRAINT `post_report_tb_ibfk_3` FOREIGN KEY (`postId`) REFERENCES `POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table POST_STATUS_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `POST_STATUS_TB`;

CREATE TABLE `POST_STATUS_TB` (
  `postId` int(10) unsigned NOT NULL,
  `postStatus` int(10) unsigned NOT NULL,
  `pastDays` int(10) unsigned NOT NULL,
  KEY `postId` (`postId`),
  KEY `stateCode` (`postStatus`),
  CONSTRAINT `post_status_tb_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `post_tb` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table POST_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `POST_TB`;

CREATE TABLE `POST_TB` (
  `postId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(10) unsigned NOT NULL,
  `existFiles` tinyint(1) NOT NULL DEFAULT '0',
  `postNum` int(10) unsigned NOT NULL,
  `depth` int(10) NOT NULL DEFAULT '0',
  `title` varchar(128) NOT NULL,
  `contents` text,
  `createdAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `postStatus` varchar(1) NOT NULL DEFAULT 'S',
  `cmtCount` int(10) unsigned NOT NULL DEFAULT '0',
  `likeCount` int(10) unsigned NOT NULL DEFAULT '0',
  `hateCount` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table USER_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `USER_TB`;

CREATE TABLE `USER_TB` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `identity` varchar(255) NOT NULL,
  `isOAuth` tinyint(1) NOT NULL DEFAULT '0',
  `roles` varchar(1) NOT NULL DEFAULT 'U',
  `name` varchar(50) NOT NULL DEFAULT ' ',
  `email` varchar(255) NOT NULL DEFAULT '',
  `profileUrl` varchar(255) NOT NULL DEFAULT ' ',
  `userStatus` varchar(2) NOT NULL DEFAULT 'S',
  `registedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
