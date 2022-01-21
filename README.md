<p align="center">
    <a href="https://github.com/isar/isar/blob/main/LICENSE">
        <img src="https://img.shields.io/github/license/isar/isar?color=%23007A88&labelColor=333940&logo=apache">
    </a>
</p>

# Tipo De Cambio API

> #### Aclaraciones:
> 1. La información del tipo de cambio de monedas son generadas al iniciar la aplicación **(no son datos reales)**.
> 2. Para la programación reactiva, decidí utilizar [Reactor](https://projectreactor.io/) en lugar de RxJava, ya que tiene mucha más compatibilidad con proyectos Spring :).

- [Características](#características)
- [Pasos para ejecutar el servicio en Docker](#pasos-para-ejecutar-el-servicio-en-Docker)
- [Endpoint de autenticación](#endpoint-de-autenticación)
  - [Login](#login)
- [Endpoints de monedas](#endpoints-de-monedas)
    - [Obtener monedas](#obtener-monedas)
    - [Obtener moneda con símbolo](#obtener-moneda-con-símbolo)
- [Endpoints de tipo cambio](#endpoints-de-tipo-cambio)
    - [Obtener tipos de cambio de una moneda](#obtener-tipos-de-cambio-de-una-moneda)
    - [Actualizar el tipo de cambio](#actualizar-el-tipo-de-cambio)
    - [**Convertir una moneda a otra**](#convertir-una-moneda-a-otra)

### Características

- 🔐 **Seguridad con JWT**. Integrado con Spring Security
- ⏱ **Asíncrono**. Con Spring WebFlux
- 🚀 **Dokerizado**. Ejecutable desde una imagen docker
- 🛢 **BD Embebida**. Información autogenerada al arrancar la aplicación
- 📜 **5 Endpoints**. Disponibles para usarlo.

## Pasos para ejecutar el servicio en Docker

### 1. Crear el archivo JAR
```console
mvn clean install
```

### 2. Construir imagen docker
```console
docker build -t bcp-container:1.0
```

### 3. Correr contenedor creado escuchando el puerto 8080
```console
docker run -d -p 8080:8080 -t bcp-container:1.0
```

## Endpoint de autenticación
### Login
```
POST /login
```
Servicio de Autenticación.

**Request:**
```json
{
    "username": "bcp-user",
    "password": "challenge123"
}
```

**Request attributes:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
username | STRING | YES | Usuario de acceso (usuario por defecto "bcp-user")
password | STRING | YES | Password de acceso (password por defecto "challenge123")

**Response 200:**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJiY3AtdXNlciIsImlhdCI6MTY0MjcxNjg5MCwiZXhwIjoxNjQyNzE3NzkwfQ.urq8IrKWU2Di0mTtrfdkdXYb8FC5DIng_11f4Wovfqk"
}
```
**Response 401:**
```json
{
    "timestamp": "2022-01-20T23:36:55.749+00:00",
    "path": "/login",
    "status": 401,
    "error": "Unauthorized",
    "message": null,
    "requestId": "0c8d9551-1"
}
```

## Endpoints de monedas
### Obtener monedas
```
GET /currency  (Authorization: Bearer eyJhbG...)
```
Servicio que obtiene lista de monedas disponibles.

**Headers:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
Authorization | STRING | YES | Token de acceso

**Response 200:**
```json
[
  {
    "id": 1,
    "symbol": "AED",
    "description": "United Arab Emirates Dirham"
  },
  {
    "id": 2,
    "symbol": "AFN",
    "description": "Afghan Afghani"
  },
  {
    "id": 3,
    "symbol": "ALL",
    "description": "Albanian Lek"
  }
]
```

### Obtener moneda con símbolo
```
GET /currency/{symbol}  (Authorization: Bearer eyJhbG...)
```
Servicio que obtiene la infomracion de una moneda.

**Path parameter:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
symbol | STRING | YES | Puede ser cualquier identificador único de una moneda (ex: PEN, USD, BND, CLP, etc...)

**Headers:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
Authorization | STRING | YES | Token de acceso

**Response 200:**
```json
{
  "id": 112,
  "symbol": "PEN",
  "description": "Peruvian Nuevo Sol"
}
```

**Response 401:**
```json
```

**Response 404:**
```json
```

## Endpoints de tipo cambio
### Obtener tipos de cambio de una moneda
```
GET /exchange/{symbol}  (Authorization: Bearer eyJhbG...)
```

Servicio que obtiene los tipos de cambio de una moneda frente a las otras, tomando como tipo de cambio base el USD.

**Path parameter:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
symbol | STRING | NO | Puede ser cualquier identificador único de una moneda (ex: PEN, USD, BND, CLP, etc...). Si no se le pasa el parámetro [symbol] por defecto utilizara USD. 

**Headers:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
Authorization | STRING | YES | Token de acceso

**Response 200:**
```json
{
  "timestamp": "1642704611565",
  "base": "PEN",
  "rates": {
    "FJD": 1.72435,
    "MXN": 1.63785,
    "STD": 0.56658,
    "SCR": 0.57339,
    "CDF": 0.48623,
    "BBD": 0.48889
  }
}
```
**Response 401:**
```json
```

**Response 404:**
```json
```

### Actualizar el tipo de cambio
```
POST /exchange  (Authorization: Bearer eyJhbG...)
```

Servicio que actualiza el tipo de cambio de una moneda frente a la [USD].

**Request:**
```json
{
  "moneda": "PEN",
  "monto": 2.28515
}
```

**Request attributes:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
monto | NUMERIC | YES | Nuevo valor del tipo de cambio.
moneda | STRING | YES | Moneda que desea actualizar. Puede ser cualquier identificador único de una moneda (ex: PEN, USD, BND, CLP, etc...).

**Headers:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
Authorization | STRING | YES | Token de acceso

**Response 200:**
```json
{
  "message": "Se actualizo el tipo de cambio satisfactoriamente."
}
```

**Response 400:**
```json
{
  "message": "La peticion no tiene los valores correctos.",
  "errors": [
    {
      "property": "monto",
      "message": "La propiedad [monto] no puede ser null"
    },
    {
      "property": "moneda",
      "message": "La propiedad [moneda] debe tener un formato de 3 caracteres"
    }
  ]
}
```

**Response 401:**
```json
```

**Response 404:**
```json
```

### Convertir una moneda a otra
```
POST /convert  (Authorization: Bearer eyJhbG...)
```

Servicio que transforma el monto de una moneda a otra, usando como base el tipo de cambio [USD].

**Request:**
```json
{
  "monto": 2.0,
  "monedaOrigen": "PEN",
  "monedaDestino": "USD"
}
```

**Request attributes:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
monto | NUMERIC | YES | Monto que desea convertir.
monedaOrigen | STRING | YES | Símbolo de moneda origen. Puede ser cualquier identificador único de una moneda (ex: PEN, USD, BND, CLP, etc...).
monedaDestino | STRING | YES | Símbolo de moneda destino. Puede ser cualquier identificador único de una moneda (ex: PEN, USD, BND, CLP, etc...).

**Headers:**

Name | Type | Mandatory | Description
------------ | ------------ | ------------ | ------------
Authorization | STRING | YES | Token de acceso

**Response 200:**
```json
{
  "message": "Conversion exitosa.",
  "result": {
    "monto": 2.0,
    "montoTipoCambio": 0.6615,
    "monedaOrigen": "PEN",
    "monedaDestino": "USD",
    "tipoCambio": 0.33075
  }
}
```

**Response 400:**
```json
{
    "message": "La peticion no tiene los valores correctos.",
    "errors": [
        {
            "property": "monedaOrigen",
            "message": "La propiedad [monedaOrigen] no puede ser null"
        },
        {
            "property": "monedaDestino",
            "message": "La propiedad [monedaDestino] no puede ser null"
        },
        {
            "property": "monto",
            "message": "La propiedad [monto] no puede ser null"
        }
    ]
}
```

**Response 401:**
```json
```

**Response 404:**
```json
```

# Happy Codding ;) 👨‍💻
