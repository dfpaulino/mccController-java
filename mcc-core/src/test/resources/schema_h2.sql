drop table adc if exists;
drop table atmega if exists;
drop table portn if exists;
drop table timer if exists;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;

create table atmega (
       id integer not null,
        created_at timestamp not null,
        updated_at timestamp not null,
        address varchar(255) not null,
        model integer,
        name varchar(255) not null,
        in_use boolean not null,
        primary key (id)
    );

create table adc (
       id integer not null,
        created_at timestamp not null,
        updated_at timestamp not null,
        adc_id tinyint not null,
        value integer not null,
        atmega_id integer,
        in_use boolean not null,
        primary key (id)
    );



create table portn (
       id integer not null,
        created_at timestamp not null,
        updated_at timestamp not null,
        ddb tinyint not null,
        port_name varchar(255),
        value tinyint not null,
        atmega_id integer,
        in_use boolean not null,
        primary key (id)
    );

create table timer (
       id integer not null,
        created_at timestamp not null,
        updated_at timestamp not null,
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
           references atmega;

     alter table portn
            add constraint FKt51hisekwqv91x1bdfap381v1
            foreign key (atmega_id)
            references atmega;

     alter table timer
             add constraint FKmkd31xoffinexehjwke3bmsjg
             foreign key (atmega_id)
             references atmega