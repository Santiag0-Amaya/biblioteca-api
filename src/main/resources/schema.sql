CREATE TABLE IF NOT EXISTS libros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    autor TEXT NOT NULL,
    isbn TEXT UNIQUE,
    anio_publicacion INTEGER,
    stock_total INTEGER NOT NULL,
    stock_disponible INTEGER NOT NULL
);
