-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 22-Jun-2023 às 22:46
-- Versão do servidor: 10.4.28-MariaDB
-- versão do PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `placesdb`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `comments`
--

CREATE TABLE `comments` (
  `iduser` int(11) NOT NULL,
  `idplace` int(11) NOT NULL,
  `comment` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Extraindo dados da tabela `comments`
--

INSERT INTO `comments` (`iduser`, `idplace`, `comment`) VALUES
(1, 1, 'ola'),
(1, 1, 'Mega like'),
(1, 3, 'adorei o jogo! do brasil'),
(1, 3, 'adorei o jogo! do brasil'),
(1, 3, 'adorei o jogo! do brasil'),
(1, 3, 'adorei o jogo! do brasil'),
(0, 0, ''),
(3, 4, 'bom dia');

-- --------------------------------------------------------

--
-- Estrutura da tabela `likes`
--

CREATE TABLE `likes` (
  `iduser` int(11) NOT NULL,
  `idplace` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Extraindo dados da tabela `likes`
--

INSERT INTO `likes` (`iduser`, `idplace`) VALUES
(1, 1),
(1, 4);

-- --------------------------------------------------------

--
-- Estrutura da tabela `place`
--

CREATE TABLE `place` (
  `id` int(11) NOT NULL,
  `iduser` int(11) NOT NULL,
  `name` varchar(250) DEFAULT NULL,
  `address` varchar(250) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `description` text DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Extraindo dados da tabela `place`
--

INSERT INTO `place` (`id`, `iduser`, `name`, `address`, `latitude`, `longitude`, `description`, `image`, `type`) VALUES
(1, 1, 'Torre de Belem', 'Avenida Brasília', 38.6916, -9.216, 'A torre de belem tem uma descrição', 'torre.jpg', NULL),
(3, 2, 'Estadio do Sporting', 'Rua Professor Fernando da Fonseca, 1501-806 Lisboa', 38.7612, -9.1618, 'O estadio do sporting tem uma descrição', 'estadio1.jpg', NULL),
(4, 0, 'Fundação Calouste Gulbenkian', 'Avenida de Berna', 38.737411, -9.154713, 'The foundation has a museum whose collections feature all the major phases of Eastern and Western art - Egyptian scarabs the Art Nouveau jewelry, Islamic textiles to impressionist paintings.\r\n\r\nIn a separate building, opposite the park, the Modern Art Center focuses much of Portuguese works, most of the twentieth century. All major museum exhibitions have been acquired by one man, oil tycoon Calouste Gulbenkian.\r\n\r\nToday the Gulbenkian Foundation directs an orchestra, three concert halls and two galleries. Funds the work in all spheres of the Portuguese cultural life - and sponsors a wide range of projects.\r\n\r\nYou can also reach the main entrance of the complex, in Avenida de Berna, by bus 731 or 746 from Restauradores or by the São Sebastião subway.', 'gul.jpg', NULL),
(6, 1, 'Sé de Braga', 'Rua Dom Afonso Henriques 105 Braga 4700', 4.5499, -8.4273, 'This is the cathedral of the city of Braga.', 'sedebraga.jpg', NULL),
(7, 1, 'Museu Nacional de Beja', 'Largo da Conceição Beja 7800-131', 38.0142, -7.8631, 'The Queen Leonor Museum occupies three buildings on a broad plaza in the center of Beja: the Convento da Conceição and the churches of Santo Amaro and São Sebastião', 'museudebeja.jpg', NULL),
(8, 1, 'Sé de Viseu', 'Adro Sé, 3500-195 Viseu', 40.6605, -7.9109, 'The Cathedral or Cathedral of Viseu, also known as the Parish Church of Santa Maria or Church of Nossa Senhora da Assunção, is a Christian cathedral located in the city, municipality and district of Viseu, Portugal. The Cathedral of Viseu has been classified as a National Monument since 1910.', 'sedeviseu.jpg', NULL),
(14, 0, 'Museu Cosme Damião', ' Av. Eusébio da Silva Ferreira Porta 9, 1500-313 Lisboa', 38.7515, -9.1843, 'This is the museum of Sport Lisboa e Benfica', 'museucosmedamiao.jpg', NULL),
(15, 1, 'Mosteiro dos Jerónimos', 'Praça do Império 1400-206 Lisboa', 38.698, -9.2066, 'The Monastery of Santa Maria de Belém, better known as the Jerónimos Monastery, is a Portuguese monastery, ordered built at the end of the 15th century by King Manuel I and given to the Order of St. Jerome. It is located in the parish of Belém', 'mosteirojeronimos.jpg', NULL),
(16, 1, 'Estádio da luz', 'Avenida Eusebio da Silva Ferreira, Lisboa', 38.2777, -9.1845, 'Stadium of Sport Lisboa e Benfica.', 'estadiodoglorioso.jpg', NULL),
(17, 1, 'Estádio do Dragão', 'Via Futebol Clube do Porto, 4350-415 Porto', 41.1618, -8.5836, 'FC Porto Stadium.', 'dragao.jpg', NULL);

-- --------------------------------------------------------

--
-- Estrutura da tabela `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL,
  `extra` varchar(1000) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Extraindo dados da tabela `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `extra`) VALUES
(1, 'test1', '673cafb10f8a6bc0ea17c7775a873d73', 'test1@test.com', 'extra info'),
(2, 'test2', '603b1e3c00cf73662118bed04424400e', 'test2@test.com', 'bla bla'),
(3, 'test3', '6100889136a0e309e6ee0f1a6ee359c7', 'test3@email.com', ''),
(4, 'test4', '4bb1dfe2acfdf896b1543bd908b4756f', 'test@test.com', ''),
(5, 'test5', 'a2133fc73ff59b3082190b4e8f4ce151', 'test5@email.com', ''),
(6, 'test6', '7eb53a35626ae4f6732cfb67939400b4', 'test6@test.com', ''),
(7, 'dias', 'd1d2e39901dd00c654e6877ee4d2c1a6', 'filipe0ie3q0oeq0o@gmail.com', ''),
(8, 'rodrigo', 'd1d2e39901dd00c654e6877ee4d2c1a6', 'r@gmail.com', ''),
(9, 'bom diaq', 'd1d2e39901dd00c654e6877ee4d2c1a6', 'b@gmail.com', ''),
(10, 'ruben', 'd1d2e39901dd00c654e6877ee4d2c1a6', 'ruben@gmail.com', '');

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `likes`
--
ALTER TABLE `likes`
  ADD PRIMARY KEY (`iduser`,`idplace`);

--
-- Índices para tabela `place`
--
ALTER TABLE `place`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT de tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `place`
--
ALTER TABLE `place`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de tabela `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
