-- MySQL dump 10.13  Distrib 5.7.24, for Win64 (x86_64)
--
-- Host: localhost    Database: individual
-- ------------------------------------------------------
-- Server version	5.7.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` int(11) NOT NULL,
  `receiver` int(11) NOT NULL,
  `message_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `title` varchar(25) DEFAULT NULL,
  `text` varchar(250) DEFAULT NULL,
  `message_state` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`message_id`),
  KEY `idx_message_id` (`message_id`),
  KEY `sender_fk` (`sender`),
  KEY `idx_message_state` (`message_state`),
  KEY `receiver_fk` (`receiver`),
  CONSTRAINT `receiver_fk` FOREIGN KEY (`receiver`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `sender_fk` FOREIGN KEY (`sender`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,1,3,'2018-11-16 22:42:42','otitlos','neo minima',0),(3,1,4,'2018-11-10 22:23:27','deftero se admin','pame ki alli dokimi',1),(6,1,1,'2018-11-16 22:12:45','ston eafto mou','dokimastiko ston eafto moy',2),(8,1,2,'2018-11-17 18:16:16','ena minima','ayto to minima den exei kanena noima alla kalo einai na to dokimaseis !! sosta??? kai polla grammata kai arithous 1231231231safwrwet342534534',2),(71,4,3,'2018-11-19 07:53:17','Kalispera!','Geia sou moda! ti nea?',2),(72,3,12,'2018-11-19 07:47:48','Geiasssssss','Hello there? What\'s up doc??',1),(73,3,14,'2018-11-19 07:48:57','God morgen!!','Hello there!! All good? Send me your news!',1),(74,4,3,'2018-11-19 07:54:03','Hello There','Modas good morning! What is up? Send me your feedback for the app',1),(75,4,2,'2018-11-19 07:54:53','All is good','Hello there. Program is running smooth. No problems so far. Send me your opinion!',1),(76,2,4,'2018-11-19 07:56:20','Feedback','Everything is working good for me. No problems so far. Looks good!',1),(77,3,4,'2018-11-19 09:09:50','Whats up?','All good bro? The program runs smooth. The customers are satisfied. hehe!',1);
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `user_type` int(2) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_unique` (`username`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_user_type` (`user_type`),
  KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'pioaplos','123',1),(2,'aplos','1234',2),(3,'modas','12345',3),(4,'admin','admin',4),(12,'dialextos','paixths1',3),(14,'mikrospaixths','passmikro1',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-19 12:02:53
