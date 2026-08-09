-- Passwords are no longer written next to the rows they open. Levels 1, 2, 3 and 10 are seeded
-- as BCrypt at the application's work factor; each level's documented password still works, only
-- the stored form changed.

-- Level 1: SQL Injection
INSERT INTO auth_users VALUES (1, 'admin_sqli', '$2a$12$rqt5Gs.hJfRYDdl.PFyQ0OxsCgTf/e1biVMF.ifkbnf8TpEP574Pi', NULL, 'BCRYPT', 1, 'admin_sqli@example.com', 'ADMIN');

-- Level 2: Sensitive Data Logging
INSERT INTO auth_users VALUES (2, 'admin_logs', '$2a$12$kk45a0Z7yz6AF8u1bPLjA.BpAk7Xfs4uE4p420zn.bMzAfU4DFEuu', NULL, 'BCRYPT', 2, 'admin_logs@example.com', 'ADMIN');

-- Level 3: Plaintext Storage
INSERT INTO auth_users VALUES (3, 'admin_plain', '$2a$12$01mjbv4Pykv6Ms3PTIl/Z.rHnM6WY4hy3FM2MwJFadLkIQGQLcIsS', NULL, 'BCRYPT', 3, 'admin_plain@example.com', 'ADMIN');

-- Level 4: MD5 Hashing
INSERT INTO auth_users VALUES (4, 'admin_md5', '0168b6037606df265be7f1f5d9c0e7fe', NULL, 'MD5', 4, 'admin_md5@example.com', 'ADMIN');

-- Level 5: SHA1 Hashing
INSERT INTO auth_users VALUES (5, 'admin_sha1', '632e10860bd26278451d3f89d1c46f180e5623e0', NULL, 'SHA1', 5, 'admin_sha1@example.com', 'ADMIN');

-- Level 6: SHA-256 (No Salt)
INSERT INTO auth_users VALUES (6, 'admin_sha256', '8b8eca84f7e2b04f531749f999c3bf9e3f045bab78f4c8a451fa70929b3c3946', NULL, 'SHA256', 6, 'admin_sha256@example.com', 'ADMIN');

-- Level 7: Salted SHA-256. The seeded digest did not cover the salt, so the level could not be
-- logged into at all; it is now SHA-256(salt || password) as the verifier computes it.
INSERT INTO auth_users VALUES (7, 'admin_enum', '6eee688ff037e0ca328a059260596242f5a45fbb70bd5430bd63bf71b51ba8ad', 's9A#2zLk', 'SHA256', 7, 'admin_enum@example.com', 'ADMIN');

-- Level 8: Weak Password + Bcrypt. The guessable password is the point of this level, so it stays.
INSERT INTO auth_users VALUES (8, 'admin_weak', '$2a$10$gV2vZ5fxhZlwOP.GIqOI1.z7q5jws8VDmgIcKqY/uzvhzSUDio2sW', NULL, 'BCRYPT', 8, 'admin_weak@example.com', 'ADMIN');

-- Level 9: Secure (Bcrypt + Generic Error)
INSERT INTO auth_users VALUES (9, 'admin_secure', '$2a$10$1WiFUNqUY/vHTzR2QtuMQuzCLK3aZEdjEUpqS4msXOevaCz7Wobe.', NULL, 'BCRYPT', 9, 'admin_secure@example.com', 'ADMIN');

-- Level 10: BCrypt at the standard work factor. A cost of 4 is a few thousand guesses a second per
-- core; the documented password is unchanged so the level still logs in.
INSERT INTO auth_users VALUES (10, 'admin_lowcost', '$2a$12$uu01hAgWpMXeoVcdxr59T.Gge21Bx/gsA9UmefvRkwNF4NDNQTRDq', NULL, 'BCRYPT', 10, 'admin_lowcost@example.com', 'ADMIN');
