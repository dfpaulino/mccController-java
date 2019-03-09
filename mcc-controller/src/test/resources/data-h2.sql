
--1
 insert
     into
         atmega
         (created_at, updated_at, address, model, name, id,in_use)
     values
         (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), '0xFFFF', 1, 'XPTO',NEXTVAL('hibernate_sequence'),true  );


--=========================================================================
-- ADC0
-- ID 2
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 0, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence'),true );

-- ADC1
-- ID 3
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence') ,true);


-- ADC2
-- ID 4
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence') ,false);

-- ADC3
-- ID 5
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence'),false );

-- ADC4
-- ID 6
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 4, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence'),false );

-- ADC5
-- ID 7
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 5, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence'),false );

-- ADC6
-- ID 8
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 6, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence'),false );


-- ADC7
-- ID 9
--
insert
into
        adc
        (created_at, updated_at, adc_id, atmega_id, value, id,in_use)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 7, (select id from atmega where address='0xFFFF'), 128, NEXTVAL('hibernate_sequence'),true );




 --#######################################

-- TIMER0
-- ID 10

    insert
    into
        timer
        (created_at, updated_at, atmega_id, in_use, mode, name, out_put_compare_register, id)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (select id from atmega where address='0xFFFF'), true, 'FAST_PWM', 'timer1', 240, NEXTVAL('hibernate_sequence'));

-- TIMER1
-- ID 11

    insert
    into
        timer
        (created_at, updated_at, atmega_id, in_use, mode, name, out_put_compare_register, id)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (select id from atmega where address='0xFFFF'), true, 'FAST_PWM', 'timer1', 240, NEXTVAL('hibernate_sequence'));

-- TIMER2
-- ID 12

    insert
    into
        timer
        (created_at, updated_at, atmega_id, in_use, mode, name, out_put_compare_register, id)
    values
        (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (select id from atmega where address='0xFFFF'), true, 'FAST_PWM', 'timer1', 240, NEXTVAL('hibernate_sequence'));


 --#######################################

--portA
-- ID 13
   insert
       into
           portn
           (created_at, updated_at, atmega_id, ddb, port_name, value, id,in_use)
       values
           (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (select id from atmega where address='0xFFFF'), 0, 'portA', 0, NEXTVAL('hibernate_sequence'),true)      ;

--portB
-- ID 14
   insert
       into
           portn
           (created_at, updated_at, atmega_id, ddb, port_name, value, id,in_use)
       values
           (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (select id from atmega where address='0xFFFF'), 0, 'portB', 0, NEXTVAL('hibernate_sequence'),false);

   --portC
   -- ID 15
      insert
          into
              portn
              (created_at, updated_at, atmega_id, ddb, port_name, value, id,in_use)
          values
              (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (select id from atmega where address='0xFFFF'), 0, 'portC', 0, NEXTVAL('hibernate_sequence'),true);

   --portD
   -- ID 16
      insert
          into
              portn
              (created_at, updated_at, atmega_id, ddb, port_name, value, id,in_use)
          values
              (CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), (select id from atmega where address='0xFFFF'), 0, 'portD', 0, NEXTVAL('hibernate_sequence'),true)

