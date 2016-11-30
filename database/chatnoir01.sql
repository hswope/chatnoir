----------------------------------------------------------
-- initial drop and create of all
----------------------------------------------------------

----------------------------------------------------------
-- drop
----------------------------------------------------------
\echo 'dropping'

DROP TABLE public."User";

----------------------------------------------------------
-- create
----------------------------------------------------------
\echo 'creating'

CREATE TABLE public."User"
(
    id UUID PRIMARY KEY NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    displayname TEXT NOT NULL

);
CREATE UNIQUE INDEX "User_username_uindex" ON public."User" (username);
