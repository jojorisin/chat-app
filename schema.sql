





CREATE TABLE users
(
    user_id         INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username        VARCHAR(50) UNIQUE NOT NULL,
    password TEXT               NOT NULL
)



CREATE TABLE messages(
                         message_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         user_id INT NOT NULL REFERENCES users(user_id),
                         message TEXT NOT NULL,
                         created_at TIMESTAMP
)
