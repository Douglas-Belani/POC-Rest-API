CREATE TABLE `user`(
userId INT PRIMARY KEY AUTO_INCREMENT, 
userName VARCHAR(120) NOT NULL,
cpf VARCHAR(12) UNIQUE NOT NULL,
email VARCHAR(160) UNIQUE NOT NULL,
password VARCHAR(200) NOT NULL, 
birthDate DATE NOT NULL
); 

CREATE TABLE state (
stateId INT PRIMARY KEY AUTO_INCREMENT,
initials CHAR(2) UNIQUE NOT NULL
);

CREATE TABLE city(
cityId INT PRIMARY KEY AUTO_INCREMENT,
cityName VARCHAR(120),
stateId INT NOT NULL
);

ALTER TABLE city ADD CONSTRAINT CITY_STATE_FK
FOREIGN KEY (stateId) REFERENCES state(stateId);

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

CREATE TABLE rate(
rateId INT PRIMARY KEY AUTO_INCREMENT,
upvotes INT NOT NULL,
downvotes INT NOT NULL
);

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

CREATE TABLE category(
categoryId INT PRIMARY KEY AUTO_INCREMENT,
categoryName VARCHAR(120)
);

CREATE TABLE aux_product_category (
productId INT,
categoryId INT,
PRIMARY KEY (productId, categoryId)
);

ALTER TABLE aux_product_category ADD CONSTRAINT AUX_PRODUCT_FK
FOREIGN KEY (productId) REFERENCES product(productId);

ALTER TABLE aux_product_category ADD CONSTRAINT AUX_CATEGORY_FK
FOREIGN KEY (categoryId) REFERENCES category(categoryId);

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

CREATE TABLE orderStatus(	
orderStatusId INT PRIMARY KEY,
status VARCHAR(120) NOT NULL UNIQUE
);

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