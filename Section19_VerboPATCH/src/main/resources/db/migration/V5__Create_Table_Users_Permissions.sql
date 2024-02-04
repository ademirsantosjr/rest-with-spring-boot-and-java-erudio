CREATE TABLE IF NOT EXISTS `users_permissions` (
  `id_user` bigint(20) NOT NULL,
  `id_permission` bigint(20) NOT NULL,
  PRIMARY KEY (`id_user`,`id_permission`),
  KEY `fk_user_permission_permission` (`id_permission`),
  CONSTRAINT `fk_users_permissions` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`),
  CONSTRAINT `fk_users_permissions_permissions` FOREIGN KEY (`id_permission`) REFERENCES `permissions` (`id`)
) ENGINE=InnoDB;