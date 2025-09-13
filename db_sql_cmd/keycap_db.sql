CREATE DATABASE  IF NOT EXISTS `keycap` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `keycap`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: keycap
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `cart_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `variant_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `added_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  KEY `user_id` (`user_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`variant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,2,1,2,'2025-06-24 21:06:42'),(2,2,2,1,'2025-06-24 21:06:42'),(3,3,3,1,'2025-06-24 21:06:42'),(4,4,4,3,'2025-06-24 21:06:42'),(5,5,5,2,'2025-06-24 21:06:42'),(6,6,6,1,'2025-06-24 21:06:42'),(7,7,7,4,'2025-06-24 21:06:42'),(8,8,8,1,'2025-06-24 21:06:42'),(9,9,9,2,'2025-06-24 21:06:42'),(10,10,10,2,'2025-06-24 21:06:42'),(11,2,2,1,'2025-07-24 14:24:50'),(12,2,3,1,'2025-07-24 14:25:39'),(13,1,3,1,'2025-07-24 14:27:41'),(56,12,3,1,'2025-09-12 12:29:21'),(57,12,66,3,'2025-09-12 12:31:35');
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_details`
--

DROP TABLE IF EXISTS `order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_details` (
  `order_detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `variant_id` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(18,2) DEFAULT NULL,
  PRIMARY KEY (`order_detail_id`),
  KEY `order_id` (`order_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`variant_id`) REFERENCES `product_variants` (`variant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_details`
--

LOCK TABLES `order_details` WRITE;
/*!40000 ALTER TABLE `order_details` DISABLE KEYS */;
INSERT INTO `order_details` VALUES (1,1,1,1,20.00),(2,2,3,1,5.50),(3,3,4,3,4.50),(4,4,5,2,4.80),(5,5,6,2,30.00),(6,6,8,2,20.00),(7,7,7,1,10.00),(8,8,9,2,6.00),(9,9,10,2,5.80),(10,10,2,10,5.00),(11,12,2,2,5.00),(12,12,4,1,4.50),(16,13,1,3,100.00),(17,10,1,3,100.00),(18,15,6,4,30.00),(19,15,4,1,4.50),(20,16,4,1,4.50),(21,16,5,2,4.80),(22,18,5,2,4.80),(23,19,6,1,30.00),(24,19,1,2,20.00),(25,20,5,2,4.80),(26,21,67,1,1200000.00),(27,22,66,4,1250000.00),(28,23,1,1,20.00),(29,24,6,1,30.00),(30,24,8,2,25.00),(31,25,8,1,25.00),(32,25,10,2,150.00),(33,26,10,1,150.00),(34,26,12,2,870000.00),(35,27,19,1,1500000.00),(36,27,12,2,870000.00),(37,29,10,1,150.00),(38,29,12,2,870000.00),(39,30,5,2,4.80),(40,31,68,1,1250000.00),(41,32,4,1,4.50),(42,33,7,1,10.00),(43,34,4,1,4.50),(44,35,69,1,1200000.00),(45,36,12,1,870000.00),(46,37,12,1,870000.00),(47,38,65,1,1200000.00),(48,38,3,2,5.50),(49,38,7,13,10.00),(50,38,63,2,1200000.00),(51,38,67,1,1200000.00);
/*!40000 ALTER TABLE `order_details` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_order_details_after_insert` AFTER INSERT ON `order_details` FOR EACH ROW BEGIN
    UPDATE product_variants
    SET stock_quantity = stock_quantity - NEW.quantity
    WHERE variant_id = NEW.variant_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `order_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `total_amount` decimal(18,2) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `shipping_fee` decimal(18,2) DEFAULT '25000.00',
  `full_name` varchar(255) NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,2,'2025-06-24 21:06:47',30.00,'0123450001','123 A St','john@example.com','COMPLETED',25000.00,''),(2,3,'2025-06-24 21:06:47',5.50,'0123450002','124 A St','alice@example.com','COMPLETED',25000.00,''),(3,4,'2025-06-24 21:06:47',13.50,'0123450003','125 A St','bob@example.com','COMPLETED',25000.00,''),(4,5,'2025-06-24 21:06:47',9.60,'0123450004','126 A St','charlie@example.com','SHIPPING',25000.00,''),(5,6,'2025-06-24 21:06:47',60.00,'0123450005','127 A St','david@example.com','CANCELLED',25000.00,''),(6,7,'2025-06-24 21:06:47',40.00,'0123450006','128 A St','emma@example.com','PENDING',25000.00,''),(7,8,'2025-06-24 21:06:47',10.00,'0123450007','129 A St','frank@example.com','COMPLETED',25000.00,''),(8,9,'2025-06-24 21:06:47',12.00,'0123450008','130 A St','grace@example.com','SHIPPING',25000.00,''),(9,10,'2025-06-24 21:06:47',11.60,'0123450009','131 A St','henry@example.com','PENDING',25000.00,''),(10,3,'2025-06-24 21:06:47',50.00,'0123450002','124 A St','alice@example.com','SHIPPING',25000.00,''),(11,1,'2025-08-04 23:41:19',15.00,'21312','Qn NG','@gmai','PENDING',25000.00,''),(12,11,'2025-08-04 23:42:09',14.50,'0987654321','123 Đường ABC, Quận 1, TP.HCM','user@example.com','PENDING',25000.00,''),(13,11,'2025-08-06 17:05:11',184.50,'0987654321','123 Đường ABC, Quận 1, TP.HCM','user@example.com','PENDING',25000.00,''),(14,11,'2025-08-06 17:16:13',124.50,'0987654321','123 Đường ABC, Quận 1, TP.HCM','user@example.com','PENDING',25000.00,''),(15,11,'2025-08-06 17:56:27',124.50,'0987654321','123 Đường ABC, Quận 1, TP.HCM','user@example.com','PENDING',25000.00,''),(16,11,'2025-08-07 16:56:33',14.10,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','CANCELLED',25000.00,''),(17,NULL,'2025-08-08 22:22:10',1.00,'6216165','Qn','@email.com','PENDING',25000.00,''),(18,NULL,'2025-08-08 22:29:02',9.60,'0901234567','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(19,11,'2025-08-08 22:48:20',70.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(20,NULL,'2025-08-08 22:48:44',9.60,'0901234567','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(21,17,'2025-09-11 09:09:32',1200000.00,'0866904453','thôn lam, Xã Minh Bảo, Thành phố Yên Bái,  Tỉnh Yên Bái','buidinhtuan04@gmail.com','PENDING',25000.00,''),(22,17,'2025-09-11 09:17:41',5000000.00,'0866904453','thôn lam, Phường Chiềng Cơi, Thành phố Sơn La,  Tỉnh Sơn La','buidinhtuan04@gmail.com','PENDING',25000.00,''),(23,17,'2025-09-11 09:23:11',20.00,'0866904453','thôn lam, Xã Tòng Đậu, Huyện Mai Châu,  Tỉnh Hoà Bình','buidinhtuan04@gmail.com','PENDING',25000.00,''),(24,17,'2025-09-11 09:35:02',80.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(25,17,'2025-09-11 09:50:55',325.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(26,17,'2025-09-11 09:59:30',1740150.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(27,17,'2025-09-11 10:19:39',3240000.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(28,1,'2025-09-11 15:28:39',25002.00,'32165','asdfasdfa','@gmail','PENDING',25000.00,''),(29,17,'2025-09-11 15:44:54',1765150.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(30,NULL,'2025-09-11 15:56:18',25009.60,'0901234567','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(31,17,'2025-09-11 18:36:01',1275000.00,'0866904453','33333, Xã Nánh Nghê, Huyện Đà Bắc,  Tỉnh Hoà Bình','buidinhtuan04@gmail.com','PENDING',25000.00,''),(32,17,'2025-09-11 18:37:15',25004.50,'0866904453','thôn lam, Xã A Mú Sung, Huyện Bát Xát,  Tỉnh Lào Cai','buidinhtuan04@gmail.com','PENDING',25000.00,''),(33,17,'2025-09-11 18:38:45',25010.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','PENDING',25000.00,''),(34,17,'2025-09-11 18:40:01',25004.50,'0866904453','thôn lam, Xã Mậu Đông, Huyện Văn Yên,  Tỉnh Yên Bái','buidinhtuan04@gmail.com','PENDING',25000.00,''),(35,17,'2025-09-11 18:40:57',1225000.00,'0866904453','thôn lam, Xã Chiềng Cang, Huyện Sông Mã,  Tỉnh Sơn La','zaisersama@shopziin.com','PENDING',25000.00,''),(36,17,'2025-09-11 18:42:45',895000.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@@student.ptithcm.edu.vn','PENDING',25000.00,''),(37,17,'2025-09-11 19:03:30',895000.00,'0987654321','123 Đường ABC, Quận 1, TP.HCM','n22dccn067@student.ptithcm.edu.vn','COMPLETED',25000.00,''),(38,17,'2025-09-12 22:56:58',4825141.00,'0866904453','thôn lam, Xã Tân Lang, Huyện Phù Yên,  Tỉnh Sơn La','buidinhtuan04@gmail.com','COMPLETED',25000.00,'tuấn bùi');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_orders_after_update_restore_stock` AFTER UPDATE ON `orders` FOR EACH ROW BEGIN
    IF UPPER(COALESCE(OLD.status, '')) <> 'CANCELLED'
       AND UPPER(COALESCE(NEW.status, '')) = 'CANCELLED' THEN

        UPDATE product_variants pv
        JOIN order_details od ON pv.variant_id = od.variant_id
        SET pv.stock_quantity = pv.stock_quantity + od.quantity
        WHERE od.order_id = NEW.order_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `product_categories`
--

DROP TABLE IF EXISTS `product_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`category_id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `product_categories_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `product_categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_categories`
--

LOCK TABLES `product_categories` WRITE;
/*!40000 ALTER TABLE `product_categories` DISABLE KEYS */;
INSERT INTO `product_categories` VALUES (1,'Keycaps',NULL,'Keycap types'),(2,'Switches',NULL,'Mechanical switches'),(3,'Transparent Keycaps',1,'Translucent keycaps'),(4,'PBT Keycaps',1,'PBT material'),(5,'ABS Keycaps',1,'ABS material'),(6,'Linear Switches',2,'Linear feedback'),(7,'Tactile Switches',2,'Tactile feedback'),(8,'Clicky Switches',2,'Clicky feedback'),(9,'Low Profile Keycaps',1,'Low profile'),(10,'Silent Switche',1,'KeyCap Pro'),(11,'Keyboards',NULL,'Bàn phím cơ'),(12,'Silent Switche',1,'KeyCap Pro'),(14,'LinearKeyCaps',NULL,'Key'),(15,'Linear KeyCaps1',NULL,'Key'),(16,'Silent Switchessss',1,'KeyCap Pro'),(17,'Linear KeyCaps1234',NULL,'Key');
/*!40000 ALTER TABLE `product_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_tags`
--

DROP TABLE IF EXISTS `product_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_tags` (
  `product_id` int NOT NULL,
  `tag_name` varchar(255) NOT NULL,
  PRIMARY KEY (`product_id`,`tag_name`),
  KEY `tag_name` (`tag_name`),
  CONSTRAINT `product_tags_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `product_tags_ibfk_2` FOREIGN KEY (`tag_name`) REFERENCES `tags` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_tags`
--

LOCK TABLES `product_tags` WRITE;
/*!40000 ALTER TABLE `product_tags` DISABLE KEYS */;
INSERT INTO `product_tags` VALUES (2,'bestseller'),(6,'bestseller'),(1,'hot'),(8,'hot'),(5,'limited'),(1,'new'),(4,'new'),(10,'new'),(3,'sale'),(7,'sale');
/*!40000 ALTER TABLE `product_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variants`
--

DROP TABLE IF EXISTS `product_variants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variants` (
  `variant_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `color` varchar(255) DEFAULT NULL,
  `price` decimal(18,2) DEFAULT NULL,
  `stock_quantity` int DEFAULT NULL,
  `img` tinytext,
  `sku` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`variant_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_variants_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variants`
--

LOCK TABLES `product_variants` WRITE;
/*!40000 ALTER TABLE `product_variants` DISABLE KEYS */;
INSERT INTO `product_variants` VALUES (1,1,'Black-White',20.00,41,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','PUD-BW-01'),(2,2,'Red',5.00,100,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','MX-RED-01'),(3,3,'Blue',5.50,78,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','MX-BLU-01'),(4,4,'Yellow',4.50,87,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','GAT-YEL-01'),(5,5,'White',4.80,64,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','BOX-WHT-01'),(6,6,'Navy Blue',30.00,54,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','DUCKY-PBT-01'),(7,7,'Black',10.00,86,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','ABS-BLK-01'),(8,8,'Gray',25.00,37,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','LP-GRY-01'),(9,9,'Red',6.00,50,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','SIL-RED-01'),(10,10,'White',150.00,46,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','K2-WHITE'),(12,11,'Trắng',870000.00,7,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751004695/gbqvrca0vnxlslypvdsn.jpg','AK3068-WHITE'),(13,12,'Trắng',1500000.00,0,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-WHITE'),(14,12,'Đen',1550000.00,0,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-BLACK'),(15,13,'Trắng',1500000.00,0,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-WHITE'),(16,13,'Đen',1550000.00,0,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-BLACK'),(17,14,'Trắng',1500000.00,0,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-WHITE'),(18,14,'Đen',1550000.00,0,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-BLACK'),(19,15,'Trắng',1500000.00,14,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-WHITE'),(20,15,'Đen',1550000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-3098B-BLACK'),(21,16,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1750957797/kwo5juycgwenk0bkabov.jpg','AKKO-DEN'),(22,16,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1750957799/saipnmqm8kidlwpuizji.jpg','AKKO-TRANG'),(23,16,'Hồng',890000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AK3068-PINK'),(27,16,'Trắng',870000.00,15,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751005901/dskcovgwhurvwq8udzge.jpg','AK3068-WHITE'),(28,16,'Hồng',890000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751003452/ooohuutosy0ssuoah5lo.jpg','AK3068-PINK'),(30,16,'Trắng',870000.00,15,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751005026/enmtqmprfdgc1iqdhngi.jpg','AK3068-WHITE'),(31,16,'Hồng',890000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751004698/y3zqlbj52ttnnkok8u55.jpg','AK3068-PINK'),(32,16,'Hồng',890000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751005029/kjni7dcv08ynqh1qudk1.jpg','AK3068-PINK'),(33,16,'Hồng',890000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751005903/sgrz0lboy8brjf5qdsro.jpg','AK3068-PINK'),(34,17,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751123226/ft2ymw0sh4yijacs8697.jpg','AKKO-DEN'),(35,17,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751123228/tarjhe5zv1zakpkkvwiw.jpg','AKKO-TRANG'),(36,18,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751132856/bky8wn38edxrbnnrb7lz.jpg','AKKO-DEN'),(37,18,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751132858/svxqkyrhzkqedkifvtd6.jpg','AKKO-TRANG'),(38,20,'Black',10000.00,50,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757237365/tuu57achjp3beqep37wr.jpg','K2-WHITE'),(39,20,'Black',120.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751306958/h3kxk4wmfu99pareltw4.jpg','K2-BLACK-NEW'),(40,1,'Pink',160.00,30,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1750957797/kwo5juycgwenk0bkabov.jpg','K2-BLACK'),(44,21,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(45,21,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(46,20,'Pink',12000.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751307462/mgqgde9ykahrky0v6lky.jpg','K2-BLACK-NEW'),(47,20,'Pink',12000.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','K2-BLACK-NEW'),(48,20,'Pink',12000.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751482842/yej4mtbrwqnwctvggwbu.jpg','K2-BLACK-NEW'),(49,22,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(50,22,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(51,23,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(52,23,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(53,24,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(54,24,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(55,25,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(56,25,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(57,26,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(58,26,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(59,28,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(60,28,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(61,29,'Đen',1200000.00,10,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(62,29,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(63,30,'Đen',1200000.00,8,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(64,30,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(65,31,'Đen',1200000.00,9,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(66,31,'Trắng',1250000.00,1,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(67,32,'Đen',1200000.00,8,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(68,32,'Trắng',1250000.00,4,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(69,33,'Đen',1200000.00,9,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-DEN'),(70,33,'Trắng',1250000.00,5,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','AKKO-TRANG'),(71,20,'Pink',12000.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757237370/achtog7zosn0fzuqgxsl.jpg','K2-BLACK-NEW'),(72,20,'Den',1200.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757237372/lymm3s5spkt48o63bo0v.jpg','K2-BLACK-NEW'),(73,20,'Pink',12000.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757237633/m5icrcszwflxi7rpdqd8.jpg','K2-BLACK-NEW'),(74,20,'Den',1200.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757237635/txw62p2vqchywycnhgnq.jpg','K2-BLACK-NEW'),(75,20,'Pink',12000.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757237701/xrclruairsfygpfthkca.jpg','K2-BLACK-NEW'),(76,20,'Den',1200.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757237703/dciccc0jayba06ru8q19.jpg','K2-BLACK-NEW'),(77,20,'Pink',12000.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','K2-BLACK-NEW'),(78,20,'Den',1200.00,20,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349100/fiorzyqw2lxutvlqwoqm.jpg','K2-BLACK-NEW'),(79,35,'3',3000.00,1,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757644647/oyxzvtyecorwr5ouoisy.jpg','MX-BLU-01');
/*!40000 ALTER TABLE `product_variants` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_update_product_info_after_insert` AFTER INSERT ON `product_variants` FOR EACH ROW BEGIN
  UPDATE products
  SET 
    stock_quantity = IFNULL((
      SELECT SUM(stock_quantity)
      FROM product_variants
      WHERE product_id = NEW.product_id
    ), 0),
    min_price = (
      SELECT MIN(price)
      FROM product_variants
      WHERE product_id = NEW.product_id
    )
  WHERE product_id = NEW.product_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_product_variants_after_insert` AFTER INSERT ON `product_variants` FOR EACH ROW BEGIN
    -- Tính tổng stock hiện tại cho product mới (NEW.product_id)
    UPDATE products
    SET stock_quantity = (
        SELECT COALESCE(SUM(stock_quantity), 0)
        FROM product_variants
        WHERE product_id = NEW.product_id
    )
    WHERE product_id = NEW.product_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_product_variants_after_update` AFTER UPDATE ON `product_variants` FOR EACH ROW BEGIN
    -- Nếu product_id đã thay đổi, cập nhật total của product cũ
    IF OLD.product_id IS NOT NULL AND OLD.product_id <> NEW.product_id THEN
        UPDATE products
        SET stock_quantity = (
            SELECT COALESCE(SUM(stock_quantity), 0)
            FROM product_variants
            WHERE product_id = OLD.product_id
        )
        WHERE product_id = OLD.product_id;
    END IF;

    -- Luôn cập nhật tổng của product mới (trường hợp product_id không đổi cũng được)
    IF NEW.product_id IS NOT NULL THEN
        UPDATE products
        SET stock_quantity = (
            SELECT COALESCE(SUM(stock_quantity), 0)
            FROM product_variants
            WHERE product_id = NEW.product_id
        )
        WHERE product_id = NEW.product_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_product_variants_after_delete` AFTER DELETE ON `product_variants` FOR EACH ROW BEGIN
    UPDATE products
    SET stock_quantity = (
        SELECT COALESCE(SUM(stock_quantity), 0)
        FROM product_variants
        WHERE product_id = OLD.product_id
    )
    WHERE product_id = OLD.product_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `brand` varchar(255) DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  `min_price` decimal(18,2) DEFAULT NULL,
  `imgs` text,
  `description` tinytext,
  `stock_quantity` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_at` datetime DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Keychron K2','Keychron',10,20.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Phiên bản mới 2025',71,0,'2025-06-24 21:06:05',NULL),(2,'Cherry MX Red','Cherry',6,5.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Smooth linear switch',100,0,'2025-06-24 21:06:05',NULL),(3,'Cherry MX Blue','Cherry',8,5.50,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1757349096/ifbuqqohjh3pzymdxbvf.jpg','Clicky switch',78,1,'2025-06-24 21:06:05',NULL),(4,'Gateron Yellow','Gateron',6,4.50,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Smooth linear switch',87,1,'2025-06-24 21:06:05',NULL),(5,'Kailh Box White','Kailh',8,4.80,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Clicky Box switch',64,1,'2025-06-24 21:06:05',NULL),(6,'Ducky PBT Set','Ducky',4,30.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Double-shot PBT keycaps',54,1,'2025-06-24 21:06:05',NULL),(7,'OEM ABS Keycaps','Generic',5,10.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Standard ABS keycaps',86,1,'2025-06-24 21:06:05',NULL),(8,'Low Profile Set','Logitech',9,25.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Low profile keycaps',37,1,'2025-06-24 21:06:05',NULL),(9,'Silent Red','Cherry',10,6.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Silent linear switch',50,1,'2025-06-24 21:06:05',NULL),(10,'TTC Gold Pink','TTC',7,5.80,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Tactile switch',46,1,'2025-06-24 21:06:05',NULL),(11,'Bàn phím cơ AKKO 3098B','AKKO',1,2.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Bàn phím cơ 98 phím, switch AKKO, hỗ trợ Bluetooth và USB.',7,1,'2025-06-26 10:31:50',NULL),(12,'Bàn phím cơ AKKO 3098B','AKKO',1,2.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Bàn phím cơ 98 phím, switch AKKO, hỗ trợ Bluetooth và USB.',0,1,'2025-06-26 10:34:38',NULL),(13,'Bàn phím cơ AKKO 3098B','AKKO',1,1500000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Bàn phím cơ 98 phím, switch AKKO, hỗ trợ Bluetooth và USB.',0,1,'2025-06-26 10:46:58',NULL),(14,'Bàn phím cơ AKKO 3098B','AKKO',1,1500000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Bàn phím cơ 98 phím, switch AKKO, hỗ trợ Bluetooth và USB.',0,1,'2025-06-26 10:51:37',NULL),(15,'Bàn phím cơ AKKO 3098B','AKKO',1,1500000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Bàn phím cơ 98 phím, switch AKKO, hỗ trợ Bluetooth và USB.',24,1,'2025-06-26 10:55:41',NULL),(16,'Bàn phím cơ Akko 3068 v2','Akko',10,870000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1750957797/kwo5juycgwenk0bkabov.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1750957799/saipnmqm8kidlwpuizji.jpg;;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751003075/qngznboornxkisopudu8.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751003191/g5i4curj1ao7l4dbqqyi.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751003452/ooohuutosy0ssuoah5lo.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751005026/enmtqmprfdgc1iqdhngi.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751004698/y3zqlbj52ttnnkok8u55.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751005029/kjni7dcv08ynqh1qudk1.jpg','Bàn phím 68 phím, hot-swappable, switch Gateron.',70,1,'2025-06-26 17:10:00',NULL),(17,'Bàn phím cơ AULA','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751123226/ft2ymw0sh4yijacs8697.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751123228/tarjhe5zv1zakpkkvwiw.jpg','Có RGB',15,1,'2025-06-28 15:07:09',NULL),(18,'Bàn phím cơ AULA','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751132856/bky8wn38edxrbnnrb7lz.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751132858/svxqkyrhzkqedkifvtd6.jpg','Có RGB',15,1,'2025-06-28 17:47:39',NULL),(19,'Bàn phím cơ AULA','AULA',1,0.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135100/ihifywbganquclihjffx.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135102/e29pokhypqsptkg6bqlw.jpg','Có RGB',0,1,'2025-06-28 18:25:51',NULL),(20,'Keyboard Pro K2 Plus','Keychron',2,120.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135229/ce8rkuius9vr42oysne5.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135232/rkmmiu7muujiju9t2bwc.jpg','New description',290,1,'2025-06-28 18:27:20',NULL),(21,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135100/ihifywbganquclihjffx.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135102/e29pokhypqsptkg6bqlw.jpg','Có RGB',15,1,'2025-06-30 16:55:18',NULL),(22,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135100/ihifywbganquclihjffx.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135102/e29pokhypqsptkg6bqlw.jpg','Có RGB',15,1,'2025-09-05 15:27:15',NULL),(23,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135100/ihifywbganquclihjffx.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135102/e29pokhypqsptkg6bqlw.jpg','Có RGB',15,1,'2025-09-05 15:31:56',NULL),(24,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135100/ihifywbganquclihjffx.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135102/e29pokhypqsptkg6bqlw.jpg','Có RGB',15,1,'2025-09-05 15:37:37',NULL),(25,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086695/nun6b5z2x9pimxfkt5wa.jpg','Có RGB',15,1,'2025-09-05 15:38:17',NULL),(26,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757086747/rmu2tbiaabhwbavugpng.jpg','Có RGB',15,1,'2025-09-05 15:39:09',NULL),(27,'Bàn phím cơ AULA Pro','AULA',1,0.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135100/ihifywbganquclihjffx.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135102/e29pokhypqsptkg6bqlw.jpg','Có RGB',0,1,'2025-09-05 15:52:27',NULL),(28,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135100/ihifywbganquclihjffx.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1751135102/e29pokhypqsptkg6bqlw.jpg','Có RGB',15,1,'2025-09-05 15:52:51',NULL),(29,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757087677/ojubmfzbq17wwvg3u9pr.jpg','Có RGB',15,1,'2025-09-05 15:54:39',NULL),(30,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757087698/ddvmhvbus4dohehmmcsa.jpg','Có RGB',13,1,'2025-09-05 15:55:00',NULL),(31,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757166610/hh0j140hzxokqzz69x9j.jpg','Có RGB',10,1,'2025-09-06 13:50:11',NULL),(32,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757167134/e4js7qfnokac7nubmtrd.jpg','Có RGB',12,1,'2025-09-06 13:58:56',NULL),(33,'Bàn phím cơ AULA Pro','AULA',1,1200000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757167253/sqrnj0bg3fqj7lcnqnq7.jpg','Có RGB',14,1,'2025-09-06 14:00:56',NULL),(34,'Python Cơ Bản','1',7,0.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757167669/kqoyxe3bd7dbico4ugdp.jpg','<p>tt</p>',0,0,'2025-09-06 14:07:50',NULL),(35,'tuan dep trai','1',7,3000.00,'https://res.cloudinary.com/dkmpcvjki/image/upload/v1757644541/akccyjjyapddsfdnkdru.jpg;https://res.cloudinary.com/dkmpcvjki/image/upload/v1757644603/ma2oafu7y34w2nhlf0je.jpg','<p>dđ</p>',1,0,'2025-09-12 02:35:44',NULL);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tags` (
  `tag_name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` VALUES ('bestseller','Top Seller'),('hot','Hot Item'),('limited','Limited Edition'),('new','New Arrival'),('sale','Discounted');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_at` datetime DEFAULT NULL,
  `is_admin` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin123','admin@example.com','Admin User',1,'0123450000','Admin St','2025-06-24 21:05:47',NULL,1),(2,'john','123456','john@example.com','John Doe',1,'0123450001','123 A St','2025-06-24 21:05:47',NULL,0),(3,'alice','123456','alice@example.com','Alice Smith',0,'0123450002','124 A St','2025-06-24 21:05:47',NULL,0),(4,'bob','123456','bob@example.com','Bob Brown',1,'0123450003','125 A St','2025-06-24 21:05:47',NULL,0),(5,'charlie','123456','charlie@example.com','Charlie Black',1,'0123450004','126 A St','2025-06-24 21:05:47',NULL,0),(6,'david','123456','david@example.com','David White',1,'0123450005','127 A St','2025-06-24 21:05:47',NULL,0),(7,'emma','123456','emma@example.com','Emma Green',1,'0123450006','128 A St','2025-06-24 21:05:47',NULL,0),(8,'frank','123456','frank@example.com','Frank Blue',1,'0123450007','129 A St','2025-06-24 21:05:47',NULL,0),(9,'grace','123456','grace@example.com','Grace Yellow',1,'0123450008','130 A St','2025-06-24 21:05:47',NULL,0),(10,'henry','123456','henry@example.com','Henry Purple',1,'0123450009','131 A St','2025-06-24 21:05:47',NULL,0),(11,'johndoe','$2a$10$R/p1sIZTzTsjkzXaaW/JFuDFNOGiOBWSE/YO4AiNMl8BUHinDBAHG','johndoe@example.com','John Doe',1,'0123456789','Hanoi, Vietnam','2025-07-08 09:50:09',NULL,0),(12,'admin2','$2a$10$GrwtiY5xyIt98IRXn0gHne2oMMhoyJTtQUh0WsTUGaObXag3pleAi','admin2@example.com','Admin 2',1,'0123456789','Hanoi, Vietnam','2025-07-08 09:56:05',NULL,1),(13,'tuan','$2a$10$lpqo2ldaeV9uRxIx2jgr/uwSzE7bdBgfgYUYjqioDp5S6o1grdKjy','buidinhtuan04@gmail.com',NULL,1,NULL,NULL,'2025-09-10 15:14:10',NULL,0),(14,'admin2222','$2a$10$8ialdoueqFgqQWwDFEW6FerXNpGv04rGi/5iUsKUhgusw/crG1d6G','buidinhtuan0@gmail.com',NULL,1,NULL,NULL,'2025-09-10 15:19:10',NULL,0),(15,'alicexx','$2a$10$8wa7PMcOzWZB4l1HAGGCDO6k8qwPuNO6OBnC0QQ75agNvUh4MHzR.','zaisersama@shopziin.com',NULL,1,NULL,NULL,'2025-09-10 15:19:57',NULL,0),(16,'johndoe123','$2a$10$UQWeYUd47ibwD8z4eJ82m.GxOHMttuQ0OugOt0W72rSwYaBy1Dfkm','johndoe@example123.com','John Doe',1,'0123456789','Hanoi, Vietnam','2025-09-10 15:20:38',NULL,0),(17,'N22DCCN095','$2a$10$kPls8XO596ZhbUrtzhteKuPaQad2rFapqWEWusRwH7SxmW5fg3Z2a','gallos1988@caychayyy.shop',NULL,1,NULL,NULL,'2025-09-10 15:22:44',NULL,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-13  0:36:26
