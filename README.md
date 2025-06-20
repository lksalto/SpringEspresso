# ☕ SpringEspresso

---

## Tecnologias usadas no projeto

- Spring MVC, Spring Data JPA, Spring Security & Thymeleaf (Lado Servidor)
- Javascript & CSS (Lado Cliente)

---

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **JDK 11 ou superior**
- **Apache Maven**
- **MySQL Server**
- **IDE (Eclipse, IntelliJ ou VS Code)**

---

## 1️⃣ Clonar o repositório

1. Abra o terminal e clone o projeto
**Execute esse comando:**
**git clone https://github.com/ronanpjr/SpringEspresso.git**
3. Acesse a pasta do projeto
**Execute esse comando:**
**cd SpringEspresso**

---

## 2️⃣ Configurar o banco de dados MySQL
1. Inicie o serviço do MySQL
- Garanta que o MySQL Server esteja rodando localmente.

2. Crie o banco de dados
- Abra o terminal do MySQL
**Execute esse comando:**
mysql -u root -p
- Crie o banco de dados
**Execute esse comando:**
CREATE DATABASE spring_espresso;
EXIT;

---

## 3️⃣ Build do projeto com Maven
No terminal, na raiz do projeto:
**Execute esse comando:**
mvn clean install

---

## 4️⃣ Rodar o projeto
Agora, para iniciar a aplicação
**Execute esse comando:**
mvn spring-boot:run
A aplicação Spring Boot iniciará na porta padrão 8080.
Acesse no navegador:
http://localhost:8080
