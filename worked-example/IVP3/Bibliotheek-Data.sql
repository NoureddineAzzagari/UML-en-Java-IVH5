-- phpMyAdmin SQL Dump
-- version 3.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 29, 2013 at 03:54 PM
-- Server version: 5.5.25a
-- PHP Version: 5.4.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `library`
--

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`ISBN`, `Title`, `Author`, `Edition`) VALUES
(1111, 'Het leven is vurrukkulluk', 'Remco Campert', 1),
(2222, 'De Ontdekking van de Hemel', 'Harry Mulisch', 5);

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`MembershipNumber`, `FirstName`, `LastName`, `Street`, `HouseNumber`, `City`, `PhoneNumber`, `EmailAddress`, `Fine`) VALUES
(1000, 'Pascal', 'van Gastel', 'Lovensdijkstraat', '61', 'Breda', '0765238754', 'ppth.vangastel@avans.nl', 0),
(1001, 'Erco', 'Argante', 'Hogeschoollaan', '1', 'Breda', '0765231234', 'e.argante@avans.nl', 0),
(1002, 'Marice', 'Bastiaensen', 'Lovensdijkstraat', '63', 'Breda', '0765236789', 'mmcm.bastiaensen@avans.nl', 5);

--
-- Dumping data for table `copy`
--

INSERT INTO `copy` (`CopyID`, `LendingPeriod`, `BookISBN`) VALUES
(10001, 5, 2222),
(10002, 21, 1111);

--
-- Dumping data for table `loan`
--

INSERT INTO `loan` (`ReturnDate`, `MembershipNr`, `CopyID`) VALUES
('2013-10-16', 1000, 10001);


--
-- Dumping data for table `reservation`
--

INSERT INTO `reservation` (`ReservationDate`, `MembershipNumber`, `BookISBN`) VALUES
('2013-09-29', 1001, 1111);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
