-- phpMyAdmin SQL Dump
-- version 3.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Sep 29, 2013 at 03:53 PM
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

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE IF NOT EXISTS `book` (
  `ISBN` int(11) NOT NULL,
  `Title` varchar(64) NOT NULL,
  `Author` varchar(32) NOT NULL,
  `Edition` int(11) NOT NULL,
  PRIMARY KEY (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `copy`
--

DROP TABLE IF EXISTS `copy`;
CREATE TABLE IF NOT EXISTS `copy` (
  `CopyID` int(11) NOT NULL,
  `LendingPeriod` int(11) NOT NULL,
  `BookISBN` int(11) NOT NULL,
  PRIMARY KEY (`CopyID`),
  KEY `BookISBN` (`BookISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
CREATE TABLE IF NOT EXISTS `loan` (
  `ReturnDate` date NOT NULL,
  `MembershipNr` int(11) NOT NULL,
  `CopyID` int(11) NOT NULL,
  PRIMARY KEY (`ReturnDate`,`MembershipNr`,`CopyID`),
  KEY `MembershipNr` (`MembershipNr`),
  KEY `CopyID` (`CopyID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE IF NOT EXISTS `member` (
  `MembershipNumber` int(11) NOT NULL,
  `FirstName` varchar(32) NOT NULL,
  `LastName` varchar(32) NOT NULL,
  `Street` varchar(32) NOT NULL,
  `HouseNumber` varchar(4) NOT NULL,
  `City` varchar(32) NOT NULL,
  `PhoneNumber` varchar(16) NOT NULL,
  `EmailAddress` varchar(32) NOT NULL,
  `Fine` double NOT NULL,
  PRIMARY KEY (`MembershipNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `ReservationDate` date NOT NULL,
  `MembershipNumber` int(11) NOT NULL,
  `BookISBN` int(11) NOT NULL,
  PRIMARY KEY (`ReservationDate`,`MembershipNumber`,`BookISBN`),
  KEY `MembershipNumber` (`MembershipNumber`),
  KEY `BookISBN` (`BookISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `copy`
--
ALTER TABLE `copy`
  ADD CONSTRAINT `copy_ibfk_1` FOREIGN KEY (`BookISBN`) REFERENCES `book` (`ISBN`) ON UPDATE CASCADE;

--
-- Constraints for table `loan`
--
ALTER TABLE `loan`
  ADD CONSTRAINT `loan_ibfk_1` FOREIGN KEY (`MembershipNr`) REFERENCES `member` (`MembershipNumber`) ON UPDATE CASCADE,
  ADD CONSTRAINT `loan_ibfk_2` FOREIGN KEY (`CopyID`) REFERENCES `copy` (`CopyID`) ON UPDATE CASCADE;

--
-- Constraints for table `reservation`
--
ALTER TABLE `reservation`
  ADD CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`MembershipNumber`) REFERENCES `member` (`MembershipNumber`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reservation_ibfk_2` FOREIGN KEY (`BookISBN`) REFERENCES `book` (`ISBN`) ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
