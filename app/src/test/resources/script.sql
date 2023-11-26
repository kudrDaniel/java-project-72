INSERT INTO urls (name, created_at) VALUES ('https://localhost', CURRENT_TIMESTAMP);

INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) VALUES (1, 200, 'Script', 'Done', 'Successful', CURRENT_TIMESTAMP);