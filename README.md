
# Cloud File Storage

Multi-user file cloud. Users of the service can use it to download and store files. 
The inspiration for the project is Google Drive.


## Tech Stack

- Java 17
- Spring Framework (Boot, Security, Data JPA)
- MinIO S3
- Docker


## Features

- Registration, Authorization, Logout
- Upload, delete, rename files and folders
- Create new empty folders
- Deep recursive file search


## Run Locally

Clone the project

```bash
  git clone https://github.com/greenblat17/cloud-file-storage
```

Go to the project directory

```bash
  cd cloud-file-storage
```

Run docker compose for DB and S3 storage

```bash
  docker compose up -d
```

Start the server

```bash
  mvn spring-boot:run
```

