# Kotlin Ktor Service - Sequence Counter

## Structure
 - Uses Jetbrains [Ktor](https://ktor.io) a [Coroutines](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html) based framework
 - No persistence as of now, a simple MutableSet used to store users list
 - For counter model with [Mutex](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.sync/-mutex/index.html) and Coroutines used
 - For authentication needs basic [JWT](https://jwt.io) token is used
 
## Running Application
 - Using source code (require JDK): `./gradlew run` 
 - Uber jar can also be build and run using: `java -jar <generated_jar>`
 
## API
**Root URL** : `/api/v1`

### Register
Used to register User

**URL** : `/user/register`

**Method** : `POST`

**Auth required** : NO

#### Data constraints

```json
{    
    "email": "<valid email address>",
    "name": "<name of user>",
    "password": "<password in plain text>"
}
```

#### Success Response

**Code** : `200 OK`

**Content example**

```json
{
     "id": "<UUID of user>",
     "email": "<email>",
     "name": "<username>"
}
```

#### Error Response

**Condition** : If 'email' already exists.

**Code** : `422 Unprocessable Entity`

**Content** :

```json
{
    "code": 100,
    "message": "User already exists"
}
```

#### CURL
```shell script
curl --request POST \
  --url /api/v1/user/register \
  --header 'content-type: application/json' \
  --data '{
	"name": "Sample User",
	"email": "me@example.com",
	"password": "super@hard"
}'
```

### Login
Used to collect a Token for a registered User.

**URL** : `/user/login`

**Method** : `POST`

**Auth required** : NO

#### Data constraints

```json
{
    "email": "<valid email address>",
    "password": "<password in plain text>"
}
```

#### Success Response

**Code** : `200 OK`

**Content example**

```json
{
    "token": "<JWT_TOKEN>"
}
```

#### Error Response

**Condition** : If 'username' and 'password' combination is wrong.

**Code** : `404 NOT FOUND`

**Content** :

```json
{
    "code": 101,
    "message": "User does not exists"
}
```

#### CURL
```shell script
curl --request POST \
  --url /api/v1/user/login \
  --header 'content-type: application/json' \
  --data '{
	"email": "me@example.com",
	"password": "super@hard"
}'
```


### Counter Peek
View the current value of counter

**URL** : `/counter/current`

**Method** : `GET`

**Auth required** : YES, Bearer 

#### Success Response

**Code** : `200 OK`

**Content example**

```json
{
     "value": "<value of counter>"
}
```

#### CURL
```shell script
curl --request GET \
  --url /api/v1/counter/current \
  --header 'authorization: Bearer <TOKEN>' \
  --header 'content-type: application/json'
```

### Counter Next
Increment value of counter by 1

**URL** : `/counter/current`

**Method** : `PUT`

**Auth required** : YES

#### Success Response

**Code** : `200 OK`

**Content example**

```json
{
     "value": "<value of counter>"
}
```

#### CURL
```shell script
curl --request PUT \
  --url /api/v1/counter/next \
  --header 'authorization: Bearer <TOKEN>' \
  --header 'content-type: application/json'
```


### Counter Reset
Reset the current value of counter by given value

**URL** : `/counter/reset`

**Method** : `PUT`

**Auth required** : YES

#### Data constraints

```json
{
    "value": "<positive int value for counter to reset>"
}
```


#### Success Response

**Code** : `200 OK`

**Content example**

```json
{
     "value": "<value of updated counter>"
}
```

#### Error Response

**Condition** : If 'value' is negative.

**Code** : `412 Precondition Failed`

**Content** :

```json
{
    "code": 200,
    "message": "Negative value for counter not allowed"
}
```

#### CURL
```shell script
curl --request PUT \
  --url /api/v1/counter/reset \
  --header 'authorization: Bearer <TOKEN>' \
  --header 'content-type: application/json' \
  --data '{
	"value": <positive_int>
}'
```


