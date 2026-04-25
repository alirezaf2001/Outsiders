-- MySQL dump 10.13  Distrib 9.6.0, for macos15 (arm64)
--
-- Host: localhost    Database: employeeData
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '3f66d0ee-086d-11f1-9cfc-48c91c2e914d:1-210';

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `addressID` int NOT NULL AUTO_INCREMENT,
  `street` varchar(100) NOT NULL,
  `cityID` int NOT NULL,
  `stateID` int NOT NULL,
  `zip` varchar(10) NOT NULL,
  `DOB` date DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `emergency_contact` varchar(130) DEFAULT NULL,
  `emergency_phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`addressID`),
  KEY `idx_addr_city` (`cityID`),
  KEY `idx_addr_state` (`stateID`),
  CONSTRAINT `fk_addr_city` FOREIGN KEY (`cityID`) REFERENCES `cities` (`cityID`),
  CONSTRAINT `fk_addr_state` FOREIGN KEY (`stateID`) REFERENCES `states` (`stateID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,'100 Main St',1,10,'30301','1990-01-15','404-555-0101','Contact A','404-555-0201'),(2,'200 Oak Ave',1,10,'30302','1988-03-22','404-555-0102','Contact B','404-555-0202'),(3,'300 Pine Rd',2,32,'10001','1992-07-04','212-555-0103','Contact C','212-555-0203'),(4,'400 Maple Dr',1,10,'30303','1985-11-30','404-555-0104','Contact D','404-555-0204'),(5,'500 Elm St',1,10,'30304','1993-05-18','404-555-0105','Contact E','404-555-0205'),(6,'600 Cedar Blvd',2,32,'10002','1991-09-09','212-555-0106','Contact F','212-555-0206'),(7,'700 Birch Ln',1,10,'30305','1987-02-14','404-555-0107','Contact G','404-555-0207'),(8,'800 Walnut Way',2,32,'10003','1994-12-01','212-555-0108','Contact H','212-555-0208'),(9,'900 Spruce Ct',1,10,'30306','1989-06-27','404-555-0109','Contact I','404-555-0209');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cities` (
  `cityID` int NOT NULL AUTO_INCREMENT,
  `cityName` varchar(25) NOT NULL,
  PRIMARY KEY (`cityID`),
  KEY `idx_cities_name` (`cityName`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
INSERT INTO `cities` VALUES (1,'Atlanta'),(4,'Chicago'),(5,'Houston'),(3,'Los Angeles'),(2,'New York');
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `division`
--

DROP TABLE IF EXISTS `division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `division` (
  `divID` int NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `city` varchar(50) NOT NULL,
  `addressLine1` varchar(50) NOT NULL,
  `addressLine2` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `country` varchar(50) NOT NULL,
  `postalCode` varchar(15) NOT NULL,
  PRIMARY KEY (`divID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='company divisions';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `division`
--

LOCK TABLES `division` WRITE;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
INSERT INTO `division` VALUES (1,'Technology Engineering','Atlanta','200 17th Street NW','','GA','USA','30363'),(2,'Marketing','Atlanta','200 17th Street NW','','GA','USA','30363'),(3,'Human Resources','New York','45 West 57th Street','','NY','USA','00034'),(999,'HQ','New York','45 West 57th Street','','NY','USA','00034');
/*!40000 ALTER TABLE `division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_division`
--

DROP TABLE IF EXISTS `employee_division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_division` (
  `empid` int NOT NULL,
  `div_ID` int NOT NULL,
  PRIMARY KEY (`empid`),
  KEY `fk_ed_div` (`div_ID`),
  CONSTRAINT `fk_ed_div` FOREIGN KEY (`div_ID`) REFERENCES `division` (`divID`),
  CONSTRAINT `fk_ed_emp` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='links employee to a division';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_division`
--

LOCK TABLES `employee_division` WRITE;
/*!40000 ALTER TABLE `employee_division` DISABLE KEYS */;
INSERT INTO `employee_division` VALUES (7,1),(10,3),(1,999),(2,999),(3,999);
/*!40000 ALTER TABLE `employee_division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_job_titles`
--

DROP TABLE IF EXISTS `employee_job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_job_titles` (
  `empid` int NOT NULL,
  `job_title_id` int NOT NULL,
  PRIMARY KEY (`empid`,`job_title_id`),
  KEY `fk_ejt_title` (`job_title_id`),
  CONSTRAINT `fk_ejt_emp` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`),
  CONSTRAINT `fk_ejt_title` FOREIGN KEY (`job_title_id`) REFERENCES `job_titles` (`job_title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_job_titles`
--

LOCK TABLES `employee_job_titles` WRITE;
/*!40000 ALTER TABLE `employee_job_titles` DISABLE KEYS */;
INSERT INTO `employee_job_titles` VALUES (7,100),(5,101),(4,102),(8,102),(9,102),(10,102),(14,103),(15,103),(11,200),(6,201),(12,201),(13,202),(2,900),(3,901),(1,902);
/*!40000 ALTER TABLE `employee_job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `empid` int NOT NULL,
  `Fname` varchar(65) NOT NULL,
  `Lname` varchar(65) NOT NULL,
  `email` varchar(65) NOT NULL,
  `HireDate` date DEFAULT NULL,
  `Salary` decimal(10,2) NOT NULL,
  `SSN` varchar(12) DEFAULT NULL,
  `addressID` int NOT NULL,
  PRIMARY KEY (`empid`),
  KEY `fk_emp_address` (`addressID`),
  KEY `idx_emp_lname` (`Lname`),
  KEY `idx_emp_ssn` (`SSN`),
  KEY `idx_emp_hiredate` (`HireDate`),
  CONSTRAINT `fk_emp_address` FOREIGN KEY (`addressID`) REFERENCES `addresses` (`addressID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Snoopy','Beagle','Snoopy@example.com','2022-08-01',45000.00,'111-11-1111',1),(2,'Charlie','Brown','Charlie@example.com','2022-07-01',48000.00,'111-22-1111',1),(3,'Lucy','Doctor','Lucy@example.com','2022-07-03',55000.00,'111-33-1111',2),(4,'Pepermint','Patti','Peppermint@example.com','2022-08-02',98000.00,'111-44-1111',3),(5,'Linus','Blanket','Linus@example.com','2022-09-01',43000.00,'111-55-1111',1),(6,'PigPin','Dusty','PigPin@example.com','2022-10-01',33000.00,'111-66-1111',3),(7,'Scooby','Doo','Scooby@example.com','1973-07-03',78000.00,'111-77-1111',4),(8,'Shaggy','Rodgers','Shaggy@example.com','1973-07-11',77000.00,'111-88-1111',5),(9,'Velma','Dinkley','Velma@example.com','1973-07-21',82000.00,'111-99-1111',6),(10,'Daphne','Blake','Daphne@example.com','1973-07-30',59000.00,'111-00-1111',4),(11,'Bugs','Bunny','Bugs@example.com','1934-07-01',18000.00,'222-11-1111',5),(12,'Daffy','Duck','Daffy@example.com','1935-04-01',16000.00,'333-11-1111',2),(13,'Porky','Pig','Porky@example.com','1935-08-12',16550.00,'444-11-1111',7),(14,'Elmer','Fudd','Elmer@example.com','1934-08-01',15500.00,'555-11-1111',8),(15,'Marvin','Martian','Marvin@example.com','1937-05-01',28000.00,'777-11-1111',9);
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_titles`
--

DROP TABLE IF EXISTS `job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_titles` (
  `job_title_id` int NOT NULL,
  `job_title` varchar(125) NOT NULL,
  PRIMARY KEY (`job_title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_titles`
--

LOCK TABLES `job_titles` WRITE;
/*!40000 ALTER TABLE `job_titles` DISABLE KEYS */;
INSERT INTO `job_titles` VALUES (100,'software manager'),(101,'software architect'),(102,'software engineer'),(103,'software developer'),(200,'marketing manager'),(201,'marketing associate'),(202,'marketing assistant'),(900,'Chief Exec. Officer'),(901,'Chief Finn. Officer'),(902,'Chief Info. Officer');
/*!40000 ALTER TABLE `job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payroll`
--

DROP TABLE IF EXISTS `payroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payroll` (
  `payID` int NOT NULL,
  `pay_date` date DEFAULT NULL,
  `earnings` decimal(8,2) DEFAULT NULL,
  `fed_tax` decimal(7,2) DEFAULT NULL,
  `fed_med` decimal(7,2) DEFAULT NULL,
  `fed_SS` decimal(7,2) DEFAULT NULL,
  `state_tax` decimal(7,2) DEFAULT NULL,
  `retire_401k` decimal(7,2) DEFAULT NULL,
  `health_care` decimal(7,2) DEFAULT NULL,
  `empid` int NOT NULL,
  PRIMARY KEY (`payID`),
  KEY `idx_payroll_empid` (`empid`),
  KEY `idx_payroll_paydate` (`pay_date`),
  CONSTRAINT `fk_payroll_emp` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payroll`
--

LOCK TABLES `payroll` WRITE;
/*!40000 ALTER TABLE `payroll` DISABLE KEYS */;
INSERT INTO `payroll` VALUES (1,'2026-01-31',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1),(2,'2025-12-31',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1),(3,'2026-01-31',923.08,295.38,13.38,57.23,110.77,3.69,28.62,2),(4,'2025-12-31',923.08,295.38,13.38,57.23,110.77,3.69,28.62,2),(5,'2026-01-31',1057.69,338.46,15.34,65.58,126.92,4.23,32.79,3),(6,'2025-12-31',1057.69,338.46,15.34,65.58,126.92,4.23,32.79,3),(7,'2026-01-31',1884.62,603.08,27.33,116.85,226.15,7.54,58.42,4),(8,'2025-12-31',1884.62,603.08,27.33,116.85,226.15,7.54,58.42,4),(9,'2026-01-31',826.92,264.62,11.99,51.27,99.23,3.31,25.63,5),(10,'2025-12-31',826.92,264.62,11.99,51.27,99.23,3.31,25.63,5),(11,'2026-01-31',634.62,203.08,9.20,39.35,76.15,2.54,19.67,6),(12,'2025-12-31',634.62,203.08,9.20,39.35,76.15,2.54,19.67,6);
/*!40000 ALTER TABLE `payroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `states`
--

DROP TABLE IF EXISTS `states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `states` (
  `stateID` int NOT NULL AUTO_INCREMENT,
  `abbrev` char(2) NOT NULL,
  PRIMARY KEY (`stateID`),
  KEY `idx_states_abbrev` (`abbrev`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `states`
--

LOCK TABLES `states` WRITE;
/*!40000 ALTER TABLE `states` DISABLE KEYS */;
INSERT INTO `states` VALUES (2,'AK'),(1,'AL'),(4,'AR'),(3,'AZ'),(5,'CA'),(6,'CO'),(7,'CT'),(8,'DE'),(9,'FL'),(10,'GA'),(11,'HI'),(15,'IA'),(12,'ID'),(13,'IL'),(14,'IN'),(16,'KS'),(17,'KY'),(18,'LA'),(21,'MA'),(20,'MD'),(19,'ME'),(22,'MI'),(23,'MN'),(25,'MO'),(24,'MS'),(26,'MT'),(33,'NC'),(34,'ND'),(27,'NE'),(29,'NH'),(30,'NJ'),(31,'NM'),(28,'NV'),(32,'NY'),(35,'OH'),(36,'OK'),(37,'OR'),(38,'PA'),(39,'RI'),(40,'SC'),(41,'SD'),(42,'TN'),(43,'TX'),(44,'UT'),(46,'VA'),(45,'VT'),(47,'WA'),(49,'WI'),(48,'WV'),(50,'WY');
/*!40000 ALTER TABLE `states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'employeeData'
--
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-20 12:43:58
