create table accounts
(
    uuid     uuid          not null,
    username varchar(16)   not null
        primary key,
    password varchar(2000) not null,
    token    text          not null
);

alter table accounts
    owner to postgres;

INSERT INTO public.accounts (uuid, username, password, token) VALUES ('00000000-0000-0000-0000-000000000000', 'development', '$argon2id$v=19$m=47104,t=1,p=1$vjiE0z4hoKmZUokZ9igDzd0yNnS9JBGa5PS29Vrgh7M$IJhyD8ua4b/6kAhXaOV4APJFYtFRnqz0EJyAyKH71eJZVWwwjJbHlCaQiOuo4XVKcHCCmgpC/QE11qvKwG59uRtKnC0vOBGgYccRRO+PzuXku8O8tU3mcFVPfZ0GugrzshJONH9FJytBSpwzo+nboyUszkU6XhvPc2NJnuZTQJE', 'development');
