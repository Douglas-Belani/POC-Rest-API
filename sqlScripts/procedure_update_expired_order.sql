DELIMITER $
CREATE PROCEDURE update_expired_order()
	BEGIN
    
    SET @orderStatusCanceledId = (SELECT orderStatusId FROM orderStatus WHERE status = 'CANCELED');
    SET @orderStatusPendentId = (SELECT orderStatusId FROM orderStatus WHERE status = 'PENDENT');
		UPDATE `order`
			SET orderStatusId = @orderStatusCanceledId
            WHERE orderStatusId = @orderStatusPendentId AND DATEDIFF(NOW(), `date`) > 7;
    END $
DELIMITER ;