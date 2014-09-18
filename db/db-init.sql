create table drug1 
(drug_id int PRIMARY KEY auto_increment, drug_name varchar(50) NOT NULL UNIQUE);

create table patient1 
(patient_id int PRIMARY KEY auto_increment, patient_name varchar(50) NOT NULL UNIQUE);

create table order1 
(order_id int PRIMARY KEY auto_increment
, order_name varchar(50) NOT NULL
, order_type varchar(10) NOT NULL
);

create table prescribe1 
(prescribe_id int PRIMARY KEY auto_increment, prescribe_name varchar(100) NOT NULL UNIQUE);

-- sql db dump command
-- script to '/home/roman/Documents/01_curepathway/work3/cuwy-cpoe-hol2/cuwy-cpoe-hol1.db.sql'

