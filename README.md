

<p align="center">
  <a href="#portugues">Português</a> • <a href="#english">English</a>
</p>

---
<a id="portugues"></a>

# Personal Finance App (Aplicativo de Finanças Pessoais)

Este é o meu Aplicativo de Finanças Pessoais, criado para auxiliar no rastreamento de despesas e gerenciamento financeiro. O projeto foi desenvolvido com foco no público brasileiro, portanto, a interface do usuário está em português.

O código-fonte, comentários e a documentação técnica seguem o padrão internacional em inglês, visando as melhores práticas de desenvolvimento e facilitando a colaboração.

---

## Funcionalidades

- Autenticação (Spring Security e Encriptação BCrypt)
- Rastreamento de assinaturas mensais
- Adicionar cartões de crédito e outros métodos de pagamento

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Lombok

---

## Rodando a Aplicação


### Pré-requisitos:

    - Java JDK 17
    - MySQL 8
    - Git

---


1. **Clone o repositório**
    ```bash
    git clone https://github.com/luis-gustavo12/personal-finance-app.git
    ```
2. **Configure o Banco de Dados MySQL**
    - Dê um nome ao banco de dados (exemplo: `finances_app`)
    - Configure o arquivo `src/main/resources/application.properties`

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/finances_app
    spring.datasource.username=user
    spring.datasource.password=pwd
    ```

    - Então, crie o banco de dados no MySQL
    ```SQL
    CREATE DATABASE finances_app;
    ```

    - Os arquivos do Flyway Migration cuidará de criar as tabelas

3. **Adicione as variáveis de ambiente necessárias para rodar a aplicação, de acordo com o sistema operacional**
    - Para o Flyway:
        - Crie uma variável para o usuário, senha, e link para o banco de dados, para o flyway, sendo eles *FLYWAY_USER*, *FLYWAY_PASSWORD*, e *FLYWAY_URL* respectivamente.
        - As variáveis em questão já estão referenciadas no _POM.xml_

    - Para usar a encriptação:
        - Crie a variável para a chave de encriptação AES: *AES_SECRET_KEY*
        - Referencie a variável no arquivo _application.properties_
        ```properties
        aes.secret.key=${AES_SECRET_KEY}
        ```
    - Para acessar a API do Exchange Rate (para conversão em tempo real de moedas):
        - Crie a variável *EXCHANGE_RATE_API_KEY*
        - Preencha com o valor referente à chave
        - Você pode criar uma no [site](https://exchangerate.host/signup/free)
        

4. **Compile o projeto**
    ```bash
    ./mvnw clean install
    ```

5. **Então, execute a aplicação**
    ```bash
    ./mvnw spring-boot:run
    ```

- A aplicação estará rodando no [localhost](http://localhost:8080)


## Observações

*   **Idioma:** A interface do usuário está em português (PT-BR) para melhor experiência do público brasileiro. O código e a documentação técnica estão em inglês.



---
<br>
<a id="english"></a>
<!-- English Content Starts Here -->

# Personal Finance App

This is my Personal Finance App, created to help track expenses and manage personal finances. The user interface is in Portuguese, as the project was initially developed with a Brazilian audience in mind.

The source code, comments, and technical documentation follow international standards in English, aiming for best development practices and ease of collaboration.

---

## Features

- Authentication
- Monthly subscriptions tracking
- Add credit cards, and other payment methods
- Add income from Account to Account

---

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Lombok
- Spring Mail

---

## Notes

*   **Language:** The user interface is in Portuguese (PT-BR) to best serve the Brazilian audience. The code and technical documentation are in English.
