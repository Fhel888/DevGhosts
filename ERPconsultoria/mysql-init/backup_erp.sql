-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Tempo de geração: 08/07/2025 às 22:01
-- Versão do servidor: 5.7.44
-- Versão do PHP: 8.2.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `erp`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `processos`
--

CREATE TABLE `processos` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) NOT NULL,
  `setor` varchar(255) NOT NULL,
  `arquivo` varchar(255) NOT NULL,
  `data_upload` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Despejando dados para a tabela `processos`
--

INSERT INTO `processos` (`id`, `nome`, `setor`, `arquivo`, `data_upload`) VALUES
(1, 'PRC-001', 'DiagnÃ³stico Empresarial', 'PRC-001_Empresa_Alfa_Ltda.pdf', '2025-07-08 21:07:14'),
(4, 'PRC-002', 'DiagnÃ³stico Empresarial', 'proc_686d89f7c33a91.70449536.pdf', '2025-07-08 21:13:27'),
(5, 'PRC-004', 'EstratÃ©gica e Planejamento', 'proc_686d8a56b28839.66013584.pdf', '2025-07-08 21:15:02');

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `processos`
--
ALTER TABLE `processos`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `processos`
--
ALTER TABLE `processos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
