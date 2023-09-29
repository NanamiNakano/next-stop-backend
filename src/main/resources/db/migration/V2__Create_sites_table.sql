create table sites
(
    uuid  uuid                 not null
        primary key,
    name  varchar(100)         not null,
    url   varchar(200)         not null,
    alive boolean default true not null
);

alter table sites
    owner to postgres;

INSERT INTO public.sites (uuid, name, url, alive) VALUES ('00000000-0000-0000-0000-000000000000', 'example', 'https://www.example.com', true);
