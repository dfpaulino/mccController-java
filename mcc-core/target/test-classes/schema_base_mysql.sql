use test;
DROP TABLE IF EXISTS adc;
DROP TABLE IF EXISTS atmega;
DROP TABLE IF EXISTS portn;
DROP TABLE IF EXISTS timer;


create table atmega (
       id integer not null AUTO_INCREMENT,
        created_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        address varchar(255) not null ,
        model integer,
        name varchar(255) not null,
        in_use boolean not null,
        primary key (id)
    );

create table adc (
       id integer not null AUTO_INCREMENT,
        created_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        adc_id tinyint not null,
        value integer not null,
        atmega_id integer,
        in_use boolean not null,
        primary key (id)
    );



create table portn (
       id integer not null AUTO_INCREMENT,
        created_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        ddb tinyint not null,
        port_name varchar(255),
        value tinyint not null,
        atmega_id integer,
        in_use boolean not null,
        primary key (id)
    );

create table timer (
       id integer not null AUTO_INCREMENT,
        created_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        updated_at timestamp not null DEFAULT CURRENT_TIMESTAMP(),
        in_use boolean not null,
        mode varchar(255),
        name varchar(255),
        out_put_compare_register integer,
        atmega_id integer,
        primary key (id)
    );

     alter table adc
           add constraint FKrecsy3d9oih65qfloouoipx6v
           foreign key (atmega_id)
           references atmega(id) ON DELETE CASCADE;

     alter table portn
            add constraint FKt51hisekwqv91x1bdfap381v1
            foreign key (atmega_id)
            references atmega(id) ON DELETE CASCADE;

     alter table timer
             add constraint FKmkd31xoffinexehjwke3bmsjg
             foreign key (atmega_id)
             references atmega(id) ON DELETE CASCADE;