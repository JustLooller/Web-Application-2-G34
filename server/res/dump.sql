create table brand
(
    id   serial
        primary key,
    name varchar(100)
);

alter table brand
    owner to postgres;

create sequence brand_seq
    increment by 50;

alter sequence brand_seq owner to postgres;

create sequence message_seq
    increment by 50;

alter sequence message_seq owner to postgres;

create table product
(
    ean         varchar(20) not null
        constraint products_pkey
            primary key,
    brand       integer
        constraint products_brand_fkey
            references brand,
    model       varchar(100),
    description text,
    image       text
);

alter table product
    owner to postgres;

create table profile
(
    email varchar not null
        primary key,
    name  varchar not null,
    age   integer,
    role  varchar(255)
);

alter table profile
    owner to postgres;

create table sale
(
    id             varchar(255) not null
        primary key,
    warranty_end   timestamp(6),
    warranty_start timestamp(6),
    buyer_email    varchar(255)
        constraint fk7b4dbc1e9mqbkexexqpi7syjl
            references profile,
    product_ean    varchar(255)
        constraint fkppuxyfkxlpd95x0vfpuhqy81t
            references product
);

alter table sale
    owner to postgres;

create table ticket
(
    id            bigint not null
        primary key,
    priority      varchar(255),
    state         varchar(255),
    creator_email varchar(255)
        constraint fk2gkub6wh4uggspks2n2w60adq
            references profile,
    expert_email  varchar(255)
        constraint fkcdv4vytab2xbswiayn6ixku6s
            references profile,
    product_ean   varchar(255)
        constraint fkmrdntxvndf7l3m59oj3oujemp
            references product,
    sale_id       varchar(255)
        constraint fkdjg1dxcotlu73o5pac4i68if6
            references sale
);

create table message
(
    id         bigint not null
        primary key,
    attachment varchar(255),
    text       varchar(255),
    timestamp  timestamp(6),
    ticket_id  bigint
        constraint fkahc0e8ev24pd1cfg3twg2qyb
            references ticket,
    user_email varchar(255)
        constraint fkd4s9m3gwauto4id98ce6ldlcu
            references profile
);

create table state_history
(
    id         bigint not null
        primary key,
    status     varchar(255),
    timestamp  timestamp(6),
    ticket_id  bigint
        constraint fklaj0wscu0nhj01j2j8nqiongq
            references ticket,
    user_email varchar(255)
        constraint fk4g5o1lodrl4ch0e215a8q9rds
            references profile
);

alter table message
    owner to postgres;

alter table state_history
    owner to postgres;

create sequence state_history_seq
    increment by 50;

alter sequence state_history_seq owner to postgres;



alter table ticket
    owner to postgres;

create sequence ticket_seq
    increment by 50;

alter sequence ticket_seq owner to postgres;

INSERT INTO brand (id, name) VALUES (1, 'Apple');
INSERT INTO brand (id, name) VALUES (2, 'Bosch');
INSERT INTO brand (id, name) VALUES (3, 'DCG');
INSERT INTO product (ean, brand, model, description, image) VALUES ('0069420894316', 3, 'FORNO VERTICALE DCG PER SPIEDINI-KEBAB-POLLO', 'Cuoci Kebab elettrico rotante domestico DCG Forno verticale ideale per cuocere spiedini,kebab, pollo, pesce e verdure,  capacità 21 lt, potenza 1200W.', 'https://m.media-amazon.com/images/I/71tAObSBoyL._AC_UF1000,1000_QL80_.jpg');
INSERT INTO product (ean, brand, model, description, image) VALUES ('0194252708002', 1, 'Apple iPhone 13', e'Il tuo nuovo superpotere. Il nostro sistema a doppia fotocamera più evoluto di sempre. Migliora del 47% la qualità delle immagini riprese in notturna.', 'https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/refurb-iphone-13-pro-graphite-2023?wid=2000&hei=1897&fmt=jpeg&qlt=95&.v=1679072987081');
INSERT INTO product (ean, brand, model, description, image) VALUES ('0194253401070', 1, 'IPHONE 14 PRO 128GB', 'Un modo magico e innovativo di interagire con iPhone. Una nuova, essenziale funzione per la sicurezza, pensata per salvare vite. Una straordinaria fotocamera da 48MP per dettagli da non credere. E dietro tutto questo, la potenza del più evoluto chip per smartphone.', 'https://www.apple.com/newsroom/images/product/iphone/lifestyle/Apple-iPhone-14-iPhone-14-Plus-yellow-2up-230307_inline.jpg.large.jpg');
INSERT INTO product (ean, brand, model, description, image) VALUES ('4242005348862', 2, 'Lavatrice a carica frontale WIW28542EU', 'La lavatrice da incasso Bosch WIW28542EU è dotata di motore EcoSilence Drive™ silenzioso e ad alta efficienza energetica. Se hai fretta e non puoi aspettare un intero ciclo di lavaggio, SpeedPerfect riduce il tempo di lavaggio fino al 65% senza compromettere i risultati.', 'https://www.vieffetrade.com/shop/foto_articoli/Bosch/556722/556722.jpg');
INSERT INTO profile (email, name, age, role) VALUES ('giacomo.demattia@mail.com', 'Giacomo Demattia', 22, null);
INSERT INTO profile (email, name, age, role) VALUES ('marco.chessa@mail.com', 'Marco Chessa', 24, null);
INSERT INTO profile (email, name, age, role) VALUES ('customeruser@gmail.com', 'Customer User', 24, 'CUSTOMER');
INSERT INTO profile (email, name, age, role) VALUES ('expertuser@gmail.com', 'Expert User', 24, 'EXPERT');
INSERT INTO profile (email, name, age, role) VALUES ('manageruser@gmail.com', 'Manager User', 24, 'MANAGER');
INSERT INTO sale (id, warranty_end, warranty_start, buyer_email, product_ean) VALUES ('1', '2026-10-10 00:00:00', '2021-10-10 00:00:00', 'customeruser@gmail.com', '0194252708002');
INSERT INTO sale (id, warranty_end, warranty_start, buyer_email, product_ean) VALUES ('2072b61a-4932-4ba0-838d-a18d1dc33ac3', '2026-10-10 00:00:00', '2021-10-10 00:00:00', 'customeruser@gmail.com', '0194253401070');
INSERT INTO sale (id, warranty_end, warranty_start, buyer_email, product_ean) VALUES ('fe2677f3-99ba-4248-8303-160dfdfa0ddf', '2026-10-10 00:00:00', '2021-10-10 00:00:00', 'customeruser@gmail.com', '0194253401070');
INSERT INTO sale (id, warranty_end, warranty_start, buyer_email, product_ean) VALUES ('537e38f6-bd39-4472-b020-ade4a7593356', '2026-10-10 00:00:00', '2021-10-10 00:00:00', 'customeruser@gmail.com', '0194252708002');
INSERT INTO sale (id, warranty_end, warranty_start, product_ean) VALUES ('8d81948c-1268-4637-a22d-22b2db662153', '2026-10-10 00:00:00', '2021-10-10 00:00:00', '0194252708002');
INSERT INTO sale (id, warranty_end, warranty_start, product_ean) VALUES ('w-uuid-kebab', '2026-10-10 00:00:00', '2021-10-10 00:00:00', '0069420894316');
INSERT INTO sale (id, warranty_end, warranty_start, product_ean) VALUES ('w-uuid-iphone', '2026-10-10 00:00:00', '2021-10-10 00:00:00', '0194252708002');
INSERT INTO sale (id, warranty_end, warranty_start, product_ean) VALUES ('w-uuid-lavatrice', '2026-10-10 00:00:00', '2021-10-10 00:00:00', '4242005348862');



