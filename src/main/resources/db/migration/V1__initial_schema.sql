-- Initial database schema matching Rails implementation

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email_address TEXT NOT NULL UNIQUE,
    password_digest TEXT NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    user_agent TEXT,
    ip_address TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX IF NOT EXISTS index_sessions_on_user_id ON sessions(user_id);

CREATE TABLE IF NOT EXISTS bio (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    brief_bio TEXT,
    content TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS contact_info (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT,
    github_url TEXT,
    linkedin_url TEXT,
    twitter_url TEXT,
    untapped_url TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS blog_posts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT,
    filename TEXT,
    published_at TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS index_blog_posts_on_filename ON blog_posts(filename);

CREATE TABLE IF NOT EXISTS presentations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT,
    abstract TEXT,
    slides_url TEXT,
    github_url TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS conferences (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    year INTEGER NOT NULL,
    link TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS index_conferences_on_title_and_year ON conferences(title, year);

CREATE TABLE IF NOT EXISTS conference_presentations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    conference_id INTEGER NOT NULL,
    presentation_id INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (conference_id) REFERENCES conferences(id),
    FOREIGN KEY (presentation_id) REFERENCES presentations(id)
);

CREATE INDEX IF NOT EXISTS index_conference_presentations_on_conference_id ON conference_presentations(conference_id);
CREATE INDEX IF NOT EXISTS index_conference_presentations_on_presentation_id ON conference_presentations(presentation_id);
