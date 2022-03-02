--For MySQL use Use Products;---
---(id,name,description,brand,category,created_at)---

insert into Products Values(
'b6afac37-cf9a-4fd4-8257-f096dbb5d34d',
'Red Shirt',
'Red hugo boss shirt',
'Hugo Boss',
--'{"red","shirt","slim fit"}',
'apparel',
'2017-04-15T01:02:03Z'
);

insert into Tags(pid,tag) Values(
'b6afac37-cf9a-4fd4-8257-f096dbb5d34d',
'red'
);

insert into Tags(pid,tag) Values(
'b6afac37-cf9a-4fd4-8257-f096dbb5d34d',
'shirt'
);

insert into Tags(pid,tag) Values(
'b6afac37-cf9a-4fd4-8257-f096dbb5d34d',
'slim fit'
);

insert into Products Values(
'357cd2c8-6f69-4bea-a6fa-86e40af0d867',
'Blue Shirt',
'Blue hugo boss shirt',
'Hugo Boss',
--'{"blue","shirt"}',
'apparel',
'2017-04-15T01:02:03Z'
);

insert into Tags(pid,tag) Values(
'357cd2c8-6f69-4bea-a6fa-86e40af0d867',
'blue'
);

insert into Tags(pid,tag) Values(
'357cd2c8-6f69-4bea-a6fa-86e40af0d867',
'shirt'
);


COMMIT;