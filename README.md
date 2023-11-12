# Hotel Management Project

## Introduction
Bu proje, otel yönetimi için geliştirilmiş bir sistemdir.

## Tables

## Hotel Table

### Table Creation
```sql
CREATE TABLE `hotels` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `city` varchar(100) NOT NULL,
  `region` varchar(100) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `star` varchar(11) DEFAULT NULL,
  `freePark` tinyint(1) NOT NULL,
  `freeWifi` tinyint(1) NOT NULL,
  `swimmingPool` tinyint(1) NOT NULL,
  `fitnessCenter` tinyint(1) NOT NULL,
  `hotelConcierge` tinyint(1) NOT NULL,
  `spa` tinyint(1) NOT NULL,
  `roomService` tinyint(1) NOT NULL,
  `ultraAllInclusive` tinyint(1) NOT NULL,
  `allInclusive` tinyint(1) NOT NULL,
  `bedAndBreakfast` tinyint(1) NOT NULL,
  `fullBoard` tinyint(1) NOT NULL,
  `halfBoard` tinyint(1) NOT NULL,
  `roomOnly` tinyint(1) NOT NULL,
  `nonAlcoholFull` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```
### Sample Data Insertion for Hotel Table
```sql
INSERT INTO `hotels` (`id`, `name`, `city`, `region`, `address`, `email`, `phone`, `star`, `freePark`, `freeWifi`, `swimmingPool`, `fitnessCenter`, `hotelConcierge`, `spa`, `roomService`, `ultraAllInclusive`, `allInclusive`, `bedAndBreakfast`, `fullBoard`, `halfBoard`, `roomOnly`, `nonAlcoholFull`) VALUES
(1, 'Rixos Premium Belek - The Land Of Legends Access', 'Antalya', 'Belek', 'Belek, Ileribaşı Mevkii, 07500 Serik/Antalya', 'rixos@rixos.com', '02427102000', '7', 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0),
(2, 'The Land Of Legends Access', 'Antalya', 'Belek', 'Belek, Ileribaşı Mevkii, 07500 Serik/Antalya', 'rixos.com', '46656564', '3', 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0),
```
