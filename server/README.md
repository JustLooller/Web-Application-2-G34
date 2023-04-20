# HOW TO CONFIGURE POSTGRES
1) Open command prompt in the actual folder
2) Run the command `docker run --name wa2-postgres -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres`
3) Run the command `cd res`
4) Run the command `docker exec -i wa2-postgres psql -U postgres -d postgres < dump.sql`