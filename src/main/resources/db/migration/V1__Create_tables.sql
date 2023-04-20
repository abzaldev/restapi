DROP TABLE IF EXISTS employee_item;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS employee;

DROP SEQUENCE IF EXISTS employee_item_seq;
DROP SEQUENCE IF EXISTS item_seq;
DROP SEQUENCE IF EXISTS employee_seq;



CREATE SEQUENCE employee_item_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE employee_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE item_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE employee
(
    id   BIGINT      NOT NULL,
    name VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE employee_item
(
    id          BIGINT  NOT NULL,
    quantity    INTEGER NOT NULL,
    employee_id BIGINT  NOT NULL,
    item_id     BIGINT  NOT NULL,
    PRIMARY KEY (id)
);

CREATE table item
(
    id    BIGINT    NOT NULL,
    name  VARCHAR(255),
    price FLOAT(53) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS employee_item
    ADD CONSTRAINT fk_employee_item_employee_id FOREIGN KEY (employee_id) REFERENCES employee;
ALTER TABLE IF EXISTS employee_item
    ADD CONSTRAINT fk_employee_item_item_id FOREIGN KEY (item_id) REFERENCES item;

