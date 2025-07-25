-- MySQL dump 10.13  Distrib 5.7.44, for Linux (x86_64)
--
-- Host: localhost    Database: erp
-- ------------------------------------------------------
-- Server version	5.7.44

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
-- Table structure for table `processos`
--

DROP TABLE IF EXISTS `processos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `processos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `setor` varchar(255) NOT NULL,
  `arquivo` varchar(255) NOT NULL,
  `data_upload` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `processos`
--

LOCK TABLES `processos` WRITE;
/*!40000 ALTER TABLE `processos` DISABLE KEYS */;
INSERT INTO `processos` VALUES (3,'PRC-001','Diagn├â┬│stico Empresarial','proc_687aa483c59a28.71153389.pdf','2025-07-18 19:46:11'),
(4,'PRC-002','Diagn├â┬│stico Empresarial','proc_687aa501c66839.11609576.pdf','2025-07-18 19:48:17'),
(5,'PRC-003','Diagn├â┬│stico Empresarial','proc_687aa50f307e00.21208606.pdf','2025-07-18 19:48:31'),
(6,'PRC-004','Estrat├â┬®gica e Planejamento','proc_687aa51fb83274.73168867.pdf','2025-07-18 19:48:47'),
(7,'PRC-005','Estrat├â┬®gica e Planejamento','proc_687aa7c9510655.34191892.pdf','2025-07-18 20:00:09'),
(8,'PRC-006','Processos e Opera├â┬º├â┬Áes','proc_687aa7e0ea7b60.50083703.pdf','2025-07-18 20:00:32'),
(9,'PRC-007','Processos e Opera├â┬º├â┬Áes','proc_687aa7f37e06c2.35809843.pdf','2025-07-18 20:00:51'),
(10,'PRC-008','Financeira e Controladoria','proc_687aa8034e8226.19616982.pdf','2025-07-18 20:01:07'),
(11,'PRC-009','Financeira e Controladoria','proc_687aa812ee4865.37849817.pdf','2025-07-18 20:01:22'),
(12,'PRC-010','Recursos Humanos','proc_687aa8289692d1.47041897.pdf','2025-07-18 20:01:44'),
(13,'PRC-011','Recursos Humanos','proc_687aa83982a622.40211404.pdf','2025-07-18 20:02:01'),
(14,'PRC-012','Marketing e Posicionamento','proc_687aa84494bee8.67664309.pdf','2025-07-18 20:02:12'),
(15,'PRC-013','Marketing e Posicionamento','proc_687aa853706cc0.28756483.pdf','2025-07-18 20:02:27'),
(16,'PRC-014','Suporte e Relacionamento c/ Cliente','proc_687aa861e806e3.63014136.pdf','2025-07-18 20:02:41'),
(17,'PRC-015','Suporte e Relacionamento c/ Cliente','proc_687aa86cb15344.52158267.pdf','2025-07-18 20:02:52');
/*!40000 ALTER TABLE `processos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-18 17:04:08
