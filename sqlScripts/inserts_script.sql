INSERT INTO user VALUES(NULL, 'user1', '111111111-11', 'user1@gmail.com', 'user1Password', '2005-08-10');
INSERT INTO user VALUES(NULL, 'user2', '222222222-22', 'user2@gmail.com', 'user2Password','2002-10-10');
INSERT INTO user VALUES(NULL, 'user3', '333333333-33','user3@gmail.com', 'user3Password','2004-07-23');
INSERT INTO user VALUES(NULL, 'user4', '444444444-44','user4@gmail.com', 'user4Password', '2003-04-3');

INSERT INTO state VALUES(NULL, 'SP');
INSERT INTO state VALUES(NULL, 'RJ');
INSERT INTO state VALUES(NULL, 'RS');

INSERT INTO city VALUES(NULL, 'city1', 1);
INSERT INTO city VALUES(NULL, 'city2', 1);
INSERT INTO city VALUES(NULL, 'city3', 2);
INSERT INTO city VALUES(NULL, 'city4', 3);

INSERT INTO address VALUES(NULL, 'neighborhood1', '1', '111111-111', 'street1', 'complement1', 1, 1);
INSERT INTO address VALUES(NULL, 'neighborhood2', '2', '222222-222', 'street2', 'complement2', 1, 2);
INSERT INTO address VALUES(NULL, 'neighborhood3', '3', '333333-333', 'street3', 'complement3', 2, 2);
INSERT INTO address VALUES(NULL, 'neighborhood4', '4', '444444-444', 'street4', 'complement4', 2, 3);
INSERT INTO address VALUES(NULL, 'neighborhood5', '5', '555555-555', 'street5', 'complement5', 3, 4);
INSERT INTO address VALUES(NULL, 'neighborhood6', '6', '666666-666', 'street6', 'complement6', 3, 2);

INSERT INTO rate VALUES(NULL, 2, 0);
INSERT INTO rate VALUES(NULL, 1, 1);
INSERT INTO rate VALUES(NULL, 0, 1);
INSERT INTO rate VALUES(NULL, 1, 0);
INSERT INTO rate VALUES(NULL, 0, 0);
INSERT INTO rate VALUES(NULL, 1, 0);
INSERT INTO rate VALUES(NULL, 0, 1);	
INSERT INTO rate VALUES(NULL, 0, 0);
INSERT INTO rate VALUES(NULL, 0, 0);
INSERT INTO rate VALUES(NULL, 0, 0);

INSERT INTO product VALUES(NULL, 'product1',  50.0, 'product 1 description', 20, 1, 1);
INSERT INTO product VALUES(NULL, 'product2',  50.0, 'product 2 description', 15, 1, 2);
INSERT INTO product VALUES(NULL, 'product3',  20.0, 'product 3 description', 5, 2, 3);

INSERT INTO category VALUES(NULL, 'category1');
INSERT INTO category VALUES(NULL, 'category2');
INSERT INTO category VALUES(NULL, 'category3');
INSERT INTO category VALUES(NULL, 'category4');
INSERT INTO category VALUES(NULL, 'category5');
INSERT INTO category VALUES(NULL, 'category6');

INSERT INTO aux_product_category VALUES(1, 1);
INSERT INTO aux_product_category VALUES(1, 2);
INSERT INTO aux_product_category VALUES(1, 5);
INSERT INTO aux_product_category VALUES(2, 3);
INSERT INTO aux_product_category VALUES(3, 4);
INSERT INTO aux_product_category VALUES(3, 2);

INSERT INTO comments VALUES(NULL, 'comment1', 3, 1, 4, NULL);
INSERT INTO comments VALUES(NULL, 'comment 1 reply', 1, 1, 5, 1);
INSERT INTO comments VALUES(NULL, 'comment2', 2, 1, 6, NULL);
INSERT INTO comments VALUES(NULL, 'comment 2 reply', 1, 1, 7, 3);
INSERT INTO comments VALUES(NULL, 'comment3', 3, 3, 8, NULL);
INSERT INTO comments VALUES(NULL, 'comment 1 reply reply', 3, 1, 9, 1);

INSERT INTO orderStatus values(1, 'PENDENT');
INSERT INTO orderStatus values(2, 'PAID');
INSERT INTO orderStatus values(3, 'SHIPPING');
INSERT INTO orderStatus values(4, 'DELIVERED');
INSERT INTO orderStatus values(5, 'CANCELED');

INSERT INTO `order` VALUES(NULL, '2019-08-15', 90.00, 3, 4, 5);
INSERT INTO `order` VALUES(NULL, '2019-10-28', 50.00, 3, 4, 5);
INSERT INTO `order` VALUES(NULL, '2020-01-15', 300.00, 2, 3, 3);

INSERT INTO orderItem VALUES(NULL, 50.00, 1, 1, 1);
INSERT INTO orderItem VALUES(NULL, 40.00, 2, 3, 1);
INSERT INTO orderItem VALUES(NULL, 50.00, 1 ,2, 2);
INSERT INTO orderItem VALUES(NULL, 200.00, 4 ,1, 3);
INSERT INTO orderItem VALUES(NULL, 100.00, 2 ,2, 3);

INSERT INTO aux_rate_user VALUES(1, 2, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(1, 3, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(3, 1, FALSE, TRUE);
INSERT INTO aux_rate_user VALUES(2, 3, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(1, 4, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(1, 7, FALSE, TRUE);
INSERT INTO aux_rate_user VALUES(2, 7, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(3, 8, TRUE, FALSE);