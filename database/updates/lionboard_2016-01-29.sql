# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: 127.0.0.1 (MySQL 5.6.25)
# Database: lionboard
# Generation Time: 2016-01-29 06:51:25 +0000
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
  `reportedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `processStatus` varchar(2) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`),
  KEY `cmtId` (`cmtId`),
  CONSTRAINT `cmt_report_tb_ibfk_2` FOREIGN KEY (`cmtId`) REFERENCES `CMT_TB` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CMT_STATUS_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CMT_STATUS_TB`;

CREATE TABLE `CMT_STATUS_TB` (
  `cmtId` int(10) unsigned NOT NULL,
  `cmtStatus` varchar(2) NOT NULL DEFAULT 'S',
  `pastDays` int(10) unsigned NOT NULL DEFAULT '0',
  KEY `cmtId` (`cmtId`),
  CONSTRAINT `cmt_status_tb_ibfk_2` FOREIGN KEY (`cmtId`) REFERENCES `CMT_TB` (`cmtId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table CMT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `CMT_TB`;

CREATE TABLE `CMT_TB` (
  `cmtId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postId` int(10) unsigned NOT NULL,
  `userId` int(10) unsigned NOT NULL,
  `cmtNum` int(10) unsigned NOT NULL DEFAULT '0',
  `depth` int(11) NOT NULL DEFAULT '0',
  `contents` text NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `likeCount` int(10) unsigned NOT NULL DEFAULT '0',
  `hateCount` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`cmtId`),
  KEY `postId` (`postId`),
  CONSTRAINT `cmt_tb_ibfk_2` FOREIGN KEY (`postId`) REFERENCES `POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `set_cmtNum` BEFORE INSERT ON `CMT_TB` FOR EACH ROW IF NEW.depth < 1 THEN
  SET @cmtNum = (SELECT cmtNum FROM CMT_TB WHERE depth = 0 AND postId = NEW.postId ORDER BY cmtNum DESC LIMIT 1);
    IF @cmtNum IS NULL THEN
      SET NEW.cmtNum = 1000;
    ELSE
      SET NEW.cmtNum = @cmtNum + 1000;
    END IF;
ELSE
  SET NEW.cmtNum = NEW.cmtNum - 1;
END IF */;;
DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;


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
  CONSTRAINT `post_file_tb_ibfk_2` FOREIGN KEY (`postId`) REFERENCES `POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table POST_REPORT_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `POST_REPORT_TB`;

CREATE TABLE `POST_REPORT_TB` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `postId` int(10) unsigned NOT NULL,
  `reporterId` int(10) unsigned NOT NULL,
  `reason` varchar(255) NOT NULL DEFAULT '',
  `reportedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
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
  `postStatus` varchar(2) NOT NULL DEFAULT 'S',
  `pastDays` int(10) unsigned NOT NULL DEFAULT '0',
  KEY `postId` (`postId`),
  CONSTRAINT `post_status_tb_ibfk_2` FOREIGN KEY (`postId`) REFERENCES `POST_TB` (`postId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `POST_STATUS_TB` WRITE;
/*!40000 ALTER TABLE `POST_STATUS_TB` DISABLE KEYS */;

INSERT INTO `POST_STATUS_TB` (`postId`, `postStatus`, `pastDays`)
VALUES
	(449,'S',0),
	(450,'S',0),
	(451,'S',0),
	(452,'S',0),
	(453,'S',0);

/*!40000 ALTER TABLE `POST_STATUS_TB` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table POST_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `POST_TB`;

CREATE TABLE `POST_TB` (
  `postId` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userId` int(10) unsigned NOT NULL,
  `existFiles` varchar(2) NOT NULL DEFAULT 'F',
  `postNum` int(10) unsigned NOT NULL DEFAULT '0',
  `depth` int(10) NOT NULL DEFAULT '0',
  `title` varchar(128) NOT NULL,
  `contents` text NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cmtCount` int(10) unsigned NOT NULL DEFAULT '0',
  `likeCount` int(10) unsigned NOT NULL DEFAULT '0',
  `hateCount` int(10) unsigned NOT NULL DEFAULT '0',
  `viewCount` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `POST_TB` WRITE;
/*!40000 ALTER TABLE `POST_TB` DISABLE KEYS */;

INSERT INTO `POST_TB` (`postId`, `userId`, `existFiles`, `postNum`, `depth`, `title`, `contents`, `createdAt`, `cmtCount`, `likeCount`, `hateCount`, `viewCount`)
VALUES
	(449,230,'F',1000,0,'강관우 테스트','<p>테테테스트</p>\n','2016-01-28 14:32:42',0,0,0,0),
	(450,230,'F',2000,0,'두번째 테스트','<p>다시 테스트닷</p>\n','2016-01-28 14:35:07',0,0,0,0),
	(451,231,'F',3000,0,'새 유저 글쓰기','<p>강딴<strong>ㄱ딴</strong></p>\n\n<p><strong>딴딴딴</strong></p>\n','2016-01-28 17:04:11',0,0,0,0),
	(452,230,'F',4000,0,'글쓰기 테스트','<p>이게 될랑가 몰라~</p>\n','2016-01-29 14:45:38',0,0,0,0),
	(453,240,'F',5000,0,'Ryan? Lion!','<p>This is <em>Lion ~&nbsp;</em></p>\n\n<h2>not Ryan!</h2>\n\n<ol>\n	<li><strong>유남쌩~</strong></li>\n</ol>\n','2016-01-29 15:48:26',0,0,0,0);

/*!40000 ALTER TABLE `POST_TB` ENABLE KEYS */;
UNLOCK TABLES;

DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `set_postNum` BEFORE INSERT ON `POST_TB` FOR EACH ROW IF NEW.depth < 1 THEN
  SET @postNum = (SELECT postNum FROM POST_TB WHERE depth = 0 ORDER BY postNum DESC LIMIT 1);
    IF @postNum IS NULL THEN
      SET NEW.postNum = 1000;
    ELSE
      SET NEW.postNum = @postNum + 1000;
    END IF;
ELSE
  SET NEW.postNum = NEW.postNum - 1;
END IF */;;
DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;


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



# Dump of table USER_TB
# ------------------------------------------------------------

DROP TABLE IF EXISTS `USER_TB`;

CREATE TABLE `USER_TB` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `identity` varchar(255) NOT NULL,
  `isOAuth` varchar(2) NOT NULL DEFAULT 'F',
  `roles` varchar(12) NOT NULL DEFAULT 'ROLE_USER',
  `name` varchar(50) NOT NULL DEFAULT ' ',
  `email` varchar(255) NOT NULL DEFAULT '',
  `profileUrl` varchar(255) NOT NULL DEFAULT ' ',
  `userStatus` varchar(2) NOT NULL DEFAULT 'S',
  `registeredAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `password` varchar(100) NOT NULL DEFAULT ' ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `identity` (`identity`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `USER_TB` WRITE;
/*!40000 ALTER TABLE `USER_TB` DISABLE KEYS */;

INSERT INTO `USER_TB` (`id`, `identity`, `isOAuth`, `roles`, `name`, `email`, `profileUrl`, `userStatus`, `registeredAt`, `password`)
VALUES
	(230,'rkdrhksdn2@gmail.com','F','ROLE_USER','강관우','rkdrhksdn2@gmail.com','temp.url','S','2016-01-28 14:12:09','Starter?5'),
	(231,'goodmorning0726@gmail.com','F','ROLE_USER','강딴딴','goodmorning0726@gmail.com','temp.url','S','2016-01-28 16:39:59','tkdlek18'),
	(232,'new@kakaocorp.com','F','ROLE_USER','카카오','new@kakaocorp.com','temp.url','S','2016-01-29 14:10:41','tkdlek18'),
	(240,'lion@gmail.com','F','ROLE_USER','사자임','lion@gmail.com','temp.url','S','2016-01-29 15:45:15','lionboard');

/*!40000 ALTER TABLE `USER_TB` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
