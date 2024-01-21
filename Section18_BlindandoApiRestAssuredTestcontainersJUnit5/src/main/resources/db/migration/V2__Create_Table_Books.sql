CREATE TABLE `books` (
  `id` int(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `author` longtext NOT NULL,
  `title` longtext NOT NULL,
  `launch_date` datetime(6) NOT NULL,
  `price` decimal(65,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;