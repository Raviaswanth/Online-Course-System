-- MySQL dump 10.13  Distrib 5.7.16, for Win64 (x86_64)
--
-- Host: localhost    Database: coursedb
-- ------------------------------------------------------
-- Server version	5.7.16-log




/* If running on a local mysql install use coursedb as the database name */

 DROP DATABASE IF EXISTS `coursedb`;
 CREATE DATABASE `coursedb` ;
 USE `coursedb`;


 /*
  * the URL for a local mysql install is jdbc:mysql://localhost:3306/coursedb
  */

-- Run from this line when running on latcs7 mysql
  
--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject` (
  `code` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  `hasPrerequisites` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`code`),
  UNIQUE KEY `name_UNIQUE` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;

INSERT INTO `subject` VALUES ('CSE3OAD','OBJECT-ORIENTED APPLICATION DEVELOPMENT',1),('CSE1IOO','INTERMEDIATE OBJECT-ORIENTED PROGRAMMING',1),('CSE1OOF','OBJECT-ORIENTED PROGRAMMING FUNDAMENTALS',0),('CSE2DES' ,'SYSTEM DESIGN ENGINEERING FUNDAMENTALS',1),('CSE2DBF','DATABASE FUNDAMENTALS',1),('CSE1PE','PROGRAMMING ENVIRONMENT',0),('MAT1DM','DISCRETE MATHEMATICS',0),('CSE5DMI','DATA MINING',1);

UNLOCK TABLES;



--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subjectCode` varchar(20) NOT NULL,
  `startingDate` varchar(10) DEFAULT NULL,
  `numberOfStudents` int(11) DEFAULT NULL,
  `semester` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `subjectCode_idx` (`subjectCode`),
  CONSTRAINT `fk_course_subject` FOREIGN KEY (`subjectCode`) REFERENCES `subject` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;

INSERT INTO `course` VALUES (1,'CSE1IOO','17/07/2021',20,'SE2'),(2,'CSE1OOF','20/03/2021',21,'SE1'),(3,'CSE2DBF','24/11/2021',23,'SUM');

UNLOCK TABLES;




