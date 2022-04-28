# DB & Redis Session Server
## docker-compose up -d
1. move current path
2. ```docker-compose -p koneps up -d``` on terminal
## Database: Postgres
1. ```docker exec -it postgres_koneps /bin/bash``` on terminal
2. ```psql -U postgres```
3. ```create database koneps;```
4. ```create user koneps with encrypted password 'koneps';```
5. ```grant all privileges on database koneps to koneps;```
