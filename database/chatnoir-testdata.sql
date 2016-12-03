--Test Data
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO "User" (id, username, password, displayname) VALUES (uuid_generate_v4(), 'hswope', 'password', 'Howard Swope');
