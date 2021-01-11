DELIMITER $
CREATE PROCEDURE user_report(fromDate DATE, toDate DATE, id INT)
	BEGIN
        DROP TABLE IF EXISTS user_report;
        CREATE TABLE user_report (
			monthYear VARCHAR(50),
            quantitySold INT,
            totalEarned FLOAT(2)
        );
        
        WHILE fromDate <= toDate DO
			SET @quantitySold = (SELECT SUM(`oi`.quantity) 
							FROM user `u` 
                            INNER JOIN product `p` ON `u`.userId = `p`.userId
                            INNER JOIN orderItem `oi` ON `p`.productId = `oi`.productId
                            INNER JOIN `order` `o` ON `o`.orderId = `oi`.orderId
                            WHERE `u`.userId = id AND DATE_FORMAT(`o`.`date`, '%m%Y') = DATE_FORMAT(fromDate, '%m%Y'));
			
            SET @totalEarned = (SELECT SUM(`oi`.subTotal) 
							FROM user `u` 
                            INNER JOIN product `p` ON `u`.userId = `p`.userId
                            INNER JOIN orderItem `oi` ON `p`.productId = `oi`.productId 
                            INNER JOIN `order` `o` ON `o`.orderId = `oi`.orderId 
							WHERE `u`.userId = id AND DATE_FORMAT(`o`.`date`, '%m%Y') = DATE_FORMAT(fromDate, '%m%Y'));
            IF @quantitySold IS NOT NULL AND @totalEarned IS NOT NULL THEN                
				INSERT INTO user_report VALUES(DATE_FORMAT(fromDate, '%m-%Y'), @quantitySold, @totalEarned);
            END IF;    
            SET fromDate = DATE_ADD(fromDate, INTERVAL 1 MONTH);
       END WHILE; 
       
       SET @folder = 'C:/ProgramData/MySQL/MySQL Server 8.0/Uploads/';
       SET @fileName = CONCAT('user', id, '-',DATE_FORMAT(NOW(), '%d%m%y'));
       SET @fileExtension = '.csv';
       
       SET @CMD = CONCAT("(SELECT 'Month and Year', 'Quantity sold by month', 'Total earned by month', 'Total sold', 'Total earned')
       UNION
       SELECT monthYear AS `Month and Year`, quantitySold AS `Quantity sold by month`, totalEarned AS `Total earned by month`,
		      s.`Total sold`, s.`Total earned`
		FROM (SELECT SUM(quantitySold) AS `Total sold`, SUM(totalEarned) AS `Total earned`
				FROM user_report) AS `s`, user_report
		INTO OUTFILE '", @folder,@fileName,@fileExtension,
        "' FIELDS ENCLOSED BY '\"'
        TERMINATED BY ';'
        ESCAPED BY '\"'
        LINES TERMINATED BY '\r\n';");
      
      PREPARE statement FROM @CMD;
      EXECUTE statement;
      
	END$
DELIMITER ;