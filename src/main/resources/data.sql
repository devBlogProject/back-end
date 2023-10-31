INSERT INTO member (email,password,nick_name,member_role,created_date,updated_date)
values (
        'test@test.com',
        '$2a$12$SZHbzxk2kYBMhjiO9MU3J.SQ0a6sdOfpkhYwvJogOzXMvwiKYwQV6',
        'test_nick',
        'ADMIN',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
        );
