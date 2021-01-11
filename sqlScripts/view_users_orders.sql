CREATE VIEW view_users_orders 
	AS
		SELECT `u`.userId, `u`.userName, `u`.cpf, `u`.email, 
			   `o`.orderId, `o`.date, `o`.total, 
               `os`.`status`
        FROM `user` `u`
        INNER JOIN `order` `o` ON `u`.userId = `o`.userId 
		INNER JOIN orderStatus `os` ON `o`.orderStatusId = `os`.orderStatusId;