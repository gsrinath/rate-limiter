# rate-limiter


sample curls

curl --location --request GET 'http://localhost:8080/hello1' \
--header 'uuid: api-client1'

curl --location --request GET 'http://localhost:8080/bye1' \
--header 'uuid: api-client1'

curl --location --request GET 'http://localhost:8080/hello2' \
--header 'firstName: Srinath' \
--header 'lastName: Gunasekaran'

curl --location --request GET 'http://localhost:8080/bye2' \
--header 'firstName: Srinath' \
--header 'lastName: Gunasekaran'
