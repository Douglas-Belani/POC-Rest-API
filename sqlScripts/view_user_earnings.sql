CREATE VIEW view_user_earnings AS 
	SELECT `u`.userId, `u`.userName, `u`.cpf, `u`.email,TRUNCATE(SUM(`oi`.subTotal), 2) AS `total earned`,
		   SUM(`oi`.quantity) AS `total quantity sold`, COUNT(`oi`.orderItemId) AS `total orders`,
           TRUNCATE(SUM(`oi`.subTotal)/ COUNT(`oi`.orderItemId), 2) AS `avarage earned by order`
    FROM `user` `u` 
    INNER JOIN product `p` ON `u`.userId = `p`.userId
    INNER JOIN orderitem `oi` ON `p`.productId = `oi`.productId
    GROUP BY `u`.userId;