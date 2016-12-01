----------------------------------------------------------
-- initial drop and create of all
----------------------------------------------------------

----------------------------------------------------------
-- drop
----------------------------------------------------------
\echo 'dropping'

DROP TABLE public."Message";
DROP TABLE public."User";

----------------------------------------------------------
-- create
----------------------------------------------------------
\echo 'creating'

CREATE TABLE public."User"
(
    id UUID PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    displayname TEXT NOT NULL
);

CREATE TABLE public."Message"
(
  sender UUID REFERENCES "User" (id),
  receiver UUID REFERENCES "User" (id),
  timesent BIGINT NOT NULL,
  messageText TEXT NOT NULL,
  PRIMARY KEY (sender, receiver)
);
