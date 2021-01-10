DROP VIEW IF EXISTS sellerUser;
DROP TABLE IF EXISTS orderItem;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS orderStatus;
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS city;
DROP TABLE IF EXISTS state;
DROP TABLE IF EXISTS aux_product_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS aux_rate_comments;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS aux_rate_product;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS aux_rate_user;
DROP TABLE IF EXISTS rate;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`(
userId INT PRIMARY KEY AUTO_INCREMENT, 
userName VARCHAR(120) NOT NULL,
cpf VARCHAR(12) UNIQUE NOT NULL,
email VARCHAR(160) UNIQUE NOT NULL,
password VARCHAR(200) NOT NULL, 
birthDate DATE NOT NULL
); 

INSERT INTO user VALUES(NULL, 'user1', '111111111-11', 'user1@gmail.com', 'user1Password', '2005-08-10');
INSERT INTO user VALUES(NULL, 'user2', '222222222-22', 'user2@gmail.com', 'user2Password','2002-10-10');
INSERT INTO user VALUES(NULL, 'user3', '333333333-33','user3@gmail.com', 'user3Password','2004-07-23');
INSERT INTO user VALUES(NULL, 'user4', '444444444-44','user4@gmail.com', 'user4Password', '2003-04-3');

CREATE TABLE state (
stateId INT PRIMARY KEY AUTO_INCREMENT,
initials CHAR(2) UNIQUE NOT NULL
);

INSERT INTO state VALUES(NULL, 'SP');
INSERT INTO state VALUES(NULL, 'RJ');
INSERT INTO state VALUES(NULL, 'RS');

CREATE TABLE city(
cityId INT PRIMARY KEY AUTO_INCREMENT,
cityName VARCHAR(120),
stateId INT NOT NULL
);

ALTER TABLE city ADD CONSTRAINT CITY_STATE_FK
FOREIGN KEY (stateId) REFERENCES state(stateId);

INSERT INTO city VALUES(NULL, 'city1', 1);
INSERT INTO city VALUES(NULL, 'city2', 1);
INSERT INTO city VALUES(NULL, 'city3', 2);
INSERT INTO city VALUES(NULL, 'city4', 3);

CREATE TABLE address(
addressId INT PRIMARY KEY AUTO_INCREMENT,
neighborhood VARCHAR(120) NOT NULL,
number VARCHAR(8) NOT NULL,
zipCode VARCHAR(10) NOT NULL,
street VARCHAR(120) NOT NULL,
complement VARCHAR(120),
userId INT NOT NULL,
cityId INT NOT NULL
);

ALTER TABLE address ADD CONSTRAINT ADDRESS_USER_FK
FOREIGN KEY (userId) REFERENCES user(userId);

ALTER TABLE address ADD CONSTRAINT ADDRESS_CITY_FK
FOREIGN KEY (cityId) REFERENCES city(cityId);

INSERT INTO address VALUES(NULL, 'neighborhood1', '1', '111111-111', 'street1', 'complement1', 1, 1);
INSERT INTO address VALUES(NULL, 'neighborhood2', '2', '222222-222', 'street2', 'complement2', 1, 2);
INSERT INTO address VALUES(NULL, 'neighborhood3', '3', '333333-333', 'street3', 'complement3', 2, 2);
INSERT INTO address VALUES(NULL, 'neighborhood4', '4', '444444-444', 'street4', 'complement4', 2, 3);
INSERT INTO address VALUES(NULL, 'neighborhood5', '5', '555555-555', 'street5', 'complement5', 3, 4);
INSERT INTO address VALUES(NULL, 'neighborhood6', '6', '666666-666', 'street6', 'complement6', 3, 2);

CREATE TABLE rate(
rateId INT PRIMARY KEY AUTO_INCREMENT,
upvotes INT NOT NULL,
downvotes INT NOT NULL
);

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

CREATE TABLE product(
productId INT PRIMARY KEY AUTO_INCREMENT,
productName VARCHAR(120) NOT NULL,
price FLOAT(2) NOT NULL,
description VARCHAR(140) NOT NULL,
stock INT NOT NULL,
userId INT NOT NULL,
rateId INT NOT NULL UNIQUE
);

ALTER TABLE product ADD CONSTRAINT PRODUCT_USER_FK 
FOREIGN KEY (userId) REFERENCES user(userId);

ALTER TABLE product ADD CONSTRAINT PRODUCT_RATE_FK
FOREIGN KEY (rateId) REFERENCES rate(rateId);

INSERT INTO product VALUES(NULL, 'product1',  50.0, 'product 1 description', 20, 1, 1);
INSERT INTO product VALUES(NULL, 'product2',  50.0, 'product 2 description', 15, 1, 2);
INSERT INTO product VALUES(NULL, 'product3',  20.0, 'product 3 description', 5, 2, 3);

CREATE TABLE category(
categoryId INT PRIMARY KEY AUTO_INCREMENT,
categoryName VARCHAR(120)
);

INSERT INTO category VALUES(NULL, 'category1');
INSERT INTO category VALUES(NULL, 'category2');
INSERT INTO category VALUES(NULL, 'category3');
INSERT INTO category VALUES(NULL, 'category4');
INSERT INTO category VALUES(NULL, 'category5');
INSERT INTO category VALUES(NULL, 'category6');

CREATE TABLE aux_product_category (
productId INT,
categoryId INT,
PRIMARY KEY (productId, categoryId)
);

ALTER TABLE aux_product_category ADD CONSTRAINT AUX_PRODUCT_FK
FOREIGN KEY (productId) REFERENCES product(productId);

ALTER TABLE aux_product_category ADD CONSTRAINT AUX_CATEGORY_FK
FOREIGN KEY (categoryId) REFERENCES category(categoryId);

INSERT INTO aux_product_category VALUES(1, 1);
INSERT INTO aux_product_category VALUES(1, 2);
INSERT INTO aux_product_category VALUES(1, 5);
INSERT INTO aux_product_category VALUES(2, 3);
INSERT INTO aux_product_category VALUES(3, 4);
INSERT INTO aux_product_category VALUES(3, 2);

CREATE TABLE comments(
commentId INT PRIMARY KEY AUTO_INCREMENT,
text VARCHAR(200) NOT NULL,
userId INT NOT NULL,
productId INT NOT NULL,
rateId INT UNIQUE,
topLevelCommentId INT
);

ALTER TABLE comments ADD CONSTRAINT `COMMENTS_USER_FK`
FOREIGN KEY (userId) REFERENCES user(userId);

ALTER TABLE comments ADD CONSTRAINT `COMMENTS_PRODUCT_FK`
FOREIGN KEY (productId) REFERENCES product(productId);

ALTER TABLE comments ADD CONSTRAINT `COMMENTS_RATE_FK`
FOREIGN KEY (rateId) REFERENCES rate(rateId);

ALTER TABLE comments ADD CONSTRAINT `TOP_LEVEL_COMMENT_ID_COMMENT_FK`
FOREIGN KEY (topLevelCommentId) REFERENCES comments(commentId);

INSERT INTO comments VALUES(NULL, 'comment1', 3, 1, 4, NULL);
INSERT INTO comments VALUES(NULL, 'comment 1 reply', 1, 1, 5, 1);

INSERT INTO comments VALUES(NULL, 'comment2', 2, 1, 6, NULL);
INSERT INTO comments VALUES(NULL, 'comment 2 reply', 1, 1, 7, 3);

INSERT INTO comments VALUES(NULL, 'comment3', 3, 3, 8, NULL);

INSERT INTO comments VALUES(NULL, 'comment 1 reply reply', 3, 1, 9, 1);

CREATE TABLE orderStatus(	
orderStatusId INT PRIMARY KEY,
status VARCHAR(120) NOT NULL UNIQUE
);

INSERT INTO orderStatus values(1, 'PENDENT');
INSERT INTO orderStatus values(2, 'PAID');
INSERT INTO orderStatus values(3, 'SHIPPING');
INSERT INTO orderStatus values(4, 'DELIVERED');
INSERT INTO orderStatus values(5, 'CANCELED');

CREATE TABLE `order`(
orderId INT PRIMARY KEY AUTO_INCREMENT,
`date` DATE NOT NULL,
total FLOAT(2),
userId INT NOT NULL,
orderStatusId INT NOT NULL,
addressId INT NOT NULL
);	

ALTER TABLE `order` ADD CONSTRAINT ORDER_USER_FK
FOREIGN KEY (userId) REFERENCES user(userId);

ALTER TABLE `order` ADD CONSTRAINT ORDER_ORDER_STATUS_FK
FOREIGN KEY (orderStatusId) REFERENCES orderStatus(orderStatusId);

ALTER TABLE `order` ADD CONSTRAINT ORDER_ADDRESS_FK
FOREIGN KEY (addressId) REFERENCES address(addressId);

INSERT INTO `order` VALUES(NULL, '2019-08-15', 90.00, 3, 4, 5);
INSERT INTO `order` VALUES(NULL, '2019-10-28', 50.00, 3, 4, 5);
INSERT INTO `order` VALUES(NULL, '2020-01-15', 300.00, 2, 3, 3);

CREATE TABLE orderItem(
orderItemId INT PRIMARY KEY AUTO_INCREMENT,
subTotal FLOAT(2),
quantity INT NOT NULL,
productId INT NOT NULL,
orderId INT NOT NULL
);

ALTER TABLE orderItem ADD CONSTRAINT ORDER_ITEM_PRODUCT_FK
FOREIGN KEY (productId) REFERENCES product(productId);

ALTER TABLE orderItem ADD CONSTRAINT ORDER_ITEM_ORDER_FK 
FOREIGN KEY (orderId) REFERENCES `order`(orderId);

INSERT INTO orderItem VALUES(NULL, 50.00, 1, 1, 1);
INSERT INTO orderItem VALUES(NULL, 40.00, 2, 3, 1);
INSERT INTO orderItem VALUES(NULL, 50.00, 1 ,2, 2);
INSERT INTO orderItem VALUES(NULL, 200.00, 4 ,1, 3);
INSERT INTO orderItem VALUES(NULL, 100.00, 2 ,2, 3);

CREATE TABLE aux_rate_user(
userId INT,
rateId INT,
isUpvote BOOLEAN NOT NULL,
isDownvote BOOLEAN NOT NULL,
PRIMARY KEY (userId, rateId)
);

ALTER TABLE aux_rate_user ADD CONSTRAINT AUX_RATE_USER_COMMENTS_RATE_FK
FOREIGN KEY (rateId) REFERENCES rate(rateId);

ALTER TABLE aux_rate_user ADD CONSTRAINT AUX_RATE_USER_USER_FK
FOREIGN KEY (userId) REFERENCES `user`(userId);

INSERT INTO aux_rate_user VALUES(1, 2, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(1, 3, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(3, 1, FALSE, TRUE);
INSERT INTO aux_rate_user VALUES(2, 3, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(1, 4, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(1, 7, FALSE, TRUE);
INSERT INTO aux_rate_user VALUES(2, 7, TRUE, FALSE);
INSERT INTO aux_rate_user VALUES(3, 8, TRUE, FALSE);