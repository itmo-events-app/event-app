insert into notification (user_id,
                          title,
                          description,
                          seen,
                          sent_time,
                          link)
values (1,
        'TestTitle1',
        'TestDescription1',
        false,
        '2024-01-01 12:34:56.789124 +00:00',
        'http://localhost:8080/task/1');

insert into notification (user_id,
                          title,
                          description,
                          seen,
                          sent_time,
                          link)
values (1,
        'TestTitle2',
        'TestDescription2',
        true,
        '2024-01-01 12:34:56.789123 +00:00',
        'http://localhost:8080/task/1');

insert into notification (user_id,
                          title,
                          description,
                          seen,
                          sent_time,
                          link)
values (2,
        'TestTitle3',
        'TestDescription3',
        false,
        '2024-01-01 12:34:56.789122 +00:00',
        'http://localhost:8080/task/2');

insert into notification (user_id,
                          title,
                          description,
                          seen,
                          sent_time,
                          link)
values (2,
        'TestTitle4',
        'TestDescription4',
        true,
        '2024-01-01 12:34:56.789121 +00:00',
        'http://localhost:8080/task/2');