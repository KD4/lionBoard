
DROP ALL OBJECTS;


CREATE TABLE POST_TB (
  postId int(10) unsigned NOT NULL AUTO_INCREMENT,
  userId int(10) unsigned NOT NULL,
  existFiles varchar(2) NOT NULL DEFAULT 'F',
  postNum int(10) unsigned NOT NULL DEFAULT '0',
  depth int(10) NOT NULL DEFAULT '0',
  title varchar(128) NOT NULL,
  contents text NOT NULL,
  createdAt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  cmtCount int(10) unsigned NOT NULL DEFAULT '0',
  likeCount int(10) unsigned NOT NULL DEFAULT '0',
  hateCount int(10) unsigned NOT NULL DEFAULT '0',
  viewCount int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (postId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE POST_FILE_TB (
  postId int(10) unsigned NOT NULL,
  fileId int(10) unsigned NOT NULL AUTO_INCREMENT,
  fileName varchar(128) NOT NULL,
  fileUrl varchar(255) NOT NULL,
  PRIMARY KEY(fileId),
  FOREIGN KEY(postId) REFERENCES POST_TB(postId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE POST_REPORT_TB (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  postId int(10) unsigned NOT NULL,
  reporterId int(10) unsigned NOT NULL,
  reason varchar(255) NOT NULL DEFAULT '',
  reportedAt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  processStatus varchar(2) NOT NULL DEFAULT 'N',
  PRIMARY KEY(id),
  FOREIGN KEY(postId) REFERENCES POST_TB(postId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE POST_STATUS_TB (
  postId int(10) unsigned NOT NULL,
  postStatus varchar(2) NOT NULL DEFAULT 'S',
  pastDays int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (postId),
  FOREIGN KEY (postId) REFERENCES POST_TB(postId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE CMT_TB (
  cmtId int(10) unsigned NOT NULL AUTO_INCREMENT,
  postId int(10) unsigned NOT NULL,
  userId int(10) unsigned NOT NULL,
  cmtNum int(10) unsigned NOT NULL DEFAULT '0',
  depth int(11) NOT NULL DEFAULT '0',
  contents text NOT NULL,
  createdAt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  likeCount int(10) unsigned NOT NULL DEFAULT '0',
  hateCount int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (cmtId),
  FOREIGN KEY (postId) REFERENCES POST_TB(postId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE CMT_REPORT_TB (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  cmtId int(10) unsigned NOT NULL,
  reporterId int(10) unsigned NOT NULL,
  reason varchar(255) NOT NULL DEFAULT '',
  reportedAt datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  processStatus int(10) unsigned NOT NULL DEFAULT '4',
  PRIMARY KEY (id),
  FOREIGN KEY (cmtId) REFERENCES CMT_TB(cmtId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE CMT_STATUS_TB (
  cmtId int(10) unsigned NOT NULL,
  cmtStatus varchar(2) NOT NULL DEFAULT 'S',
  pastDays int(10) unsigned NOT NULL DEFAULT '0',
  FOREIGN KEY (cmtId) REFERENCES CMT_TB(cmtId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TRIGGER set_postNum BEFORE INSERT ON POST_TB
  FOR EACH ROW CALL "com.github.lionboard.triggers.PostTriggers";

CREATE TRIGGER set_cmtNum BEFORE INSERT ON CMT_TB
FOR EACH ROW CALL "com.github.lionboard.triggers.CmtTriggers";

--
--
--
-- DELIMITER ;;
-- /*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION" */;;
-- /*!50003 CREATE */ /*!50017 DEFINER=root@localhost */ /*!50003 TRIGGER set_cmtNum BEFORE INSERT ON CMT_TB FOR EACH ROW IF NEW.depth < 1 THEN
--   SET @cmtNum = (SELECT cmtNum FROM CMT_TB WHERE depth = 0 AND postId = NEW.postId ORDER BY cmtNum DESC LIMIT 1);
--     IF @cmtNum IS NULL THEN
--       SET NEW.cmtNum = 1000;
--     ELSE
--       SET NEW.cmtNum = @cmtNum + 1000;
--     END IF;
-- ELSE
--   SET NEW.cmtNum = NEW.cmtNum - 1;
-- END IF */;;
-- DELIMITER ;
-- /*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;
--
-- DELIMITER ;;
-- /*!50003 SET SESSION SQL_MODE="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION" */;;
-- /*!50003 CREATE */ /*!50017 DEFINER=root@localhost */ /*!50003 TRIGGER set_postNum BEFORE INSERT ON POST_TB FOR EACH ROW IF NEW.depth < 1 THEN
--   SET @postNum = (SELECT postNum FROM POST_TB WHERE depth = 0 ORDER BY postNum DESC LIMIT 1);
--     IF @postNum IS NULL THEN
--       SET NEW.postNum = 1000;
--     ELSE
--       SET NEW.postNum = @postNum + 1000;
--     END IF;
-- ELSE
--   SET NEW.postNum = NEW.postNum - 1;
-- END IF */;;
-- DELIMITER ;
-- /*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;

CREATE TABLE STICKY_CMT_TB (
  cmtId int(10) unsigned NOT NULL,
  postId int(10) unsigned NOT NULL,
  FOREIGN KEY (cmtId) REFERENCES cmt_tb(cmtId) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (postId) REFERENCES post_tb(postId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE STICKY_POST_TB (
  postId int(10) unsigned NOT NULL,
  FOREIGN KEY (postId) REFERENCES post_tb(postId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE USER_TB (
  id int(10) unsigned NOT NULL AUTO_INCREMENT,
  identity varchar(255) NOT NULL,
  isOAuth varchar(2) NOT NULL DEFAULT 'F',
  roles varchar(1) NOT NULL DEFAULT 'U',
  name varchar(50) NOT NULL DEFAULT ' ',
  email varchar(255) NOT NULL DEFAULT '',
  profileUrl varchar(255) NOT NULL DEFAULT ' ',
  userStatus varchar(2) NOT NULL DEFAULT 'S',
  registeredAt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  password varchar(100) NOT NULL DEFAULT ' ',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
