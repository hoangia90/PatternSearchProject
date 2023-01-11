set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE patternSearchdb;
    CREATE USER postgres WITH ENCRYPTED PASSWORD '123';
    GRANT ALL PRIVILEGES ON DATABASE patternSearchdb TO postgres;
EOSQL