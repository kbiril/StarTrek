insert into bestellingen (werknemerId, omschrijving, bedrag, moment) values
  ((select id from werknemers where voornaam = 'test1' and familienaam = 'test1'), 'test1', 20, '2023/05/06 11:00:00'),
  ((select id from werknemers where voornaam = 'test1' and familienaam = 'test1'), 'test2', 35, '2023/05/06 12:00:00');