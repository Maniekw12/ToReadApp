
> [!WARNING]
> NOTE: APPLICATION IN PROGRESS
>
📌 Features of Maniek's Book App

🔍 View all books – browse the entire book collection

📘 View read books – see which books you've already read

📕 View unread books – check what's still on your reading list

🔎 Show book by ID – quickly find a specific book by its ID

🗑️ Delete a book – remove books you no longer need

➕ Add a new book – expand your library

✏️ Update a book – edit book details


![image](https://github.com/user-attachments/assets/be29cbb4-d3de-4b50-a743-13b8f42ab210)

## ⚙️ Application Setup & Running

> ⚠️ **Requirements**:  
> - Docker must be installed  
> - Ports `8000` (Frontend) and `8080` (Backend) must be free  
> - Python (for frontend server)

## Application Set-up and Running
you should have Docker instaled, ensure your ports: 8000 and 8080 are free to use.
8000 is responsible for frontend while 8080 for backend.

### Running Backend
to run application, Firstly you should type 
```bash
docker compose up 
```
to set up the database and later
```bash
./gradlew bootRun 
```
to run application.

### Running Frontend
to run Frontend, you should open CMD and type: 
```bash
cd [Path to Frontend file]
```
and later
```bash
py -m http.server 8000
```
while everything is ready, application will be available on http://localhost:8000.
> [!WARNING]
> ENSURE THAT, YOU HAVE PYTHON INSTALLED
>


### Killing the database

to kill database you should type
```bash
docker compose down
```
## THINGS TO DO:
- Make Dockerfile to run the entire Application using one command.




