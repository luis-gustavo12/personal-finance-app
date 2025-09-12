

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
- Adicionar cartões de crédito e outros métodos de pagamento (complicente com o PCI DCSS)

---

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Lombok
- Spring Scheduler
- Quartz

---

## Estrutura do Projeto

- No momento, existe somente a parte de web. O diretório root do Spring, localiza-se no caminho **web/Finance**

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

    - Depois de feito o clone, mude o diretório para o root do Spring Boot.

    ```bash
    cd web/Finance
    ```

2. **Configure o Banco de Dados MySQL**
    - Dê um nome ao banco de dados (exemplo: `finances_app`)
    - Crie o arquivo `src/main/resources/application.properties`, e coloque as seguintes informações nele

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/finances_app
    spring.datasource.username=my_user
    spring.datasource.password=pwd
    ```

    - Então, crie o banco de dados no MySQL
    ```SQL
    CREATE DATABASE finances_app;
    ```

    - Não se esqueça de dar acesso ao usuário dentro do banco de dados.

    ```SQL
    CREATE USER 'my_user'@'localhost' IDENTIFIED BY 'pwd';
    GRANT ALL PRIVILEGES ON finances_app.* TO 'my_user'@'localhost';
    FLUSH PRIVILEGES;
    ```

    - Os arquivos de migração do Flyway Migration cuidarão de criar as tabelas

3. **Adicione as configurações necessárias para rodar a aplicação**

    - Para uso de gateway de cartões
        - Ir no _application.properties_, e preencher o seguinte campo, da seguinte maneira:  *cardgateway.provider=STRIPE*
        - Logo após, você precisa da suas chaves públicas e privadas da stripe, e referenciar também no _application.properties_:
        ```properties
        cardgateway.provider=STRIPE
        cardgateway.public-key=PUBLIC_KEY
        cardgateway.private-key=PRIVATE_KEY
        ```

        - Você pode criar uma no [website](https://docs.stripe.com/keys)

    - Para usar dos serviços de criptografia
        - Adicionar a seguinte linha no application.properties
        ```properties
        aes.secret.key=SECRET_KEY
        ```
        - De recomendação, é possível adicionar uma variável de ambiente, para nao divulgar a chave de encriptação
        ```properties
        aes.secret.key=${SECRET_KEY}
        ```
        - Para gerar uma, você pode rodar o seguinte comando no Linux:
        ```bash
        openssl rand -base64 32
        ``` 
    - Para usar o Quartz
        - Adicionar a seguinte linha no application.properties
        ```properties        
        spring.quartz.jdbc.initialize-schema=never
        spring.quartz.job-store-type=jdbc
        spring.quartz.properties.org.quartz.jobStore.tablePrefix=QRTZ_
        spring.quartz.properties.org.quartz.threadPool.threadCount=10
        ```        
    - Para tokens JWT
        - Adicionar as seguintes linhas no application.properties
        ```properties
        jwt.secret.key=SECRET_KEY
        jwt.issuer=ISSUER
        ```
        - De recomendação, é possibile adicionar uma variável de ambiente, para nao divulgar a chave de encriptação
        ```properties
        jwt.secret.key=${SECRET_KEY}
        jwt.issuer=${ISSUER}
        ```
    - Para usar o Spring Mail:
        - Adicionar a seguinte linha no application.properties
        ```properties
        spring.mail.host=smtp.gmail.com
        spring.mail.port=587
        spring.mail.username=EMAIL
        spring.mail.password=PASSWORD
        spring.mail.properties.mail.smtp.auth=true
        spring.mail.properties.mail.smtp.starttls.enable=true
        ```
        - As demais configurações devem ser adicionadas conforme a orientação do seu email, na configuraçãod o seu SMPT



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
