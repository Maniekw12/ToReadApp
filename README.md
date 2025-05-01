
> [!WARNING]
> NOTE: 🚧 Application under development
>
📌 Features of Maniek's Book App

🔍 View all books – browse the entire book collection

📘 View read books – see which books you've already read

📕 View unread books – check what's still on your reading list

🔎 Show book by ID – quickly find a specific book by its ID

🗑️ Delete a book – remove books you no longer need

➕ Add a new book – expand your library

✏️ Update a book – edit book details


## ⚙️ Application Setup & Running

> ⚠️ **Requirements**:  
> - Docker must be installed  
> - Ports `8000` (Frontend), `8080` (Backend) and `5432` (database) must be free  

## Application Set-up and Running
Before you begin, ensure that Docker is installed on your machine and that the following ports are available for use:

>Port 8000: Used by the frontend.

>Port 8080: Used by the backend.

>Port 5432: Used by the database.



### Build the Application
To build the application, use the following command:

```bash
docker compose up --build
```
### Running Application
Once the application is successfully built, you can run the image with:

```bash
docker compose up 
```
### Stopping Application
To stop the containers
```bash
docker compose down 
```

Once everything is set up and running, the application will be available at:
 http://localhost:8000.



## THINGS TO DO:
- Develop Tests
- Design a New Frontend
- Refactor API Endpoints


![img.png](img.png)


![img_1.png](img_1.png)

![img_2.png](img_2.png)
