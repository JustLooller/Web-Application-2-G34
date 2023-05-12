# Web-Application-2-G34

## JIB Image Creation and Deployment
1. `cd ./server`
2. `./gradlew jibDockerBuild`
3. `cd ../ && docker-compose up -d`

## Frontend build
1. `cd ./client`
2. `npm install`
3. `npm run build`

The build will be in the `server/[...]/resources/static` folder.  
The server will serve the static files from there.

## PostgresDB setup

1) Open command prompt in the root folder of this project
2) `docker-compose up -d`
3) `cd server/res`
4) `docker exec -i wa2_g34_db psql -U postgres -d postgres < dump.sql`
