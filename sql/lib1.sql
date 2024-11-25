
CREATE TABLE User (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE ,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nick_name VARCHAR(255),
    phone VARCHAR(255),
    sex VARCHAR(255),
    address VARCHAR(255),
    role INT NOT NULL        -- 0为读者1为管理员
);

CREATE TABLE BookIsbn (
    isbn VARCHAR(13) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2),
    author VARCHAR(255),
    publisher VARCHAR(255),
    borrownum INT DEFAULT 0
);

CREATE TABLE BookEntity (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(13),
    status VARCHAR(1) DEFAULT '1',
    place VARCHAR(255),
    FOREIGN KEY (isbn) REFERENCES BookIsbn (isbn)
);

CREATE TABLE Lends (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    lend_time DATETIME NOT NULL,
    return_time DATETIME DEFAULT NULL,
    returned BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES User (id),
    FOREIGN KEY (book_id) REFERENCES BookEntity (id)
) 

